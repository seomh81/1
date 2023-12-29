package com.hdel.miri.concurrent.domain.message;

import com.hdel.miri.concurrent.domain.dgk.vo.CcLogVO;
import com.hdel.miri.concurrent.domain.dgk.vo.ReqVO;
import com.hdel.miri.concurrent.domain.dgk.vo.RespVO;
import com.hdel.miri.concurrent.domain.dgk.vo.RespVO.ConcurrentLogVO;
import com.hdel.miri.concurrent.domain.message.CcMessageVO.*;
import com.hdel.miri.concurrent.domain.mail.MailService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hdel.miri.concurrent.domain.sap.SAPAsyncService;
import com.hdel.miri.concurrent.domain.sap.SAPScrmService;
import com.hdel.miri.concurrent.domain.sap.SAPSrmService;
import com.hdel.miri.concurrent.domain.scrm.SCRMRepository;
import com.hdel.miri.concurrent.util.response.ResponseBody;
import com.hdel.miri.concurrent.util.response.ResponseFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdel.miri.concurrent.domain.dgk.CcAsyncService;
import com.hdel.miri.concurrent.domain.dgk.CcRepository;
import com.hdel.miri.concurrent.domain.dgk.vo.CcElevatorVO;

@Slf4j
@Service
@RequiredArgsConstructor
public class CcMessageService {
    private final CcRepository      ccRepository;
    private final SCRMRepository    scrmRepository;
    private final MailService       mailService;

    @Value("${spring.api.firebaseUrl}")
    private String firebaseUrl;

    @Value("${spring.api.firebaseKey}")
    private String firebaseKey;

    @Value("${spring.api.kakaoYelloKey}")
    private String kakaoYelloKey;

    private int AlarmLogging(String _method, String _type, String _event, String _contents, String _receiverId, String _receiverPhoneNo) {
        try {
            return ccRepository.insertAlarmLog(_method, _type, _event, _contents, _receiverId, _receiverPhoneNo);
        } catch(Exception ex) {
            System.out.println(ex);            
            return 0;
        }
    }


    private int SendEMAIL(GetSubscriberListVO _data, String contents) {
        try {
            List<String> _toAddress = new ArrayList();
            _toAddress.add(_data.getUserId());

            MailVO vo = MailVO.builder().toAddress(_toAddress)
                                        .fromAddress("MIRIPortaladmin@hyundaielevator.com")
                                        .subject("[현대엘리베이터-미리포탈]")
                                        .content(contents).build();
            mailService.send(vo, false);
            return 1;
        } catch(Exception ex) {
            return 0;
        }
    }

    private int SendKAKAO(GetSubscriberListVO _data, String tmplCd, String contents) {
        try {
            CcKakaoVO vo = CcKakaoVO.builder().yellowidKey(kakaoYelloKey)
                                            .tmplCd(tmplCd)
                                            .msg(contents)
                                            .lmsSubject("현대엘리베이터 - 고장접수 확인")
                                            .phone(_data.getPhonenumber()).build();
            scrmRepository.SendKAKAO(vo);    
            return 1;
        } catch(Exception ex) {
            return 0;
        }
    }


    private int SendMMS(GetSubscriberListVO _data, String contents) {
        try {
            CcMMSVO vo = CcMMSVO.builder().receiverPhoneNo(_data.getPhonenumber())
                            .title("[현대엘리베이터-미리포탈]")
                            .contents(contents).build();
            scrmRepository.SendMMS(vo);
            return 1;
        } catch(Exception ex) {
            return 0;
        }
    }


    private int SendAPPPUSH(GetSubscriberListVO _data, String contents, String alarmType) {
        try {
                String tabsValue= "";

                if(alarmType.equals("portal_fail_recv") || alarmType.equals("portal_fail_recv") || alarmType.equals("portal_fail_complete"))
                {
                    tabsValue = "tabs/service";
                }

                Map<String, String> notification = new HashMap<>();
                notification.put("title", "[현대엘리베이터-미리포탈]");
                notification.put("body", contents);
                notification.put("tabs", tabsValue);

                Map<String, String> data = new HashMap<>();
                data.put("title", "[현대엘리베이터-미리포탈]");
                data.put("body", contents);
                data.put("tabs", tabsValue);

                Map<String, String> priv = new HashMap<>();
                priv.put("priority", "high");

                Map<String,Object> params = new LinkedHashMap<>(); // 파라미터 세팅
                params.put("to", _data.getFirebaseUserId());
                params.put("notification", notification);    
                params.put("data", data);  
                params.put("android", priv);
                params.put("priority", "high");

                JSONObject reqParams = new JSONObject(params);
    
                ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
                RestTemplate restTemplate = new RestTemplateBuilder()
                        .requestFactory(() -> requestFactory)
                        .build();
                    
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Authorization", "key="+firebaseKey);
    
                String requestBody = reqParams.toString();
                    
                ResponseEntity<String> response = restTemplate.exchange(firebaseUrl, HttpMethod.POST, new HttpEntity<>(requestBody, headers), String.class);
                
                int _stat = response.getStatusCode().value();
                if (_stat > 299) {
                    System.out.println(_stat + " Error!");
                    throw new RuntimeException("App push Error!");
                } else {
                    JSONObject jo = new JSONObject(response.getBody());
                }
            return 1;
        } catch(Exception ex) {
            return 0;
        }
    }

    private Map<String, Map> SendMessage(CcMessageVO.CcSourceVO req, String kakaoTempCd, String _msgContents, String _msgContentsShot) {
        List<GetSubscriberListVO> _target = ccRepository.GetSubscriberForAlarm(req);
        Map<String, Map> rtnValue = new HashMap<>();
        Map<String, Integer> rtnMap = new HashMap<>();

        if(_target != null && !_target.isEmpty()) {

            Map<String, GetSubscriberListVO> _dbInsertTarget = new HashMap<>();

            for(GetSubscriberListVO _subscriberVO: _target) {
                _dbInsertTarget.put(_subscriberVO.getUserId(), _subscriberVO);
                switch(_subscriberVO.getAlarmType()) {
                    case "01":
                        rtnMap.put("EMAIL RESULT",SendEMAIL(_subscriberVO, _msgContents));
                        break;
                    case "02":
                        if(_subscriberVO.getPhonenumber() != null && !"".equals(_subscriberVO.getPhonenumber()) && !"notice".equals(kakaoTempCd)) {
                            rtnMap.put("KAKAO RESULT", SendKAKAO(_subscriberVO, kakaoTempCd, _msgContentsShot));
                        } else {
                            rtnMap.put("KAKAO RESULT", 0);
                        }
                        break;
                    case "03":
                        if(_subscriberVO.getPhonenumber() != null && !"".equals(_subscriberVO.getPhonenumber())) {
                            rtnMap.put("MMS RESULT",SendMMS(_subscriberVO, _msgContentsShot));
                        } else {
                            rtnMap.put("MMS RESULT", 0);
                        }

                        break;
                    case "04":
                        if(_subscriberVO.getFirebaseUserId() != null && !"".equals(_subscriberVO.getFirebaseUserId()) && _subscriberVO.getFirebaseUserId().length() > 100) {
                            rtnMap.put("APP PUSH RESULT", SendAPPPUSH(_subscriberVO, _msgContentsShot, kakaoTempCd));
                        } else {
                            rtnMap.put("APP PUSH RESULT", 0);
                        }
                        break;
                }
                rtnValue.put(_subscriberVO.getUserId(), rtnMap);
            }

            GetSubscriberListVO temp;
            Map<String, Integer> tempMap;
            for(String userId: _dbInsertTarget.keySet()) {
                temp = _dbInsertTarget.get(userId);
                tempMap = rtnValue.get(userId);
                if(tempMap.get("EMAIL RESULT")!=null && tempMap.get("EMAIL RESULT") == 1) AlarmLogging("email", temp.getDiv().substring(0,2), temp.getDiv(), _msgContents, temp.getUserId() , temp.getPhonenumber());
                if(tempMap.get("KAKAO RESULT")!=null && tempMap.get("KAKAO RESULT") == 1) AlarmLogging("kakao", temp.getDiv().substring(0,2), temp.getDiv(), _msgContentsShot, temp.getUserId() , temp.getPhonenumber());
                if(tempMap.get("MMS RESULT")!=null && tempMap.get("MMS RESULT") == 1) AlarmLogging("mms", temp.getDiv().substring(0,2), temp.getDiv(), _msgContentsShot, temp.getUserId() , temp.getPhonenumber());
                if(tempMap.get("APP PUSH RESULT")!=null && tempMap.get("APP PUSH RESULT") == 1) AlarmLogging("app push", temp.getDiv().substring(0,2), temp.getDiv(), _msgContentsShot, temp.getUserId() , temp.getPhonenumber());
            }

        }
        return rtnValue;
    }


    private String IsAlarmYn() {
        return ccRepository.GetAlarmYn(); 
    }

    /*
     * 제목 : 고장 - 발생/접수시 메세지 전송
     * 방식 : MMS - DB등록, Mail - Mailsender, AppPush - google firebase api 호출
     */
    public ResponseEntity SendFaultReport(CcMessageVO.CcSourceVO req) {
        //단문 contents
        req.setDivType("1011");
        MsgTemplateVO _templateShot = ccRepository.GetMsgTemplate(req.getDivType());
        String _msgContentsShot = _templateShot.getMsgTemplateContents().replace("#{접수번호}", req.getReqNo());
        _msgContentsShot = _msgContentsShot.replace("#{현장명}", req.getSiteNm());
        _msgContentsShot = _msgContentsShot.replace("#{호기명}", req.getPrjHoNo());
        _msgContentsShot = _msgContentsShot.replace("#{접수유형}", req.getReqType());
        //장문 contents
        req.setDivType("1001");
        MsgTemplateVO _template = ccRepository.GetMsgTemplate(req.getDivType());
        String _msgContents = _template.getMsgTemplateContents().replace("#{접수번호}", req.getReqNo());
        _msgContents = _msgContents.replace("#{현장명}", req.getSiteNm());
        _msgContents = _msgContents.replace("#{호기명}", req.getPrjHoNo());
        _msgContents = _msgContents.replace("#{접수유형}", req.getReqType());
        Map<String, Map> resp = SendMessage(req, "portal_fail_recv", _msgContents, _msgContentsShot);
        ResponseBody body = ResponseBody.builder().data(resp).build();

        return ResponseFactory.create(HttpStatus.OK, 200, "메세지를 송신하였습니다.",body);
    }

    /*
     * 제목 : 고장 - 배치완료 메세지 전송
     * 방식 : MMS - DB등록, Mail - Mailsender, AppPush - google firebase api 호출
     */
    public ResponseEntity SendFaultServiceReady(CcMessageVO.CcSourceVO req) {
        //단문 contents
        req.setDivType("1012");
        MsgTemplateVO _templateShot = ccRepository.GetMsgTemplate(req.getDivType());
        String _msgContentsShot = _templateShot.getMsgTemplateContents().replace("#{접수번호}", req.getReqNo());
        _msgContentsShot = _msgContentsShot.replace("#{현장명}", req.getSiteNm());
        _msgContentsShot = _msgContentsShot.replace("#{호기명}", req.getPrjHoNo());
        _msgContentsShot = _msgContentsShot.replace("#{접수유형}", req.getReqType());
        _msgContentsShot = _msgContentsShot.replace("#{담당기사}", req.getAssignedEngineerNm());
        //장문 contents
        req.setDivType("1002");
        MsgTemplateVO _template = ccRepository.GetMsgTemplate(req.getDivType());
        String _msgContents = _template.getMsgTemplateContents().replace("#{접수번호}", req.getReqNo());
        _msgContents = _msgContents.replace("#{현장명}", req.getSiteNm());
        _msgContents = _msgContents.replace("#{호기명}", req.getPrjHoNo());
        _msgContents = _msgContents.replace("#{접수유형}", req.getReqType());
        _msgContents = _msgContents.replace("#{담당기사}", req.getAssignedEngineerNm());
        
        Map<String, Map> resp = SendMessage(req, "portal_fail_reserve", _msgContents, _msgContentsShot);
        ResponseBody body = ResponseBody.builder().data(resp).build();
        return ResponseFactory.create(HttpStatus.OK, 200, "메세지를 송신하였습니다.",body);
    }

    /*
     * 제목 : 고장 - 처리완료시 메세지 전송
     * 방식 : MMS - DB등록, Mail - Mailsender, AppPush - google firebase api 호출
     */
    public ResponseEntity SendFaultComplete(CcMessageVO.CcSourceVO req) {
        //단문 contents
        req.setDivType("1013");
        MsgTemplateVO _templateShot = ccRepository.GetMsgTemplate(req.getDivType());
        String _msgContentsShot = _templateShot.getMsgTemplateContents().replace("#{접수번호}", req.getReqNo());
        _msgContentsShot = _msgContentsShot.replace("#{현장명}", req.getSiteNm());
        _msgContentsShot = _msgContentsShot.replace("#{호기명}", req.getPrjHoNo());
        _msgContentsShot = _msgContentsShot.replace("#{접수유형}", req.getReqType());
        //장문 contents
        req.setDivType("1003");
        MsgTemplateVO _template = ccRepository.GetMsgTemplate(req.getDivType());
        String _msgContents = _template.getMsgTemplateContents().replace("#{접수번호}", req.getReqNo());
        _msgContents = _msgContents.replace("#{현장명}", req.getSiteNm());
        _msgContents = _msgContents.replace("#{호기명}", req.getPrjHoNo());
        _msgContents = _msgContents.replace("#{접수유형}", req.getReqType());

        Map<String, Map> resp = SendMessage(req, "portal_fail_complete", _msgContents, _msgContentsShot);
        ResponseBody body = ResponseBody.builder().data(resp).build();
        return ResponseFactory.create(HttpStatus.OK, 200, "메세지를 송신하였습니다.",body);
    }

    /*
     * 제목 : 미수금
     * 방식 : MMS - DB등록, Mail - Mailsender, AppPush - google firebase api 호출
     */
    public ResponseEntity SendUnBilledInfo(CcMessageVO.CcSourceVO req) {
        if("y".equals(IsAlarmYn())) {
            MsgTemplateVO _template = ccRepository.GetMsgTemplate("3002");
            System.out.println("Test");
        } else {

        }
        return null;
    }

    /*
     * 제목 : 계약만료 15일전
     * 방식 : MMS - DB등록, Mail - Mailsender, AppPush - google firebase api 호출
     */
    public ResponseEntity SendContractExpirationInfo(CcMessageVO.CcSourceVO req) {
        Map<String, Map> rtnVal = new HashMap<>();
        List<String> _privList = new ArrayList();
        _privList.add("MANAGER");
        req.setPrivList(_privList);

        if("y".equals(IsAlarmYn())) {
            MsgTemplateVO _template     = ccRepository.GetMsgTemplate("3001"); //장문
            MsgTemplateVO _templateShot = ccRepository.GetMsgTemplate("3011"); //단문
            // 계약만료 15일전 통합프로젝트 + 거래선 단위로 가져오기
            List<CcSourceVO> _expiredContractList = null;
            if(req.getIntgPrjNo() != null && !"".equals(req.getIntgPrjNo())) {
                _expiredContractList = new ArrayList();
                _expiredContractList.add(CcSourceVO.builder().intgPrjNo(req.getIntgPrjNo())
                                                    .trlineCd(req.getTrlineCd())
                                                    .divType("3001")
                                                    .siteNm("계약만료 15일전 현장")
                                                    .customerNm("나만료")
                                                    .contractEndDt("20230601")
                                                    .branchNm("종로지사")
                                                    .branchTelNo("1577-0603")
                                                    .build());
            } else {
                _expiredContractList = ccRepository.GetExpContractSourceList();
            }
            
            if(_expiredContractList != null && !_expiredContractList.isEmpty()) {
                for(CcSourceVO _sourceVO: _expiredContractList) {
                    String _msgContents = _template.getMsgTemplateContents().replace("#{현장명}", _sourceVO.getSiteNm());
                    _msgContents = _msgContents.replace("#{고객명}", _sourceVO.getCustomerNm());
                    _msgContents = _msgContents.replace("#{계약만료일}", _sourceVO.getContractEndDt());
                    _msgContents = _msgContents.replace("#{지사명}", _sourceVO.getBranchNm());
                    _msgContents = _msgContents.replace("#{지사전번}", _sourceVO.getBranchTelNo());

                    String _msgContentsShot = _templateShot.getMsgTemplateContents().replace("#{현장명}", _sourceVO.getSiteNm());
                    _msgContentsShot = _msgContentsShot.replace("#{고객명}", _sourceVO.getCustomerNm());
                    _msgContentsShot = _msgContentsShot.replace("#{계약만료일}", _sourceVO.getContractEndDt());
                    _msgContentsShot = _msgContentsShot.replace("#{지사명}", _sourceVO.getBranchNm());
                    _msgContentsShot = _msgContentsShot.replace("#{지사전번}", _sourceVO.getBranchTelNo());

                    req.setDivType(_sourceVO.getDivType());
                    req.setIntgPrjNo(_sourceVO.getIntgPrjNo());
                    req.setTrlineCd(_sourceVO.getTrlineCd());
                    Map<String, Map> resp = SendMessage(req, "portal_contract_expiration", _msgContents, _msgContentsShot);
                    rtnVal.put(req.getIntgPrjNo()+req.getTrlineCd(), resp);
                }
            } else {
                return ResponseFactory.create(HttpStatus.OK, 200, "송신 대상이 없습니다.",null);
            }

            System.out.println("kkkk");
        } else {
            return ResponseFactory.create(HttpStatus.OK, 200, "송신 대상이 없습니다.", null);
        }
        ResponseBody body = ResponseBody.builder().data(rtnVal).build();
        return ResponseFactory.create(HttpStatus.OK, 200, "전송하였습니다.", body);
    }

    /*
     * 제목 : 공지사항 - 내계약그룹
     * 방식 : MMS - DB등록, Mail - Mailsender, AppPush - google firebase api 호출
     */
    public ResponseEntity SendNotice(CcMessageVO.CcSourceVO req) {
        Map<String, Map> rtnVal = new HashMap<>();
        List<String> _privList = new ArrayList();
        _privList.add("MANAGER");
        _privList.add("USER");
        req.setPrivList(_privList);

        if("y".equals(IsAlarmYn())) {
            MsgTemplateVO _template     = ccRepository.GetMsgTemplate("4001");
            MsgTemplateVO _templateShot = ccRepository.GetMsgTemplate("4002");
            // 어제 등록된 공지사항을 통합프로젝트 + 거래선 단위로 가져오기
            List<CcSourceVO> _noticeList = null;
            if(req.getIntgPrjNo() != null && !"".equals(req.getIntgPrjNo())) {
                _noticeList = new ArrayList();
                _noticeList.add(CcSourceVO.builder().intgPrjNo(req.getIntgPrjNo())
                                                    .trlineCd(req.getTrlineCd())
                                                    .divType("4001")
                                                    .title("Swagger Test")
                                                    .build());
            } else {
                _noticeList = ccRepository.GetNoticeSourceList();
            }
            
            if(_noticeList != null && !_noticeList.isEmpty()) {
                for(CcSourceVO _sourceVO: _noticeList) {
                    String _msgContents     = _template.getMsgTemplateContents().replace("#{제목}", _sourceVO.getTitle());
                    String _msgContentsShot = _templateShot.getMsgTemplateContents().replace("#{제목}", _sourceVO.getTitle());
                    req.setDivType(_sourceVO.getDivType());
                    req.setIntgPrjNo(_sourceVO.getIntgPrjNo());
                    req.setTrlineCd(_sourceVO.getTrlineCd());
                    Map<String, Map> resp = SendMessage(req, "notice", _msgContents, _msgContentsShot);
                    rtnVal.put(req.getIntgPrjNo()+req.getTrlineCd(), resp);
                }
            } else {
                return ResponseFactory.create(HttpStatus.OK, 200, "송신 대상이 없습니다.",null);
            }

            System.out.println("kkkk");
        } else {
            return ResponseFactory.create(HttpStatus.OK, 200, "송신 대상이 없습니다.", null);
        }
        ResponseBody body = ResponseBody.builder().data(rtnVal).build();
        return ResponseFactory.create(HttpStatus.OK, 200, "전송하였습니다.", body);
    }

    /*
     * 제목 : 정기점검 - 완료시
     * 방식 : MMS - DB등록, Mail - Mailsender, AppPush - google firebase api 호출
     */
    public ResponseEntity SendSelfInspComplete(CcMessageVO.CcSourceVO req) {
        Map<String, Map> rtnVal = new HashMap<>();
        List<String> _privList = new ArrayList();
        _privList.add("MANAGER");
        req.setPrivList(_privList);
        if("y".equals(IsAlarmYn())) {
            MsgTemplateVO _template     = ccRepository.GetMsgTemplate("2001");
            MsgTemplateVO _templateShot = ccRepository.GetMsgTemplate("2002");
            // 지난주 정기점검 완료된 통합프로젝트 + 거래선 단위로 가져오기
            List<CcSourceVO> _expiredContractList = null;
            if(req.getIntgPrjNo() != null && !"".equals(req.getIntgPrjNo())) {
                _expiredContractList = new ArrayList();
                _expiredContractList.add(CcSourceVO.builder().intgPrjNo(req.getIntgPrjNo())
                                                    .trlineCd(req.getTrlineCd())
                                                    .divType("2001")
                                                    .build());
            } else {
                _expiredContractList = ccRepository.GetSelfInspCompleteSourceList();
            }
            
            if(_expiredContractList != null && !_expiredContractList.isEmpty()) {
                for(CcSourceVO _sourceVO: _expiredContractList) {
                    String _msgContents     = _template.getMsgTemplateContents();
                    String _msgContentsShot = _templateShot.getMsgTemplateContents();
                    req.setDivType(_sourceVO.getDivType());
                    req.setIntgPrjNo(_sourceVO.getIntgPrjNo());
                    req.setTrlineCd(_sourceVO.getTrlineCd());
                    Map<String, Map> resp = SendMessage(req, "portal_selfinsp", _msgContents, _msgContentsShot);
                    rtnVal.put(req.getIntgPrjNo()+req.getTrlineCd(), resp);
                }
            } else {
                return ResponseFactory.create(HttpStatus.OK, 200, "송신 대상이 없습니다.",null);
            }
        } else {
            return ResponseFactory.create(HttpStatus.OK, 200, "송신 대상이 없습니다.", null);
        }
        ResponseBody body = ResponseBody.builder().data(rtnVal).build();
        return ResponseFactory.create(HttpStatus.OK, 200, "전송하였습니다.", body);
    }
}