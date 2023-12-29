package com.hdel.miri.concurrent.domain.dgk;

import com.hdel.miri.concurrent.domain.dgk.vo.CcLogVO;
import com.hdel.miri.concurrent.domain.dgk.vo.ReqVO;
import com.hdel.miri.concurrent.domain.dgk.vo.ReqVO.ElVO;
import com.hdel.miri.concurrent.domain.dgk.vo.ReqVO.TargetMailVO;
import com.hdel.miri.concurrent.domain.dgk.vo.RespVO;
import com.hdel.miri.concurrent.domain.dgk.vo.RespVO.ConcurrentLogVO;
import com.hdel.miri.concurrent.domain.hccc.HCCCRepository;
import com.hdel.miri.concurrent.domain.mail.MailService;
import com.hdel.miri.concurrent.domain.message.CcMessageVO.MailVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.hdel.miri.concurrent.domain.sap.SAPAsyncService;
import com.hdel.miri.concurrent.domain.sap.SAPScrmService;
import com.hdel.miri.concurrent.domain.sap.SAPSrmService;
import com.hdel.miri.concurrent.domain.scrm.SCRM;
import com.hdel.miri.concurrent.domain.scrm.SCRMRepository;
import com.hdel.miri.concurrent.domain.srm.SRMRepository;
import com.hdel.miri.concurrent.domain.user.User;
import com.hdel.miri.concurrent.global.config.OpenURIConfig;
import com.ibm.db2.cmx.runtime.internal.repository.util.Base64;

import com.hdel.miri.concurrent.domain.mms.mmsService;
import com.hdel.miri.concurrent.domain.mms.mmsVO;

import java.util.Map;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import com.sap.conn.jco.ext.DestinationDataProvider;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.ext.DataProviderException;
import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.Environment;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonObject;
import com.hdel.miri.concurrent.domain.dgk.vo.CcElevatorVO;

@Slf4j
@Service
@RequiredArgsConstructor
public class CcService {
    private final CcRepository ccRepository;
    private final CcAsyncService ccAsyncService;
    private final SAPScrmService sapScrmService;
    private final SAPSrmService sapSrmService;
    private static final String DESC_NAME1 = "MIRI-SCRM";
    private static final String DESC_NAME2 = "MIRI-SRM";
    private final MailService mailService;
    private final mmsService mmsService;
    private final PasswordEncoder passwordEncoder;

    private final SCRMRepository scrmRepository;
    private final SRMRepository srmRepository;
    private final OpenURIConfig openURIConfig;
    private final ServletContext context;

    private final HCCCRepository hcccRepository;

    @Value("${spring.api.dataGoKr}")
    private String api_key;

    @Value("${spring.api.buildingInfo}")
    private String buildingInfoUrl;

    @Value("${spring.api.inspectHis}")
    private String inspectHisUrl;

    @Value("${spring.api.inspectErrorHis}")
    private String inspectErrorHisUrl;

    @Value("${spring.api.selfInspectHis}")
    private String selfInspectHisUrl;

    @Value("${spring.api.insuranceInfo}")
    private String insuranceInfoUrl;

    @Value("${spring.api.safeManager}")
    private String safeManagerUrl;

    // for SAP
    @Value("${spring.sap.srm.mshost}")
    private String srm_mshost;

    @Value("${spring.sap.srm.ashost}")
    private String srm_ashost;

    @Value("${spring.sap.srm.msserv}")
    private String srm_msserv;

    @Value("${spring.sap.srm.r3name}")
    private String srm_r3name;

    @Value("${spring.sap.srm.sysnr}")
    private String srm_sysnr;

    @Value("${spring.sap.srm.client}")
    private String srm_client;

    @Value("${spring.sap.srm.group}")
    private String srm_group;

    @Value("${spring.sap.srm.user}")
    private String srm_user;

    @Value("${spring.sap.srm.passwd}")
    private String srm_passwd;

    @Value("${spring.sap.srm.lang}")
    private String srm_lang;

    // scrm
    @Value("${spring.sap.scrm.mshost}")
    private String scrm_mshost;

    @Value("${spring.sap.scrm.ashost}")
    private String scrm_ashost;

    @Value("${spring.sap.scrm.msserv}")
    private String scrm_msserv;

    @Value("${spring.sap.scrm.r3name}")
    private String scrm_r3name;

    @Value("${spring.sap.scrm.sysnr}")
    private String scrm_sysnr;

    @Value("${spring.sap.scrm.client}")
    private String scrm_client;

    @Value("${spring.sap.scrm.group}")
    private String scrm_group;

    @Value("${spring.sap.scrm.user}")
    private String scrm_user;

    @Value("${spring.sap.scrm.passwd}")
    private String scrm_passwd;

    @Value("${spring.sap.scrm.lang}")
    private String scrm_lang;

    @Value("https://miri-api.hdel.co.kr/miri/v2/user")
    private String miri_service;

    private static InMemoryDestinationDataProvider mp = new InMemoryDestinationDataProvider();
    // {
    //     "userLocale": "ko_kr",
    //     "userId": "test@aa.bb",
    //     "userName": "홍길동",
    //     "password": "1234",
    //     "postnumber": "123-123",
    //     "address": "대한민국 어딘가",
    //     "detailaddress": "대한민국 어딘가",
    //     "note": "대한민국 어딘가",
    //     "phonenumber": "010-0000-0000",
    //     "termsServiceUseAg": "y | n",
    //     "termsPlInfoUsingAg": "y | n",
    //     "termsPlInfoStoreTimeAg": "y | n",
    //     "termsAdRecvAg": "y | n",
    //     "lobby": [
    //       {
    //         "intgPrjNo": "a123455",
    //         "trlineCd": "A01",
    //         "model": "WBSS",
    //         "siteName": "파랑주간보호센터",
    //         "addr": "전라북도 전주시.."
    //       }
    //     ],
    //     "firebaseUserId": "xdjfoiwejfoweifw",
    //     "iuserId": "string"
    //   }

    public void joinManager(){
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(miri_service+"/join/manager");

        HttpResponse response = null;
        try {
            JsonObject object=new JsonObject();
            object.addProperty("userId","manwook.goh@hyundaielevator.com");
            object.addProperty("password","1");
            object.addProperty("userName","들국화");
            object.addProperty("address","연지동");
            object.addProperty("detailaddress","현대그룹빌딩");
            object.addProperty("phonenumber","01912122212");

             // Set the json as the body of the request
            httpPost.setEntity(new StringEntity(object.toString()));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            response = httpClient.execute(httpPost);
            String jsonResponse = EntityUtils.toString(response.getEntity());

            if (!StringUtils.isEmpty(jsonResponse)) {
                log.error("jsonResponse=:{}", jsonResponse);
            }
            //     return result;
        } catch (Exception ex) {
                System.out.println("에러" + ex);
        }
    }


    /*
     * 제목 : 건물별 엘리베이터 정보 동기화
     * API : 국가 데이타 센터
     * SRM을 먼저 등록후 SCRM을 등록한다 -> 만약 SCRM /SRM에 중복데이타가 있는경우 SCRM을 우선하기 위함
     */
    public void syncBuildingElInfo() {
        List<String> _data = ccRepository.getTargetElList("SCRM");
        postElInfoFromDGK("SCRM", _data, buildingInfoUrl, api_key);

        _data = ccRepository.getTargetElList("SRM");
        postElInfoFromDGK("SRM", _data, buildingInfoUrl, api_key);

    }

    private void postElInfoFromDGK(String db_type, List<String> data, String dgkUrl, String dgkApiKey) {
        StringBuffer paramBuffer = new StringBuffer();
        for (int i = 0; i < data.size(); i++) {
            paramBuffer.delete(0, paramBuffer.length());
            paramBuffer.append("&elevator_no=" + data.get(i));
            try {
                // 초당 30개만 처리가능한 국가데이타 센터를 고려하여 5개 요청후 1초대기
                if (i % 15 == 0) {
                    Thread.sleep(1000);
                }
                ccAsyncService.postElInfoFromDGK(db_type, data.get(i), dgkUrl, paramBuffer.toString(), dgkApiKey);
            } catch (Exception ex) {
                System.out.println("에러" + ex);
            }
        }
    }

    /* 2023-07-18 수정
     * 제목 : 건물별 엘리베이터 정보 동기화
     * 내용 : SCRM / SRM 순서 관계없이 최종수정일 오름 차순으로 처리 한다.
     */
    public int syncBuildingElInfoNew() {
        //최신일자 오름차순 el 추출
        List<ReqVO.ElVO> _data = ccRepository.getTargetElListNew();
        try{
            if(_data.size() == 0) return 0;

            for (int i = 0; i < _data.size(); i++) {
                if("y".equals(_data.get(i).getDel_yn())){
                    //EL 삭제 처리
                    ccRepository.deleteElevatorInfo(_data.get(i).getElevator_no());
                }else{
                    StringBuffer paramBuffer = new StringBuffer();
                    paramBuffer.delete(0, paramBuffer.length());
                    paramBuffer.append("&elevator_no=" +  _data.get(i).getElevator_no());
                    ccAsyncService.postElInfoFromDGK(_data.get(i).getDb_type(), _data.get(i).getElevator_no(), buildingInfoUrl,
                                                    paramBuffer.toString(), api_key);
                }
                // 초당 30개만 처리가능한 국가데이타 센터를 고려하여 15개 요청후 1초대기
                if (i % 15 == 0) {
                    Thread.sleep(1000);
                }
                log.error("elevator_no=["+ _data.get(i).getElevator_no() +"]-------["+i+"]");
                //Thread.sleep(500);
            }
            return 1;
        } catch (Exception ex) {
                log.error("에러 :" + ex.getMessage());
                return 0;
        }
    }
    /* 2023-10-04
     * 제목 : 거래선 중복에 따른 엘리베이터 정보 동기화
     * 내용 : SCRM / SRM 순서 관계없이 최종수정일 오름 차순으로 처리 한다.
     */
    public int syncScrmSrmElInfo() {
        //프로젝트번호,호기,원프로젝트번호,거래선 중복되는 El_NO 추출
        List<ReqVO.ElInfoVO> _data = ccRepository.getCheckElInfo();
        List<SCRM.VO> _sEList = null;
        try{
            if (_data.size() == 0) {
                ccRepository.deleteExpiredElevatorInfo("2");
                return 0;
            }

            for (int i = 0; i < _data.size(); i++) {

                if("SCRM".equals(_data.get(i).getDbtype())){
                    //SCRM 정보 가져오기
                    _sEList = scrmRepository.getScrmElInfo(_data.get(i).getElevator_no());
                }else{
                    //SRM 정보 가져오기
                    _sEList = srmRepository.getSrmElInfo(_data.get(i).getElevator_no());
                }
                // el_no의 정보 변경 여부 체크
                if (_sEList != null && _sEList.size() > 0) {
                    //chkItem 이 El_INFO 테이블 과 같지 않을 경우 EL_INFO 등록
                    //같을 경우는 skip
                    if(!_data.get(i).getChkitem().equals(_sEList.get(0).getChkitem())){
                        // EL info 등록
                        StringBuffer paramBuffer = new StringBuffer();
                        paramBuffer.delete(0, paramBuffer.length());
                        paramBuffer.append("&elevator_no=" +  _data.get(i).getElevator_no());
                        ccAsyncService.postElInfoFromDGK(_data.get(i).getDbtype(), _data.get(i).getElevator_no(), buildingInfoUrl,
                                                        paramBuffer.toString(), api_key);
                    }
                }else{
                    //_데이터 없는 경우 .해당 EL_NO 관련 데이터 삭제 처리
                    ccRepository.deleteElevatorInfo(_data.get(i).getElevator_no());
                }
                log.error("elevator_no=["+ _data.get(i).getElevator_no() +"]-------["+i+"]");
                //Thread.sleep(500);
            }
            
            Thread.sleep(5000);
            ccRepository.deleteExpiredElevatorInfo("2");
            return 1;
        } catch (Exception ex) {
                log.error("에러 :" + ex.getMessage());
            return 0;
        }
    }

    /* 2023-10-17 add
     * 제목 : 엘리베이터 정보 동기화(위도/경도)
     * 내용 : HCCC 에서 누락된 위도/경도를 조회하여 EL_INFO  update 한다.
     * 대상 : el_info - wgslat , wgslon is null 인 pjt_no, ho_no
     */
    public int syncUpdateElInfo() {
        //최신일자 오름차순 el 추출
        List<SCRM.VO> _data = ccRepository.getUpdateElInfo();
        try{
            if(_data.size() == 0) return 0;

            List<SCRM.VO> _step = hcccRepository.getGisFromHcccUpdate(_data);

            List<List<SCRM.VO>> partitionedItems = ListUtils.partition(_step, 1000);

            partitionedItems.forEach(item -> {
                ccRepository.updateElInfo(item);
            });

            return 1;

        } catch (Exception ex) {
                log.error("에러 :" + ex.getMessage());
                return 0;
        }
    }
    /*
     * 제목 : 엘리베이터 보험가입정보 동기화
     * API : 국가 데이터 센터
     */
    public void syncInssuranceInfo(ReqVO.SyncElevatorTargetVO request) {
        List<String> _data = (request.getElList() != null && (0 < request.getElList().size())) ? request.getElList()
                : ccRepository.getInssuranceTargetElInfo();
        StringBuffer paramBuffer = new StringBuffer();
        Date date_now = new Date(System.currentTimeMillis());
        SimpleDateFormat fourteen_format = new SimpleDateFormat("yyyyMMdd");
        String nowDt = fourteen_format.format(date_now);

        for (int i = 0; i < _data.size(); i++) {
            paramBuffer.delete(0, paramBuffer.length());
            paramBuffer.append("&page_no=1");
            paramBuffer.append("&numOfRows=100");
            paramBuffer.append("&elevator_no=" + _data.get(i));
            paramBuffer.append("&cont_ymd=" + nowDt);
            try {
                // 초당 30개만 처리가능한 국가데이타 센터를 고려하여 5개 요청후 1초대기
                if (i % 15 == 0) {
                    Thread.sleep(1000);
                }
                ccAsyncService.postInssuranceInfo(_data.get(i), insuranceInfoUrl, paramBuffer.toString(), api_key);
                // Thread.sleep(1000);
            } catch (Exception ex) {
                System.out.println("-----------------------------------보험에러----------------------------------\n");
                System.out.println("에러 elevator_no : " + _data.get(i) + " : " + ex);
            }
        }
    }

    /*
     * 제목 : 엘리베이터 안전관리자 정보 동기화
     * API : 국가 데이터 센터
     */
    public void syncSafetyMgrInfo(ReqVO.SyncElevatorTargetVO request) {
        List<String> _data = (request.getElList() != null && (0 < request.getElList().size())) ? request.getElList()
                : ccRepository.getSafetyMgrElInfo();
        StringBuffer paramBuffer = new StringBuffer();

        for (int i = 0; i < _data.size(); i++) {
            paramBuffer.delete(0, paramBuffer.length());
            paramBuffer.append("&page_no=1");
            paramBuffer.append("&numOfRows=100");
            paramBuffer.append("&elevator_no=" + _data.get(i));

            try {
                // 초당 30개만 처리가능한 국가데이타 센터를 고려하여 5개 요청후 1초대기
                if (i % 15 == 0) {
                    Thread.sleep(1000);
                }
                ccAsyncService.postSafetyMgrInfo(_data.get(i), safeManagerUrl, paramBuffer.toString(), api_key);
            } catch (Exception ex) {
                System.out.println("에러" + ex);
            }
        }
    }

    /*
     * 제목 : 엘리베이터 자체점검 정보 동기화
     * API : 국가 데이터 센터
     */
    public void syncSelfInspHisInfo(ReqVO.SyncElevatorTargetVO2 request) {
        List<String> _data = (request.getElList() != null && (0 < request.getElList().size())) ? request.getElList()
                : ccRepository.getAllElInfo();
        StringBuffer paramBuffer = new StringBuffer();

        Date date_now = new Date(System.currentTimeMillis());
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        SimpleDateFormat yyymmddFormat = new SimpleDateFormat("yyyyMM");
        String nowDay = dayFormat.format(date_now);
        String currentMonth = yyymmddFormat.format(date_now);

        int nowDtn = Integer.parseInt(nowDay);

        LocalDate now = LocalDate.now();
        LocalDate prevMonth = now.minusMonths(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        String prevMonthStr = prevMonth.format(formatter);

        System.out.println("오늘날짜 nowDay :" + nowDay);
        System.out.println("당월 :" + currentMonth);
        System.out.println("전월 :" + prevMonthStr);

        if (request.getSyncYYYMM() != null && !"".equals(request.getSyncYYYMM())) {
            for (int i = 0; i < _data.size(); i++) {
                paramBuffer.delete(0, paramBuffer.length());
                paramBuffer.append("&page_no=1");
                paramBuffer.append("&numOfRows=200");
                paramBuffer.append("&elevator_no=" + _data.get(i));
                paramBuffer.append("&yyyymm=" + request.getSyncYYYMM());

                try {
                    // 초당 30개만 처리가능한 국가데이타 센터를 고려하여 5개 요청후 1초대기
                    if (i % 15 == 0) {
                        Thread.sleep(1000);
                    }
                    ccAsyncService.postSelfInspHisInfo(request.getSyncYYYMM(), _data.get(i), selfInspectHisUrl,
                            paramBuffer.toString(), api_key);
                } catch (Exception ex) {
                    System.out.println("에러" + ex);
                }
            }
        } else {
            // 승강기 자체점검결과는 익월 10일전까지 반드시 입력하여야 한다는 국가정책이 있습니다.
            // 하여 매월 10일 전까지는 전월실적도 같이 Update가 필요합니다.
            if (nowDtn < 11) {
                for (int i = 0; i < _data.size(); i++) {
                    paramBuffer.delete(0, paramBuffer.length());
                    paramBuffer.append("&page_no=1");
                    paramBuffer.append("&numOfRows=100");
                    paramBuffer.append("&elevator_no=" + _data.get(i));
                    paramBuffer.append("&yyyymm=" + prevMonthStr);

                    try {
                        // 초당 30개만 처리가능한 국가데이타 센터를 고려하여 5개 요청후 1초대기
                        if (i % 15 == 0) {
                            Thread.sleep(1000);
                        }
                        ccAsyncService.postSelfInspHisInfo(prevMonthStr, _data.get(i), selfInspectHisUrl,
                                paramBuffer.toString(), api_key);
                    } catch (Exception ex) {
                        System.out.println("에러" + ex);
                    }
                }
            }
            // 현재월 Update
            for (int i = 0; i < _data.size(); i++) {
                paramBuffer.delete(0, paramBuffer.length());
                paramBuffer.append("&page_no=1");
                paramBuffer.append("&numOfRows=200");
                paramBuffer.append("&elevator_no=" + _data.get(i));
                paramBuffer.append("&yyyymm=" + currentMonth);

                try {
                    // 초당 30개만 처리가능한 국가데이타 센터를 고려하여 5개 요청후 1초대기
                    if (i % 15 == 0) {
                        Thread.sleep(1000);
                    }
                    ccAsyncService.postSelfInspHisInfo(currentMonth, _data.get(i), selfInspectHisUrl,
                            paramBuffer.toString(), api_key);
                } catch (Exception ex) {
                    System.out.println("에러" + ex);
                }
            }
        }

    }

    /*
     * 제목 : 엘리베이터 정기검사 정보 동기화
     * API : 국가 데이터 센터
     */
    public void syncInspHisInfo(ReqVO.SyncInspElevatorTargetVO request) {
        // List<String> _data = (request.getElList() != null && (0 <
        // request.getElList().size()))
        // ?request.getElList():ccRepository.getAllElInfo();
        List<String> _data = (request.getElList() != null && (0 < request.getElList().size())) ? request.getElList()
                : ccRepository.getInspHistTargetList();

        StringBuffer paramBuffer = new StringBuffer();

        LocalDate now = LocalDate.now();
        LocalDate prevMonth = now.minusDays(7);
        DateTimeFormatter yyymmddFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
        String toDay = now.format(yyymmddFormat);
        String prevDay = prevMonth.format(yyymmddFormat);

        System.out.println("오늘 :" + toDay);
        System.out.println("어제 :" + prevDay);

        if (request.getSyncYear() != null && !"".equals(request.getSyncYear())) {
            // temporary
            LocalDate date = LocalDate.parse(request.getSyncYear() + "-01-01");
            for (int i = 0; i < _data.size(); i++) {
                for (int j = 0; j < 12; j++) {
                    LocalDate initDate = date.plusMonths(j);

                    YearMonth month = YearMonth.from(initDate);
                    LocalDate firstDate = month.atDay(1);
                    LocalDate lastDate = month.atEndOfMonth();

                    paramBuffer.delete(0, paramBuffer.length());
                    paramBuffer.append("&page_no=1");
                    paramBuffer.append("&numOfRows=100");
                    paramBuffer.append("&elevator_no=" + _data.get(i));
                    paramBuffer.append("&appr_sdt=" + firstDate.format(yyymmddFormat));
                    paramBuffer.append("&appr_edt=" + lastDate.format(yyymmddFormat));

                    try {
                        ccAsyncService.postInspHisInfo(_data.get(i), inspectHisUrl, paramBuffer.toString(), api_key);
                    } catch (Exception ex) {
                        System.out.println("에러" + ex);
                    }
                }
            }
        } else {
            // prevDay ~ toDay (7일)
            for (int i = 0; i < _data.size(); i++) {
                paramBuffer.delete(0, paramBuffer.length());
                paramBuffer.append("&page_no=1");
                paramBuffer.append("&numOfRows=100");
                paramBuffer.append("&elevator_no=" + _data.get(i));
                paramBuffer.append("&appr_sdt=" + prevDay);
                paramBuffer.append("&appr_edt=" + toDay);
                try {
                    ccAsyncService.postInspHisInfo(_data.get(i), inspectHisUrl, paramBuffer.toString(), api_key);
                } catch (Exception ex) {
                    System.out.println("에러" + ex);
                }
            }
        }
    }

    /*
     * 제목 : 엘리베이터 정기검사 정보 동기화
     * API : 국가 데이터 센터 과거 데이터 추출 대상 : 2018~2023 까지
     */
    public void syncInspHisInfoLast(ReqVO.SyncInspElevatorTargetVO request) {
        List<String> _data = (request.getElList() != null && (0 < request.getElList().size())) ? request.getElList()
                : ccRepository.getInspHistTargetList();
        StringBuffer paramBuffer = new StringBuffer();

        LocalDate now = LocalDate.now();
        LocalDate prevMonth = now.minusDays(7);
        DateTimeFormatter yyymmddFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
        String toDay = now.format(yyymmddFormat);
        String prevDay = prevMonth.format(yyymmddFormat);

        System.out.println("오늘 :" + toDay);
        System.out.println("어제 :" + prevDay);

        if (request.getSyncYear() != null && !"".equals(request.getSyncYear())) {
            //년도로 들어올때 2022-> 01 ~ 12월
            if(request.getSyncYear().length() == 4){
                LocalDate date1 = LocalDate.parse(request.getSyncYear() + "-01-01");

                for (int i = 0; i < _data.size(); i++) {
                    for (int j = 0; j < 12; j++) {
                        LocalDate initDate = date1.plusMonths(j);

                        YearMonth month1 = YearMonth.from(initDate);
                        LocalDate firstDate1 = month1.atDay(1);
                        LocalDate lastDate1 = month1.atEndOfMonth();

                        paramBuffer.delete(0, paramBuffer.length());
                        paramBuffer.append("&page_no=1");
                        paramBuffer.append("&numOfRows=100");
                        paramBuffer.append("&elevator_no=" + _data.get(i));
                        paramBuffer.append("&appr_sdt=" + firstDate1.format(yyymmddFormat));
                        paramBuffer.append("&appr_edt=" + lastDate1.format(yyymmddFormat));

                        //System.out.println("Start_date=End_date =["+firstDate1.format(yyymmddFormat)+"]=["+lastDate1.format(yyymmddFormat)+"]");

                        try {
                           ccAsyncService.postInspHisInfo(_data.get(i), inspectHisUrl, paramBuffer.toString(), api_key);
                        } catch (Exception ex) {
                            System.out.println("에러:" + ex);
                        }
                    }
                }

            }else{
                // temporary  --월로 돌리기
                // 년월 : 2022-12
                LocalDate date = LocalDate.parse(request.getSyncYear() + "-01");
                YearMonth month = YearMonth.from(date);

                String firstDate = month.atDay(1).format(yyymmddFormat);
                String lastDate = month.atEndOfMonth().format(yyymmddFormat);

                log.info("Start_date-End_date=["+firstDate+"] ["+lastDate+"]");
                for (int i = 0; i < _data.size(); i++) {
                    paramBuffer.delete(0, paramBuffer.length());
                    paramBuffer.append("&page_no=1");
                    paramBuffer.append("&numOfRows=100");
                    paramBuffer.append("&elevator_no=" + _data.get(i));
                    paramBuffer.append("&appr_sdt=" + firstDate);
                    paramBuffer.append("&appr_edt=" + lastDate);
                    try {
                        ccAsyncService.postInspHisInfo(_data.get(i), inspectHisUrl, paramBuffer.toString(), api_key);
                        Thread.sleep(100);
                    } catch (Exception ex) {
                        log.error("에러:" + ex.getMessage());
                    }
                }
            }
        }
    }
    // 현재월 Update

    /*
     * 제목 : SAP 미수금 조회
     * API : SAP RFC Call
     */
    public void syncUnbilledInfo(ReqVO.UnBilledElevatorTargetVO request) {
        try {
            if (!Environment.isDestinationDataProviderRegistered()) {
                Environment.registerDestinationDataProvider(mp);
            }
        } catch (IllegalStateException providerAlreadyRegisteredException) {
            providerAlreadyRegisteredException.printStackTrace();
            throw providerAlreadyRegisteredException;
        }

        String apType = System.getProperty("app.aptype");

        if ("ap1".equals(apType)) {
            log.error("[MISU-SCRM:Start]-"+apType);
            List<String> _scrmList = request.getScrmList();

            //기존 데이타를 남기고 추가로 등록하는 경우가 아니면 기존 미수금 내역은 삭제한다.
            if ((_scrmList == null || _scrmList.isEmpty())) {
               ccRepository.clearUnbilledInfo("SCRM");
            }

            try {
                mp.changeProperties(DESC_NAME1, getSCRMDesc());
                List<String> _elList = (_scrmList != null && !_scrmList.isEmpty()) ? _scrmList
                        : ccRepository.getElInfoBySrc("SCRM");
                List<List<String>> partitionedItems = ListUtils.partition(_elList, 1000);
                List<CcElevatorVO.CcElevatorInfoForSAP> _data = ccRepository.getAllElInfoForSAP(partitionedItems);
                if (_data != null && _data.size() > 0)
                    sapScrmService.syncUnbilledInfo(_data);
            } catch (Exception ex) {
                System.out.println("UnBilled... TYPE : SCRM ->" + ex.toString());
                log.error("[MISU-SCRM:Error]-"+ ex.getMessage());
            }
        } else {
            log.error("[MISU-SRM:Start]-"+apType);
            List<String> _srmList = request.getSrmList();

            //기존 데이타를 남기고 추가로 등록하는 경우가 아니면 기존 미수금 내역은 삭제한다
            if ((_srmList == null || _srmList.isEmpty())) {
                ccRepository.clearUnbilledInfo("SRM");
            }

            try {
                mp.changeProperties(DESC_NAME2, getSRMDesc());
                List<String> _elList = (_srmList != null && !_srmList.isEmpty()) ? _srmList
                        : ccRepository.getElInfoBySrc("SRM");
                List<List<String>> partitionedItems = ListUtils.partition(_elList, 1000);
                List<CcElevatorVO.CcElevatorInfoForSAP> _data = ccRepository.getAllElInfoForSAP(partitionedItems);
                if (_data != null && _data.size() > 0)
                    sapSrmService.syncUnbilledInfo(_data);
            } catch (Exception ex) {
                System.out.println("UnBilled... TYPE : SRM");
                log.error("[MISU-SRM:Error]-" + ex.getMessage());
            }
        }
    }

    /*
     * 제목 : 정주기 API 에러난것들 재처리
     * API : FIX API ERRORS
     */
    public void fixApiErrors() {
        ReqVO.SyncElevatorTargetVO _reqVO = null;
        ReqVO.SyncInspElevatorTargetVO _reqVO2 = null;
        List<String> _elList = null;

        //건물별 엘리베이터 오류건 추출
        List<ReqVO.ElVO>  _el_list = ccRepository.getElevatorErrors();
        List<RespVO.ConcurrentLogVO> _inssurance_list = ccRepository.getConcurrentErrors("CONCURRENT USER", "보험정보");
        List<RespVO.ConcurrentLogVO> _safemgr_list = ccRepository.getConcurrentErrors("CONCURRENT USER", "안전관리자");
        List<RespVO.ConcurrentLogVO> _selfinsp_list = ccRepository.getConcurrentErrors("CONCURRENT USER", "자체점검");
        List<RespVO.ConcurrentLogVO> _insplist = ccRepository.getConcurrentErrors("CONCURRENT USER", "정기검사");

        // 건물별 엘리베이터
        if(_el_list != null && _el_list.size() > 0){
            ccRepository.deleteErrorLogsNew(_el_list);
            for (int i = 0; i < _el_list.size(); i++) {
                if("y".equals(_el_list.get(i).getDel_yn())){
                    //EL 삭제 처리
                    ccRepository.deleteElevatorInfo(_el_list.get(i).getElevator_no());
                }else{
                    StringBuffer paramBuffer = new StringBuffer();
                    paramBuffer.delete(0, paramBuffer.length());
                    paramBuffer.append("&elevator_no=" +  _el_list.get(i).getElevator_no());
                    try {
                        ccAsyncService.postElInfoFromDGK(_el_list.get(i).getDb_type(), _el_list.get(i).getElevator_no(), buildingInfoUrl,
                                                        paramBuffer.toString(), api_key);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        log.error("[El_FIX:Error]-" + e.getMessage());
                    }
                }
                log.error("error elevator_no=["+ _el_list.get(i).getElevator_no() +"]-------["+i+"]");
            }
        }
        //보험 정보
        if (_inssurance_list != null && _inssurance_list.size() > 0) {
            // 에러 Log를 먼저 삭제한다.
            ccRepository.deleteErrorLogs(_inssurance_list);
            _elList = _inssurance_list.stream().map(RespVO.ConcurrentLogVO::getElevator_no)
                    .collect(Collectors.toList());
            _reqVO = ReqVO.SyncElevatorTargetVO.builder().elList(_elList).build();
            syncInssuranceInfo(_reqVO);
        }
        //안전관리자
        if (_safemgr_list != null && _safemgr_list.size() > 0) {
            // 에러 Log를 먼저 삭제한다.
            ccRepository.deleteErrorLogs(_safemgr_list);
            _elList = _safemgr_list.stream().map(RespVO.ConcurrentLogVO::getElevator_no).collect(Collectors.toList());
            _reqVO = ReqVO.SyncElevatorTargetVO.builder().elList(_elList).build();
            syncSafetyMgrInfo(_reqVO);
        }
        // 정기점검
        if (_selfinsp_list != null && _selfinsp_list.size() > 0) {
            // 에러 Log를 먼저 삭제한다.
            ccRepository.deleteErrorLogs(_selfinsp_list);
            _elList = _selfinsp_list.stream().map(RespVO.ConcurrentLogVO::getElevator_no).collect(Collectors.toList());
            if (_elList != null && !_elList.isEmpty()) {
                for (RespVO.ConcurrentLogVO _vo : _selfinsp_list) {
                    try {
                        ccAsyncService.fixSelfInspHisInfo(_vo.getIn_params());
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            }
        }
        // 정기검사
        if (_insplist != null && _insplist.size() > 0) {
            // 에러 Log를 먼저 삭제한다.
            ccRepository.deleteErrorLogs(_insplist);
            _elList = _insplist.stream().map(RespVO.ConcurrentLogVO::getElevator_no).collect(Collectors.toList());
            _reqVO2 = ReqVO.SyncInspElevatorTargetVO.builder().elList(_elList).syncYear(null).build();
            syncInspHisInfo(_reqVO2);
        }
    }

    private Properties getSCRMDesc() {
        Properties scrm_cp = new Properties();
        if (System.getProperty("spring.profiles.active").equals("prod")) {
            // prod
            scrm_cp.setProperty(DestinationDataProvider.JCO_MSHOST, scrm_mshost);
            scrm_cp.setProperty(DestinationDataProvider.JCO_MSSERV, scrm_msserv);
            scrm_cp.setProperty(DestinationDataProvider.JCO_R3NAME, scrm_r3name);
            scrm_cp.setProperty(DestinationDataProvider.JCO_SYSNR, scrm_sysnr);
            scrm_cp.setProperty(DestinationDataProvider.JCO_CLIENT, scrm_client);
            scrm_cp.setProperty(DestinationDataProvider.JCO_GROUP, scrm_group);
            scrm_cp.setProperty(DestinationDataProvider.JCO_USER, scrm_user);
            scrm_cp.setProperty(DestinationDataProvider.JCO_PASSWD, scrm_passwd);
            scrm_cp.setProperty(DestinationDataProvider.JCO_LANG, scrm_lang);
        } else {
            scrm_cp.setProperty(DestinationDataProvider.JCO_ASHOST, scrm_ashost);
            scrm_cp.setProperty(DestinationDataProvider.JCO_SYSNR, scrm_sysnr);
            scrm_cp.setProperty(DestinationDataProvider.JCO_CLIENT, scrm_client);
            scrm_cp.setProperty(DestinationDataProvider.JCO_USER, scrm_user);
            scrm_cp.setProperty(DestinationDataProvider.JCO_PASSWD, scrm_passwd);
            scrm_cp.setProperty(DestinationDataProvider.JCO_LANG, scrm_lang);
        }
        return scrm_cp;
    }

    private Properties getSRMDesc() {
        Properties srm_cp = new Properties();
        if (System.getProperty("spring.profiles.active").equals("prod")) {
            // prod
            srm_cp.setProperty(DestinationDataProvider.JCO_ASHOST, srm_ashost);
            srm_cp.setProperty(DestinationDataProvider.JCO_SYSNR, srm_sysnr);
            srm_cp.setProperty(DestinationDataProvider.JCO_CLIENT, srm_client);
            srm_cp.setProperty(DestinationDataProvider.JCO_USER, srm_user);
            srm_cp.setProperty(DestinationDataProvider.JCO_PASSWD, srm_passwd);
            srm_cp.setProperty(DestinationDataProvider.JCO_LANG, srm_lang);
        } else {
            srm_cp.setProperty(DestinationDataProvider.JCO_ASHOST, srm_ashost);
            srm_cp.setProperty(DestinationDataProvider.JCO_SYSNR, srm_sysnr);
            srm_cp.setProperty(DestinationDataProvider.JCO_CLIENT, srm_client);
            srm_cp.setProperty(DestinationDataProvider.JCO_USER, srm_user);
            srm_cp.setProperty(DestinationDataProvider.JCO_PASSWD, srm_passwd);
            srm_cp.setProperty(DestinationDataProvider.JCO_LANG, srm_lang);
        }
        return srm_cp;
    }

    private static class InMemoryDestinationDataProvider implements DestinationDataProvider {
        private DestinationDataEventListener eL;
        private HashMap<String, Properties> secureDBStorage = new HashMap<String, Properties>();

        @Override
        public Properties getDestinationProperties(String destinationName) {
            try {
                // read the destination from DB
                Properties p = secureDBStorage.get(destinationName);

                // check if all is correct
                if (p != null && p.isEmpty())
                    throw new DataProviderException(DataProviderException.Reason.INVALID_CONFIGURATION,
                            "destination configuration is incorrect", null);

                return p;
            } catch (RuntimeException re) {
                throw new DataProviderException(DataProviderException.Reason.INTERNAL_ERROR, re);
            }
        }

        @Override
        public void setDestinationDataEventListener(DestinationDataEventListener eventListener) {
            this.eL = eventListener;
        }

        @Override
        public boolean supportsEvents() {
            return true;
        }

        void changeProperties(String destName, Properties properties) {
            synchronized (secureDBStorage) {
                if (properties == null) {
                    if (secureDBStorage.remove(destName) != null)
                        eL.deleted(destName);
                } else {
                    secureDBStorage.put(destName, properties);
                    eL.updated(destName); // create or updated
                }
            }
        }
    }

    /*
     * 제목 : 영업사원 계약 변경 시 처리
     * API : 국가 데이터 센터
     */
    public void syncAccountChangeInfo() {
        try {
            // 1. 통합프로젝트 코드가 존재하지 않는 portfolio 매핑 건 삭제 ( 전체 )
            // 2. 통합프로젝트는 같으나, 영업사원이 변경된 portfolio 매핑 삭제한다 (영업사원인 경우)
            // 3. 변경된 영업사원의 lobby 에 계약건 입력한다.
            // 4. 신규계약건 portfolio 생성
            ccRepository.deleteAccountChange();
        } catch (Exception ex) {
            System.out.println("에러" + ex);
        }
    }

    /*
     * 제목 : 영업사원 메일 전송 ( 1주일 간 등록 된 건물관리자 목록 ) - OLD
     * 내용 : 벌크로 등록된 건물 관리자 정보를 유상계약번호 정보를 참조해 관련 영업사원에게 메일을 전송 함.
     */
    public void sendMailAccountOld() {
        try{
            // 메일 전송 대상 조회
            List<ReqVO.TargetMailVO> _data = ccRepository.getSendMailAccount();

            if ( !_data.isEmpty() ){

                //중복 accId 제외 : distinct accId 정리
                List<String> accIdList = _data.stream().map(ReqVO.TargetMailVO::getAccId).distinct().collect(Collectors.toList());
                // HashSet<String> dupSet = new HashSet<>();
                // for(String accId : accIdList){
                //     dupSet.add(accId);
                // }

                String strContent = "";
                String t = "";
                TargetMailVO tmp = new TargetMailVO();
                int cnt = 0;

                //조회일자 지난주 월 ~ 일 구하기
                Calendar cal = Calendar.getInstance();

                //무조건 7일 뺌
                cal.add(Calendar.DATE, -7);
                int nWeek = cal.get(Calendar.DAY_OF_WEEK);
                System.out.println(">>>>>>>>cal:"+nWeek);//지난주 요일
                cal.add(Calendar.DATE, 2-nWeek);//요일에서 월요일 되도록 뺌


                //지난주 월요일
                int nMonth  = cal.get(Calendar.MONTH)+1; String dayLastWeekFrom = cal.get(Calendar.YEAR) +"-"+ (nMonth<10?"0"+nMonth:nMonth+"") +"-"+ (cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE):cal.get(Calendar.DATE)+""); System.out.println(">>>>>>>dayLastWeekFrom:"+dayLastWeekFrom);
                //지난주 일요일
                cal.add(Calendar.DATE, 6);//월요일 부터 일요일까지의 날짜 더함
                nMonth  = cal.get(Calendar.MONTH)+1;
                String dayLastWeekTo = cal.get(Calendar.YEAR) +"-"+ (nMonth<10?"0"+nMonth:nMonth+"") +"-"+ (cal.get(Calendar.DATE)<10?"0"+cal.get(Calendar.DATE):cal.get(Calendar.DATE)+"");
                System.out.println(">>>>>>>dayLastWeekTo:"+dayLastWeekTo);

                String terms = "<p>안녕하세요!</p><p>기간 : "+dayLastWeekFrom + " ~ " + dayLastWeekTo + " 까지 등록된 건물관리자 목록입니다.</P><br>";

                String style = "<style type='text/css'> \n" +
                               ".tg  {border-collapse:collapse;border-spacing:0;}\n" +
                               ".tg td{border-color:black;border-style:solid;border-width:1px;font-family:Arial, sans-serif;font-size:14px;overflow:hidden;padding:10px 5px;word-break:normal;}\n"+
                               ".tg th{border-color:black;border-style:solid;border-width:1px;font-family:Arial, sans-serif;font-size:14px;font-weight:normal;overflow:hidden;padding:10px 5px;word-break:normal;}\n"+
                               ".tg .tg-to19{background-color:#67fd9a;border-color:inherit;color:#333333;font-weight:bold;text-align:left;vertical-align:top}\n"+
                               ".tg .tg-0pky{border-color:inherit;text-align:left;vertical-align:top}</style>";

                // Iterator<String> iter = dupSet.iterator();
                for(String tmpAccId : accIdList){
                    // 영업사원 메일 가져오기
                    List<String> accUserMail = new ArrayList<>();
                    accUserMail.add(tmpAccId);
                    //content 생성
                    strContent = style + terms + "<table  class='tg'><thead><tr><th class='tg-to19'>사용자 ID</th><th class='tg-to19'>사용자 명</th><th class='tg-to19'>연락처</th><th class='tg-to19'>유상계약번호</th><th class='tg-to19'>등록일자</th></tr></thead><tbody>";

                    for (int i = 0; i < _data.size(); i++) {
                        tmp = _data.get(i);
                        if (tmpAccId.equals(tmp.getAccId())) {
                            t = String.format("<tr><td class='tg-0pky'>"+"%s"+"</td><td class='tg-0pky'>"+"%s"+"</td><td class='tg-0pky'>"+"%s"+"</td><td class='tg-0pky'>"+"%s"+"</td><td class='tg-0pky'>"+"%s"+"</td></tr>\n", tmp.getMngId(),tmp.getMngNm(),tmp.getMngPhone(),tmp.getCompsCntrNo(),tmp.getRegDt());
                            strContent += t;
                        }
                    }
                    strContent +="</tbody></table>" +
                              "<p><a href='https://miri.hdel.co.kr/priv/user'>MIRI Service 고객포탈 바로가기</a></p><br><br><br>" +
                              "<p><span style='color: rgba(var(--v-theme-on-surface),var(--v-high-emphasis-opacity));'>감사합니다.</span></p>" +
                              "<p><span style='color: rgba(var(--v-theme-on-surface),var(--v-high-emphasis-opacity));'><strong>현대엘리베이터 드림</strong></span></p>";

                    log.error("====>Cnt=["+cnt+"]AccId ::::["+ tmpAccId+"] content ::: ["+strContent+"]");

                    MailVO mailVO = MailVO.builder()
                                .fromAddress("MIRIPortaladmin@hyundaielevator.com")
                                .subject("Miri Portal 건물관리자 등록 내역")
                                .toAddress(accUserMail)
                                .content(strContent).build();
                    mailService.send(mailVO, true);
                    cnt ++;
                }
            }
        }catch (Exception ex){
            System.out.println("에러" + ex);
        }
    }

    /*
     * 제목 : 영업사원 메일 전송 ( 1주일 간 등록 된 건물관리자 목록 ) NEW
     * 내용 : 등록된 건물 관리자의 PortFolio 거래선 정보를 이용하여 관련 영업사원에게  메일을 전송 함.
     * 주기 : 매주 화요일 20시에 수행 함.
     */
    public void sendMailAccount() {
        try{
            // 메일 전송 대상 조회(영업사원)
            List<ReqVO.TargetMailVO> _data = ccRepository.getSendMailAccount();

            if ( !_data.isEmpty() ){
                int j = 0;

                for(TargetMailVO tmpAccInfo : _data){
                    //영업사원 email, name
                    List<String> accUserMail = new ArrayList<>();
                    accUserMail.add(tmpAccInfo.getAccId());
                    String accNm = tmpAccInfo.getAccNm();

		            //메일 양식 가져오기
		            // String _msgTemplate = ccRepository.GetMsgTemplateContents("9997");
		            String _msgTemplate = tmpAccInfo.getMsgContent();
		            _msgTemplate = _msgTemplate.replace("#{고객명}",accNm );

                    log.error("AccNm ==>["+accUserMail.get(0)+"]["+accNm+"]--["+j+"]"+ accUserMail);

                    String subject = "[MIRI] "+ accNm + " 매니저님, 고객포털 내 지난 주 건물관리인 계정 생성 현황을 확인 바랍니다.";
                    MailVO mailVO = MailVO.builder()
                                    .fromAddress("MIRIPortaladmin@hyundaielevator.com")
                                    .subject(subject)
                                    .toAddress(accUserMail)
                                    .content(_msgTemplate).build();

                    mailService.send(mailVO, true);
                    j ++ ;
                }
            }
        }catch (Exception ex){
            System.out.println("에러" + ex);
        }
    }
    /*
     *  2023-10-13
     *  SCRM / SRM 에서 생성된 고객을 Miri-portal 사용자로 등록한다.
     *  매일 새벽 00 에 수행 함
     */
    public void registerManager() {
        try{
            //Miri 사용자 자동 등록요청 대상 가져오기 (SCRM )
            List<User.DefaultUserCreate> _scrmRegUser = scrmRepository.getAutoRegUserList();

            //SCRM 건물관리인 조회
            for (User.DefaultUserCreate reqScrm : _scrmRegUser){
                // 0.유상계약번호, 거래선 존재 확인
                //  1)존재하지 않은 경우는 Skip 처리
                // 1.Miri portal에 사용자가 존재하는 경우 처리(이메일, 사용자명으로)
                //  1) portfolio Lobby 에 거래선을 입력한다.
                //  2) SCRM/SRM 등록요청 건 update 처리 - 성공/오류 코드및 메세지
                // 2.Miri portal 에 사용자 존재하지 않는 경우
                //  1).사용자 등록 처리 - 관리 테이블 생성
                //  2).메일 발송 / MMS 발송(휴대전화번호 존재 시)
                //  3) SCRM/SRM 등록요청 건 update 처리 - 성공/오류 코드및 메세지

                /** EL_INFO 에서 유상계약 번호 또는 거래선이 존재할 경우만 사용자 등록 처리한다.*/
                List<User.ContractAPI> contractList = ccRepository.getContractInfo(reqScrm);

                //0.el_info에 유상계약번호의 거래선이 없는 경우는 Skip
                if(contractList.isEmpty() || contractList.size() < 1 ){
                    //로그 남기기(등록 오류: code:0)
                    reqScrm.setProcYn("N");
                    reqScrm.setProcResult("계약이 존재하지 않습니다.");
                    scrmRepository.updateAutoUserRegister(reqScrm);
                    continue;
                }

                //1.사용자 존재 확인 (이메일과 이름으로 )
                List<ReqVO.UserVO> _data = ccRepository.getUserExist(reqScrm);
                //사용자 존재 시
                if ( _data != null && !_data.isEmpty() ){
                   //portfolio 구성
                    if(ccRepository.insertExistPortfolio(reqScrm) > 0){
                        //요청 건 처리로그
                        reqScrm.setProcYn("Y");
                        reqScrm.setProcResult("처리되었습니다.");
                    }else{
                        reqScrm.setProcYn("N");
                        reqScrm.setProcResult("포트폴리오 구성 오류입니다.");
                    }
                    scrmRepository.updateAutoUserRegister(reqScrm);
                }else{
                    //이메일 유효성 체크 
                    if(!isValidEmail(reqScrm.getUserId())){
                        reqScrm.setProcYn("N");
                        reqScrm.setProcResult("이메일 형식 오류입니다.");
                        scrmRepository.updateAutoUserRegister(reqScrm);
                        continue;
                    }
                    //전화번호 유효성 체크
                    if(!isValidPhone(reqScrm.getPhonenumber())){
                        reqScrm.setProcYn("N");
                        reqScrm.setProcResult("전화번호 오류입니다.");
                        scrmRepository.updateAutoUserRegister(reqScrm);
                        continue;
                    }

                    //신규사용자
                    reqScrm.setOriginPassword(GetRandomPassword());
                    reqScrm.setPassword(passwordEncoder.encode(reqScrm.getOriginPassword()));
                    reqScrm.setRoleType("MANAGER");

                    //tb_user 등록
                    if( 0 < ccRepository.insertTypeManager(reqScrm)){
                        //정보등록
                        join(reqScrm);
                    }
                }
            }

            Thread.sleep(5000);

            //Miri 사용자 자동 등록요청 대상 가져오기 (SRM))
            List<User.DefaultUserCreate> _srmRegUser = srmRepository.getAutoRegUserList();
             //SRM 건물관리인 조회
            for (User.DefaultUserCreate reqSrm : _srmRegUser){

                /** EL_INFO 에서 유상계약 번호 또는 거래선이 존재할 경우만 사용자 등록 처리한다.*/
                List<User.ContractAPI> contractList = ccRepository.getContractInfo(reqSrm);

                //0.el_info에 유상계약번호의 거래선이 없는 경우는 Skip
                if(contractList.isEmpty() || contractList.size() < 1 ){
                    //로그 남기기(등록 오류: code:0)
                    reqSrm.setProcYn("N");
                    reqSrm.setProcResult("계약이 존재하지 않습니다.");
                    srmRepository.updateAutoUserRegister(reqSrm);
                    continue;
                }

                //1.사용자 존재 여부 확인
                List<ReqVO.UserVO> _data = ccRepository.getUserExist(reqSrm);
                //사용자 존재 시, 
                if ( _data != null && !_data.isEmpty() ){
                    //portfolio 구성
                    if(ccRepository.insertExistPortfolio(reqSrm) > 0){
                        //요청 건 처리로그
                        reqSrm.setProcYn("Y");
                        reqSrm.setProcResult("처리되었습니다.");
                    }else{
                        reqSrm.setProcYn("N");
                        reqSrm.setProcResult("포트폴리오 구성 오류입니다.");
                    }
                    srmRepository.updateAutoUserRegister(reqSrm);
                }else{
                    //이메일 유효성 체크 
                    if(!isValidEmail(reqSrm.getUserId())){
                        reqSrm.setProcYn("N");
                        reqSrm.setProcResult("이메일 형식 오류입니다.");
                        srmRepository.updateAutoUserRegister(reqSrm);
                        continue;
                    }
                    //전화번호 유효성 체크
                    if(!isValidPhone(reqSrm.getPhonenumber())){
                        reqSrm.setProcYn("N");
                        reqSrm.setProcResult("전화번호 오류입니다.");
                        srmRepository.updateAutoUserRegister(reqSrm);
                        continue;
                    }

                    //신규사용자
                    reqSrm.setOriginPassword(GetRandomPassword());
                    reqSrm.setPassword(passwordEncoder.encode(reqSrm.getOriginPassword()));
                    reqSrm.setRoleType("MANAGER");

                    //tb_user 등록
                    if( 0 < ccRepository.insertTypeManager(reqSrm)){
                        //정보등록
                        join(reqSrm);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("에러 :" + ex.getMessage());
        }
    }

    public void join(User.DefaultUserCreate request) throws Exception{
        /** 사용자 이메일 인증 키 발송 */
        String sendAcceptKey = Base64.encode(UUID.randomUUID().toString().getBytes()).concat(Base64.encode(UUID.randomUUID().toString().getBytes()));

        ccRepository.updateSendAcceptKey(User.UpdateAcceptKey.builder()
                        .userId(request.getUserId())
                        .sendAcceptKey(sendAcceptKey).build());

        /** 서비스 약관 동의 생성 */
        ccRepository.insertDefaultT(User.TermsDefaultCreate.builder()
                .userId(request.getUserId())
                .serviceUseAg(request.getTermsServiceUseAg())
                .plInfoUsingAg(request.getTermsPlInfoUsingAg())
                .plInfoStoreTimeAg(request.getTermsPlInfoStoreTimeAg())
                .adRecvAg(request.getTermsAdRecvAg()).build());

        /** 기본 사용자 설정 생성 */
        ccRepository.insertDefaultS(User.SetupDefaultCreate.builder().userId(request.getUserId()).build());

        /** 기본 포트폴리오 생성 */
        User.PortfolioDefaultCreate portfolio = User.PortfolioDefaultCreate.builder()
                                                        .userId(request.getUserId())
                                                        .defaultYn("y")
                                                        .portfolioName("lobby").build();

        ccRepository.insertDefaultP(portfolio);

        /** 포트폴리오 X 계약 정보 생성 */
        ccRepository.insertDefaultC(portfolio.getUserPortfolioMappingId(), request.getIntgPrjTrlineCdCode());

        // USER-ROLE Mapping 등록
        ccRepository.insertUserRoleMapping2(request);

        /** 알람 기본 정보 생성 */
        ccRepository.insertDefaultA(User.AlarmDefaultCreate.builder()
                .userId(request.getUserId())
                .build());

        // 메일 Format을 가져온다 .
        String _msgTemplate = ccRepository.GetMsgTemplateContents("9999");
        _msgTemplate = _msgTemplate.replace("#{고객명}", request.getUserName());
        _msgTemplate = _msgTemplate.replace("#{비밀번호}", "귀하의 임시 비밀번호는 " + request.getOriginPassword() + " 입니다.");
        _msgTemplate = _msgTemplate.replace("#{링크}", openURIConfig.BASE_URI.concat("/miri/v2/user/confirm-accept?key=")
                                                                          .concat(sendAcceptKey).concat("&user=").concat(request.getUserId()));

       mailService.send(MailVO.builder()
               .fromAddress("MIRIPortaladmin@hyundaielevator.com")
               .toAddress(Arrays.asList(request.getUserId()))
               .subject("[현대엘리베이터] 자동 가입 안내 메일")
               .content(_msgTemplate)
               .build(), true);

        //mms전송 휴대폰전화번호 있는 경우만 송신한다. (11 자리)
        if(!"".equals(request.getPhonenumber()) && request.getPhonenumber() != null &&
            request.getPhonenumber().replace("-","").length() == 11 &&
            "010".equals(request.getPhonenumber().replace("-","").substring(0,3))){
            //가입 안내 문자 전송
            _msgTemplate = ccRepository.GetMsgTemplateContents("9998");
            _msgTemplate = _msgTemplate.replace("#{고객명}", request.getUserName());
            _msgTemplate = _msgTemplate.replace("#{이메일}", request.getUserId());

            mmsService.sendMMS(mmsVO.request.builder()
                                .receiveNo(request.getPhonenumber())
                                .title("[현대엘리베이터] 고객포탈 가입 안내")
                                .contents(_msgTemplate).build());
        }
        //로그 남기기(등록 성공: code:1)
        request.setProcYn("Y");
        request.setProcResult("처리되었습니다.");

        if("SCRM".equals(request.getDbType())){
            scrmRepository.updateAutoUserRegister(request);
        }else{
            srmRepository.updateAutoUserRegister(request);
        }
    }

    /**
     * 랜덤 패스워드 생성
     */
    private String GetRandomPassword() {
        // alphaNum에 특수문자를 추가하여 커스텀이 가능하다.
        String alphaStr = "ABCDEFGHJKLMNOPQRSTUVWXYZabcdefghjkmnopqrstuvwxyz023456789@#$%^&*()";
        String specialStr = "@#$%^&*()";
        String numStr = "023456789";
        int alphaLength = alphaStr.length();
        int specialLength = specialStr.length();
        int numLength = numStr.length();

        Random random = new Random();

        StringBuffer code = new StringBuffer();
        for (int i = 0; i < 7; i++) {
            code.append(alphaStr.charAt(random.nextInt(alphaLength)));
        }

        code.append(specialStr.charAt(random.nextInt(specialLength)));
        code.append(numStr.charAt(random.nextInt(numLength)));

        return code.toString();
    }
    /**
    * Comment  : 정상적인 이메일 인지 검증.
    */
    public static boolean isValidEmail(String email) {
        boolean err = false;
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";   
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if(m.matches()) {
            err = true; 
        }
        return err;
   }
   /*
    * 핸드폰 번호 체크
    */
    public static boolean isValidPhone(String phoneNo) {
        boolean err = false;

        if(!"".equals(phoneNo) && phoneNo != null && phoneNo.replace("-","").length() == 11 &&
            "010".equals(phoneNo.replace("-","").substring(0,3))){
            err = true; 
        }
        return err;
   }
}
