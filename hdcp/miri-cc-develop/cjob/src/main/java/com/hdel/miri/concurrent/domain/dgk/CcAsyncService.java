package com.hdel.miri.concurrent.domain.dgk;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.collections4.ListUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.hdel.miri.concurrent.domain.dgk.vo.CcLogVO;
import com.hdel.miri.concurrent.domain.dgk.vo.ReqVO;
import com.hdel.miri.concurrent.domain.dgk.xmlschema.ElevatorInfo;
import com.hdel.miri.concurrent.domain.dgk.xmlschema.InspectHis;
import com.hdel.miri.concurrent.domain.dgk.xmlschema.InssuranceInfo;
import com.hdel.miri.concurrent.domain.dgk.xmlschema.SafetyMgrInfo;
import com.hdel.miri.concurrent.domain.dgk.xmlschema.SelfInspectHis;
import com.hdel.miri.concurrent.domain.scrm.SCRM;
import com.hdel.miri.concurrent.domain.scrm.SCRMRepository;
import com.hdel.miri.concurrent.domain.srm.SRMRepository;
import com.hdel.miri.concurrent.util.HevUtils;
import com.hdel.miri.concurrent.domain.hccc.HCCCRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

@Slf4j
@Service
@RequiredArgsConstructor
public class CcAsyncService {

    private final CcRepository ccRepository;
    private final SCRMRepository scrmRepository;
    private final SRMRepository srmRepository;
    private final HCCCRepository hcccRepository;

    /*
     * SCRM / SRM으로부터 전일 변경이 일어난 호기정보를 가져온다.
     * CT_EL_SYNC_TARGET
     */
    @Async("CcAsyncExcutor1")
    public void insertCcTargetElList(List<ReqVO.ElVO> target, String dbType) {
        List<ReqVO.ElVO> distinctedElevatorNoList = target.stream()
                .filter(HevUtils.distinctByKey(ReqVO.ElVO::getElevator_no)).collect(Collectors.toList());

        if (distinctedElevatorNoList != null && !distinctedElevatorNoList.isEmpty())
            ccRepository.deleteCcTargetElList(dbType);
        List<List<ReqVO.ElVO>> partitionedItems = ListUtils.partition(distinctedElevatorNoList, 1000);
        partitionedItems.forEach(item -> {
            ccRepository.insertCcTargetElList(item);
        });

    }

    /*
     * SCRM / SRM으로부터 Elevator 번호 없는 계약정보 가져온다.
     * CT_EL_SYNC_NO_TARGET
     */
    @Async("CcAsyncExcutor1")
    public void insertCcTargetNoElList(List<ReqVO.NoElVO> target, String dbType) {
        List<List<ReqVO.NoElVO>> partitionedItems = ListUtils.partition(target, 1000);
        partitionedItems.forEach(item -> {
            ccRepository.insertCcTargetNoElList(item);
        });

    }

    /*
     * SCRM / SRM으로부터 지사 / 영업소 정보를 가져온다
     * TB_ORG_MST
     */
    @Async("CcAsyncExcutor1")
    public void insertOrgMstInfoList(List<ReqVO.NoElVO> target) {
        List<List<ReqVO.NoElVO>> partitionedItems = ListUtils.partition(target, 1000);
        partitionedItems.forEach(item -> {
            ccRepository.insertOrgMstInfoList(item);
        });

    }

    /**
     * CT_EL_SYNC_TARGET 데이타를 국가데이타 센터와 SRM/SCRM으로부터 데이타를 가져와서 EL_INFO에 넣는다.
     * CT_EL_INFO
     */
    @Async("CcAsyncExcutor1")
    public void postElInfoFromDGK(String db_type, String elNo, String apiUrl, String params, String apiKey)
            throws Exception {
        StringBuffer requestBuffer = new StringBuffer();
        int lastLogIdx = 10;

        requestBuffer.append(apiUrl);
        requestBuffer.append("?ServiceKey=");
        requestBuffer.append(apiKey);
        requestBuffer.append(params);

        ElevatorInfo elInfo = null;
        int newId = 999;
        String buffer = "";

        try {
            URL url = new URL(requestBuffer.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/xml");

            JAXBContext jbc = JAXBContext.newInstance(ElevatorInfo.class);
            Unmarshaller ums = jbc.createUnmarshaller();

            // 엘베정보 가져오기 성공
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String inputLine;

            // 페이지의 정보를 저장한다.
            while ((inputLine = in.readLine()) != null) {
                buffer += inputLine.trim();
            }

            elInfo = (ElevatorInfo) ums.unmarshal(new StringReader(buffer));
            boolean isEmpty = elInfo.getBody().getItems().getItem() == null ? true : false;
log.error("isEmpty=====================>["+elNo+"]["+isEmpty+"]");
            if (!isEmpty) {
                if ("SCRM".equals(db_type)) {
                    // SCRM에서 프로젝터번호, 호기번호, 통합프로젝트번호, 거래선 정보를 가져온다.
                    List<SCRM.VO> _scrm_step2 = scrmRepository
                            .getInternalKeysFromSCRM(elInfo.getBody().getItems().getItem());
                    // 각 호기별 GIS정보 매핑
                    List<SCRM.VO> _scrm_step3 = _scrm_step2;
                    // 각 호기별 GIS정보 매핑
                    if (_scrm_step2 != null && _scrm_step2.size() > 0) {
                        _scrm_step3 = hcccRepository.getGisFromHccc(_scrm_step2);
                    }

                    if (_scrm_step3 != null && !_scrm_step3.isEmpty()) {
                        // DB에 저장
                        // 1000개씩 분할하여 실행
                        List<List<SCRM.VO>> partitionedItems = ListUtils.partition(_scrm_step3, 1000);
                        partitionedItems.forEach(item -> {
                            ccRepository.insertElInfo(item);
                        });
                    }
                } else {
                    // SRM에서 다시한번 매핑이 있는지 확인 만약 SCRM에서 내부키들이 Null이 아니면 SCRM정보 사용
                    List<SCRM.VO> _step2 = srmRepository.getInternalKeysFromSRM(elInfo.getBody().getItems().getItem());

                    List<SCRM.VO> _step3 = _step2;
                    // 각 호기별 GIS정보 매핑
                    if (_step2 != null && _step2.size() > 0) {
                        _step3 = hcccRepository.getGisFromHccc(_step2);
                    }

                    if (_step3 != null && !_step3.isEmpty()) {
                        // DB에 저장
                        // 1000개씩 분할하여 실행
                        List<List<SCRM.VO>> partitionedItems = ListUtils.partition(_step3, 1000);
                        partitionedItems.forEach(item -> {
                            ccRepository.insertElInfo(item);
                        });
                    }
                }
            }
        } catch (Exception ex) {
            CcLogVO.CcLogCreate idata = new CcLogVO.CcLogCreate();
            idata.setJobNm("건물별엘리베이터");
            idata.setInParams(requestBuffer.toString());
            idata.setElNo(elNo);
            idata.setErrorYn("Y");
            idata.setCreationUser(db_type);
            lastLogIdx = buffer.length() >= 2000 ? 1800 : buffer.length();
            buffer = buffer.substring(0, lastLogIdx);
            idata.setErrors(buffer);
            ccRepository.insertStartLog(idata);

            log.error("건물별엘리베이터 insert error", ex);
            throw ex;
        }
    }

    @Async("CcAsyncExcutor1")
    public void postInssuranceInfo(String elNo, String apiUrl, String params, String apiKey) throws Exception {
        StringBuffer requestBuffer = new StringBuffer();
        int lastLogIdx = 10;

        requestBuffer.append(apiUrl);
        requestBuffer.append("?ServiceKey=");
        requestBuffer.append(apiKey);
        requestBuffer.append(params);

        InssuranceInfo insInfo = null;
        int newId = 999;
        String buffer = "";

        try {
            URL url = new URL(requestBuffer.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "text/xml");

            JAXBContext jbc = JAXBContext.newInstance(InssuranceInfo.class);
            Unmarshaller ums = jbc.createUnmarshaller();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String inputLine;

            // 페이지의 정보를 저장한다.
            while ((inputLine = in.readLine()) != null) {
                buffer += inputLine.trim();
            }

            log.error("[보험정보 수신-1] {}", buffer);

            // 엘베정보 가져오기 성공
            // insInfo = (InssuranceInfo)ums.unmarshal(conn.getInputStream());
            insInfo = (InssuranceInfo) ums.unmarshal(new StringReader(buffer));

            // log.error("[보험정보 수신-2] {}", insInfo.toString());

            boolean isEmpty = insInfo.getBody().getItems().getItem() == null ? true : false;
            if (!isEmpty)
                ccRepository.insertInssuranceInfo(insInfo.getBody().getItems().getItem());
        } catch (Exception ex) {
            CcLogVO.CcLogCreate idata = new CcLogVO.CcLogCreate();
            idata.setJobNm("보험정보");
            idata.setInParams(requestBuffer.toString());
            idata.setElNo(elNo);
            idata.setErrorYn("Y");
            idata.setCreationUser("CONCURRENT USER");
            lastLogIdx = buffer.length() >= 2000 ? 1800 : buffer.length();
            buffer = buffer.substring(0, lastLogIdx);
            idata.setErrors(buffer);
            ccRepository.insertStartLog(idata);

            log.error("보험정보 insert error", ex);
            throw ex;
        }
    }

    @Async("CcAsyncExcutor1")
    public void postSafetyMgrInfo(String elNo, String apiUrl, String params, String apiKey) throws Exception {
        StringBuffer requestBuffer = new StringBuffer();
        int lastLogIdx = 10;

        requestBuffer.append(apiUrl);
        requestBuffer.append("?ServiceKey=");
        requestBuffer.append(apiKey);
        requestBuffer.append(params);

        SafetyMgrInfo smInfo = null;
        int newId = 999;
        String buffer = "";

        try {
            URL url = new URL(requestBuffer.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/xml");

            JAXBContext jbc = JAXBContext.newInstance(SafetyMgrInfo.class);
            Unmarshaller ums = jbc.createUnmarshaller();

            // 엘베정보 가져오기 성공
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String inputLine;

            // 페이지의 정보를 저장한다.
            while ((inputLine = in.readLine()) != null) {
                buffer += inputLine.trim();
            }

            smInfo = (SafetyMgrInfo) ums.unmarshal(new StringReader(buffer));
            boolean isEmpty = smInfo.getBody().getItems().getItem() == null ? true : false;
            if (!isEmpty)
                ccRepository.insertSafetyMgrInfo(smInfo.getBody().getItems().getItem());
        } catch (Exception ex) {
            CcLogVO.CcLogCreate idata = new CcLogVO.CcLogCreate();
            idata.setJobNm("안전관리자");
            idata.setInParams(requestBuffer.toString());
            idata.setElNo(elNo);
            idata.setErrorYn("Y");
            idata.setCreationUser("CONCURRENT USER");
            lastLogIdx = buffer.length() >= 2000 ? 1800 : buffer.length();
            buffer = buffer.substring(0, lastLogIdx);
            idata.setErrors(buffer);
            ccRepository.insertStartLog(idata);

            log.error("안전관리자 insert error", ex);
            throw ex;
        }
    }

    @Async("CcAsyncExcutor1")
    public void postSelfInspHisInfo(String yyyymm, String elNo, String apiUrl, String params, String apiKey)
            throws Exception {
        StringBuffer requestBuffer = new StringBuffer();
        int lastLogIdx = 10;

        requestBuffer.append(apiUrl);
        requestBuffer.append("?ServiceKey=");
        requestBuffer.append(apiKey);
        requestBuffer.append(params);

        SelfInspectHis siInfo = null;
        int newId = 999;
        String buffer = "";

        try {
            URL url = new URL(requestBuffer.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/xml");

            JAXBContext jbc = JAXBContext.newInstance(SelfInspectHis.class);
            Unmarshaller ums = jbc.createUnmarshaller();

            // log.info("el_no:{}, err:{}", elNo, params);

            // 엘베정보 가져오기 성공
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String inputLine;

            // 페이지의 정보를 저장한다.
            while ((inputLine = in.readLine()) != null) {
                buffer += inputLine.trim();
            }

            siInfo = (SelfInspectHis) ums.unmarshal(new StringReader(buffer));

            // log.info("el_no:{}, siInfo:{}", elNo, siInfo);
            boolean isEmpty = siInfo.getBody().getItems().getItem() == null ? true : false;
            // if(!isEmpty)
            // ccRepository.insertSelfInspHisInfo(siInfo.getBody().getItems().getItem());

            // 2023.06.04 변경, 기존에는 전체 데이타를 저장했으나 데이타량이 너무 많아서 향후 운영에 문제가 발생할것으로 판단
            // 첫번째 Row하나만 넣고 나머지는 직접 호출하는 것으로 변경한다.
            if (!isEmpty) {
                ccRepository.insertSelfInspHisInfo(yyyymm, siInfo.getBody().getItems().getItem().get(0));
            }
        } catch (Exception ex) {
            CcLogVO.CcLogCreate idata = new CcLogVO.CcLogCreate();
            idata.setJobNm("자체점검");
            idata.setInParams(requestBuffer.toString());
            idata.setElNo(elNo);
            idata.setErrorYn("Y");
            idata.setCreationUser("CONCURRENT USER");
            lastLogIdx = buffer.length() >= 2000 ? 1800 : buffer.length();
            buffer = buffer.substring(0, lastLogIdx);
            idata.setErrors(buffer);
            ccRepository.insertStartLog(idata);
            throw ex;
        }
    }

    @Async("CcAsyncExcutor1")
    public void fixSelfInspHisInfo(String fullUrl) throws Exception {
        StringBuffer requestBuffer = new StringBuffer();
        int lastLogIdx = 10;
        SelfInspectHis siInfo = null;
        int newId = 999;
        String buffer = "";

        String[] _parseStr = fullUrl.split("&");
        String _elNo = null;
        String _yyyyMM = null;

        // 20230817 gmw
        String originRequestBuffer = requestBuffer.toString();

        if (_parseStr != null && _parseStr.length == 5) {
            _elNo = _parseStr[3].replace("elevator_no=", "");
            _yyyyMM = _parseStr[4].replace("yyyymm=", "");

            try {
                URL url = new URL(fullUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/xml");

                JAXBContext jbc = JAXBContext.newInstance(SelfInspectHis.class);
                Unmarshaller ums = jbc.createUnmarshaller();

                // 엘베정보 가져오기 성공
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String inputLine;

                // 페이지의 정보를 저장한다.
                while ((inputLine = in.readLine()) != null) {
                    buffer += inputLine.trim();
                }

                siInfo = (SelfInspectHis) ums.unmarshal(new StringReader(buffer));
                boolean isEmpty = siInfo.getBody().getItems().getItem() == null ? true : false;

                // 2023.06.04 변경, 기존에는 전체 데이타를 저장했으나 데이타량이 너무 많아서 향후 운영에 문제가 발생할것으로 판단
                // 첫번째 Row하나만 넣고 나머지는 직접 호출하는 것으로 변경한다.
                if (!isEmpty) {
                    ccRepository.insertSelfInspHisInfo(_yyyyMM, siInfo.getBody().getItems().getItem().get(0));
                }
            } catch (Exception ex) {
                CcLogVO.CcLogCreate idata = new CcLogVO.CcLogCreate();
                idata.setJobNm("자체점검");

                // 20230617 gmw : inParm 이 사라진 경우에
                if (requestBuffer.toString() == null || "".equals(requestBuffer.toString())) {
                    idata.setInParams(originRequestBuffer);
                } else {
                    idata.setInParams(requestBuffer.toString());
                }

                idata.setElNo(_elNo);
                idata.setErrorYn("Y");
                idata.setCreationUser("CONCURRENT USER");
                lastLogIdx = buffer.length() >= 2000 ? 1800 : buffer.length();
                buffer = buffer.substring(0, lastLogIdx);
                idata.setErrors(buffer);
                ccRepository.insertStartLog(idata);
                throw ex;
            }
        }
    }

    @Async("CcAsyncExcutor1")
    public void postInspHisInfo(String elNo, String apiUrl, String params, String apiKey) throws Exception {
        StringBuffer requestBuffer = new StringBuffer();
        int lastLogIdx = 10;

        requestBuffer.append(apiUrl);
        requestBuffer.append("?ServiceKey=");
        requestBuffer.append(apiKey);
        requestBuffer.append(params);

        InspectHis inspInfo = null;
        int newId = 999;
        String buffer = "";

        try {
            URL url = new URL(requestBuffer.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/xml");

            JAXBContext jbc = JAXBContext.newInstance(InspectHis.class); 
            Unmarshaller ums = jbc.createUnmarshaller();

            // 엘베정보 가져오기 성공
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String inputLine;

            // 페이지의 정보를 저장한다.
            while ((inputLine = in.readLine()) != null) {
                buffer += inputLine.trim();
            }
            inspInfo = (InspectHis) ums.unmarshal(new StringReader(buffer));
            boolean isEmpty = inspInfo.getBody().getItems().getItem() == null ? true : false;
            if (!isEmpty)
                ccRepository.insertInspHisInfo(inspInfo.getBody().getItems().getItem());
        } catch (Exception ex) {
            CcLogVO.CcLogCreate idata = new CcLogVO.CcLogCreate();
            idata.setJobNm("정기검사");
            idata.setInParams(requestBuffer.toString());
            idata.setElNo(elNo);
            idata.setErrorYn("Y");
            idata.setCreationUser("CONCURRENT USER");
            lastLogIdx = buffer.length() >= 2000 ? 1800 : buffer.length();
            buffer = buffer.substring(0, lastLogIdx);
            idata.setErrors(buffer);
            ccRepository.insertStartLog(idata);
            //수작업 시 로그 남김
            idata.setInParams(params);
            ccRepository.insertStartLogInspInfo(idata);
            throw ex;
        }
    }

    @Async("CcAsyncExcutor1")
    public void fixInspHisInfo(String fullUrl) throws Exception {
        StringBuffer requestBuffer = new StringBuffer();
        int lastLogIdx = 10;

        InspectHis inspInfo = null;
        int newId = 999;
        String buffer = "";

        String[] _parseStr = fullUrl.split("&");
        String _elNo = null;
        String _yyyyMM = null;

        if (_parseStr != null && _parseStr.length == 5) {
            _elNo = _parseStr[3].replace("elevator_no=", "");

            try {
                URL url = new URL(fullUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/xml");

                JAXBContext jbc = JAXBContext.newInstance(InspectHis.class);
                Unmarshaller ums = jbc.createUnmarshaller();

                // 엘베정보 가져오기 성공
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String inputLine;

                // 페이지의 정보를 저장한다.
                while ((inputLine = in.readLine()) != null) {
                    buffer += inputLine.trim();
                }
                inspInfo = (InspectHis) ums.unmarshal(new StringReader(buffer));
                boolean isEmpty = inspInfo.getBody().getItems().getItem() == null ? true : false;
                if (!isEmpty)
                    ccRepository.insertInspHisInfo(inspInfo.getBody().getItems().getItem());
            } catch (Exception ex) {
                CcLogVO.CcLogCreate idata = new CcLogVO.CcLogCreate();
                idata.setJobNm("정기검사");
                idata.setInParams(requestBuffer.toString());
                idata.setElNo(_elNo);
                idata.setErrorYn("Y");
                idata.setCreationUser("CONCURRENT USER");
                lastLogIdx = buffer.length() >= 2000 ? 1800 : buffer.length();
                buffer = buffer.substring(0, lastLogIdx);
                idata.setErrors(buffer);
                ccRepository.insertStartLog(idata);

                throw ex;
            }
        }
    }
}
