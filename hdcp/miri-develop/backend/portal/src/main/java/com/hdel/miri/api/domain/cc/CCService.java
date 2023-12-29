package com.hdel.miri.api.domain.cc;

import com.hdel.miri.api.domain.cc.CC.CCCurrentPortfolioRequest;
import com.hdel.miri.api.domain.cc.CC.CCElevatorInfoVO;
import com.hdel.miri.api.domain.cc.CC.CCServiceVO;
import com.hdel.miri.api.domain.cc.CC.CalenderVO;
import com.hdel.miri.api.domain.cc.CC.HCCCServiceREQ;
import com.hdel.miri.api.domain.cc.CC.SelfInspectHistoryVO;
import com.hdel.miri.api.domain.cc.CC.ServiceReqInfoVO;
import com.hdel.miri.api.domain.cc.xml.InspectFail;
import com.hdel.miri.api.domain.cc.xml.SelfInspectHis;
import com.hdel.miri.api.domain.cc.xml.SelfInspectHis.Item;
import com.hdel.miri.api.domain.hccc.HCCCRepository;
import com.hdel.miri.api.domain.hccc.HCCCService;
import com.hdel.miri.api.domain.hrts.HRTSRepository;
import com.hdel.miri.api.domain.mail.MailService;
import com.hdel.miri.api.domain.mail.MailVO;
import com.hdel.miri.api.domain.masterdata.MasterData;
import com.hdel.miri.api.domain.masterdata.MasterDataRepository;
import com.hdel.miri.api.domain.masterdata.MasterDataService;
import com.hdel.miri.api.domain.masterdata.MasterData.MasterDataDetailVO;
import com.hdel.miri.api.util.response.Message;
import com.hdel.miri.api.util.response.ResponseBody;
import com.hdel.miri.api.util.response.ResponseTemplate;
import com.hdel.miri.api.util.response.ResultCode;
import org.json.JSONObject;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class CCService {
    @Value("${api.service-key}")
    private String apiKey;
    @Value("${api.inspect-key}")
    private String inspectFailApi;
    @Value("${api.self-inspect-key}")
    private String selfInspectApi;
    @Value("${api.hccc-service-req}")
    private String hcccServiceReqApi;


    private final Message message;
    private final ResponseTemplate responseTemplate;
    private final CCRepository ccRepository;
    private final HCCCRepository hcccRepository;
    private final HCCCService hcccService;
    private final HRTSRepository hrtsRepository;
    private final MailService mailService;
    private final MasterDataRepository masterDataRepository;


    public ResponseEntity getIntegratedInspectionDetailMasterInfo(CC.ElvSearch request){
        try {
            CC.IntegratedMasterInfoVO result = ccRepository.selectIntegratedMasterInfo(request);
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(result)
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            log.error(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }
    public ResponseEntity getIntegratedInspectionList(CC.CCCurrentPortfolioRequestExtSearch request){
        request.setProjnos(ccRepository.selectProjnosByPortfolio(request.getUserPortfolioMappingId().toString()));

        try {
            List<CC.IntegratedInspectVO> resultList = ccRepository.selectIntegratedInspList(request);
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(resultList)
                    .build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity getInspCountTotal(CC.CCCurrentPortfolioRequest request){
        try {
            CC.InspectTotalVO inspectVO = new CC.InspectTotalVO();
            Long itotal = ccRepository.selectElevatorTotalCount(request);
            Long success = ccRepository.selectElevatorSuccessCount(request);
            Long fail = ccRepository.selectElevatorFailCount(request);
            Long not = itotal - (success+fail);
            inspectVO.setTotal(itotal);
            inspectVO.setSuccess(success);
            inspectVO.setFail(fail);
            inspectVO.setNot(not);

            CC.CompMisuTotalVO contractVO = new CC.CompMisuTotalVO();
            Long ctotal = ccRepository.selectCompTotalCount(request);
            Long misu = ccRepository.selectMisuTotalCount(request);
            contractVO.setTotal(ctotal);
            contractVO.setMisu(misu);
            contractVO.setSuccess(ctotal-misu);

            CC.ELCountStatusVO resultVO = new CC.ELCountStatusVO();
            resultVO.setInspect(inspectVO);
            resultVO.setContract(contractVO);
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(resultVO)
                    .build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity getSelfInspectManage(CC.InspectSearchByPortfolio request){
        try {
            List<CC.SelfInspectHeadVO> selfInspect = ccRepository.selectSelfInsepectManage(request);
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(selfInspect)
                    .build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    // 정기점검 상세조회
    public ResponseEntity getSelfInspectManageDetails(CC.InspectSearchBySelfInspectHead request){
        try {
            
            // List<CC.SelfInspectHistoryVO> details = ccRepository.selectSelfInsepectHeadHistory(request);
            StringBuffer    requestBuffer   = new StringBuffer();
            int lastLogIdx = 10;
    
            StringBuffer paramBuffer = new StringBuffer();
            paramBuffer.delete(0, paramBuffer.length());
            paramBuffer.append("&page_no=1");
            paramBuffer.append("&numOfRows=200");
            paramBuffer.append("&elevator_no="+request.getElevatorNo());
            paramBuffer.append("&yyyymm="+request.getSelChkBeginDate());

            requestBuffer.append(selfInspectApi);
            requestBuffer.append("?ServiceKey=");
            requestBuffer.append(apiKey);
            requestBuffer.append(paramBuffer.toString());
    
            SelfInspectHis siInfo = null;
            int newId = 999;
            String buffer = "";
    
            try 
            {
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
                while ((inputLine = in.readLine()) != null){
                    buffer += inputLine.trim();
                }
    
                siInfo = (SelfInspectHis)ums.unmarshal(new StringReader(buffer));
                boolean isEmpty = siInfo.getBody().getItems().getItem() == null ? true : false;
    
            } catch(Exception ex) {
                throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
            }

            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(siInfo.getBody().getItems().getItem())
                    .build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    private List<CC.SelfInspectHistoryVO> GetSelfInspInfoFromGDK(String _elNo, String _yyyymm) {
        try {
            StringBuffer    requestBuffer   = new StringBuffer();
            StringBuffer paramBuffer = new StringBuffer();
            paramBuffer.delete(0, paramBuffer.length());
            paramBuffer.append("&page_no=1");
            paramBuffer.append("&numOfRows=200");
            paramBuffer.append("&elevator_no="+_elNo);
            paramBuffer.append("&yyyymm="+_yyyymm);

            requestBuffer.append(selfInspectApi);
            requestBuffer.append("?ServiceKey=");
            requestBuffer.append(apiKey);
            requestBuffer.append(paramBuffer.toString());
    
            SelfInspectHis siInfo = null;
            int newId = 999;
            String buffer = "";
    
            try 
            {
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
                while ((inputLine = in.readLine()) != null){
                    buffer += inputLine.trim();
                }
    
                siInfo = (SelfInspectHis)ums.unmarshal(new StringReader(buffer));
                boolean isEmpty = siInfo.getBody().getItems().getItem() == null ? true : false;
    
            } catch(Exception ex) {
                return new ArrayList();
            }


            // SelfInspectHistoryVO
            // EL.ELEVATOR_NO ELEVATOR_NO
            // , EL.BULD_NM BULD_NM
            // , EL.ADDRESS1 ADDRESS1
            // , EL.INSTALLATION_PLACE INSTALLATION_PLACE
            // , HIS.SEL_CHK_BEGIN_DATE SEL_CHK_BEGIN_DATE
            // , HIS.SEL_CHK_USNM SEL_CHK_USNM
            // , HIS.SEL_CHK_ST_DT SEL_CHK_ST_DT
            // , HIS.SEL_CHK_EN_DT SEL_CHK_EN_DT
            // , HIS.COMPANY_NM COMPANY_NM
            // , HIS.SELCHK_RESULT_NM SELCHK_RESULT_NM
            // , HIS.TIT_NO TIT_NO
            // , HIS.SEL_CHK_ITEM_NM SEL_CHK_ITEM_NM
            // , HIS.SEL_CHK_ITEM_DT_NM SEL_CHK_ITEM_DT_NM
            // , HIS.SEL_CHK_RESULT SEL_CHK_RESULT
            // , HIS.CNFIRM_DT CNFIRM_DT
            // , HIS.REGIST_DT REGIST_DT
            List<SelfInspectHistoryVO> _rst = new ArrayList<>();

            for(SelfInspectHis.Item _item:siInfo.getBody().getItems().getItem()) {
                _rst.add(
                    SelfInspectHistoryVO.builder()
                                .selChkBeginDate(_item.getSelchkBeginDate())
                                .selChkUsnm(_item.getSelchkUsnm())
                                .selChkStDt(_item.getSelChkStDt())
                                .selChkEnDt(_item.getSelChkEnDt())
                                .companyNm(_item.getCompanyNm())
                                .selchkResultNm(_item.getSelchkResultNm())
                                .titNo(_item.getTitNo())
                                .selChkItemNm(_item.getSelChkItemNm())
                                .selChkItemDtNm(_item.getSelChkItemDtlNm())
                                .selChkResult(_item.getSelChkResult())
                                .registDt(_item.getRegistDt()).build()
                );
            }
            return _rst;
        }catch (Exception e){
            return new ArrayList();
        }
    }

    // 모바일 정기점검 결과 송신하는 서비스 :  GimaPei
    public ResponseEntity getSelfInspect(CC.InspectSearch request){
        try {
            List<CC.SelfInspectVO> selfInspect = ccRepository.selectSelfInsepect(request);

            for(CC.SelfInspectVO _vo:selfInspect) {
                _vo.setDetails(GetSelfInspInfoFromGDK(_vo.getElevatorNo(), _vo.getSelChkBeginDate().substring(0, 6)));
            }


            // for(int i = 0,len = selfInspect.size();i<len;i++){
            //     selfInspect.get(0).setDetails(ccRepository.selectSelfInsepectHistory(selfInspect.get(0)));
            // }
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(selfInspect)
                    .build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity getInspectManage(CC.InspectSearchByPortfolio request){
        try {
            List<CC.InspectHeadVO> insp = ccRepository.selectInsepectManage(request);
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(insp)
                    .build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity getInspectManageDetails(CC.InspectSearchByInspectHead request){
        try {
            String apiUrl = inspectFailApi.concat("?serviceKey=").concat(apiKey).concat("&numOfRows=1000");
            URL url = new URL(apiUrl.concat("&fail_cd=").concat(request.getFailCd()));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/xml");
            JAXBContext jbc = JAXBContext.newInstance(InspectFail.class);
            Unmarshaller ums = jbc.createUnmarshaller();
            InspectFail failDetails = (InspectFail) ums.unmarshal(conn.getInputStream());

            List<InspectFail.Item> resultList = failDetails.getBody().getItems().getItem();
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(resultList)
                    .build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }
    public ResponseEntity getInspect(CC.InspectSearch request){
        try {
            List<CC.InspectVO> inspect = ccRepository.selectInsepect(request);
            /*
            String apiUrl = inspectFailApi.concat("?serviceKey=").concat(apiKey).concat("&numOfRows=1000");
            for(int i=0,len = inspect.size(); i < len ; i++){
                if(inspect.get(i).getFailCd()==null){
                    continue;
                }
                URL url = new URL(apiUrl.concat("&fail_cd=").concat(inspect.get(i).getFailCd()));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/xml");
                JAXBContext jbc = JAXBContext.newInstance(InspectFail.class);
                Unmarshaller ums = jbc.createUnmarshaller();
                try{
                    InspectFail failDetails = (InspectFail) ums.unmarshal(conn.getInputStream());

                    int total = Integer.parseInt(failDetails.getBody().getTotalCount());
                    if(total > 0){
                        inspect.get(i).setFails(failDetails.getBody().getItems().getItem());
                    }
                }catch(Exception e){
                    log.error(e.getMessage());
                    continue;   
                }
            }
            */
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(inspect)
                    .build(),request.getUserLocale());
        }catch (Exception e){
            log.error(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity getSelfInspCountInfoByPortfolio(CC.InspectSearchByPortfolioExtMonth request){

        // HCCC와 카운트를 맞추기위해서
        // i.e hccc에는 계약이 변경되어 거래선(A01 to B01)이 변경되었으나 데이타가 남아있어 호기번호로 조인시에는 중복된 Elevator값이 나올수 있다. 
        // 실제로는 계약변경에 의해서 인전 계약인 A01은 최신상태만 가지는 HCCC테이블에서 빠져야하지만 안빼고 있는 관계로
        // 우리는 통합프로젝트+거래선으로 조회하도록한다. 
        List<String> _intgPrjNos = ccRepository.getPrjInfoWithPortfolioId(CC.CCCurrentCalenderRequest.builder().userPortfolioMappingId(request.getUserPortfolioMappingId().toString()).build());
        request.setProjnos(ccRepository.selectProjnosByPortfolio(request.getUserPortfolioMappingId().toString()));


        CC.DashBoradeSelfInspectCountInfoVO resultVo = new CC.DashBoradeSelfInspectCountInfoVO();
        if(0 < request.getProjnos().size()){

            Long total = hcccRepository.selectAllCount2(_intgPrjNos);

            // Long total = ccRepository.selectSelfInspTotalWithMonth(request);
            Long success = ccRepository.selectSelfInspSuccessWithMonth(request);
            Long remote = hrtsRepository.selectSelfInspRemoteWithMonth(request);

            resultVo.setTotal(total);
            resultVo.setSuccess(success+remote);
            resultVo.setRemote(remote);
            resultVo.setField(success);
        }
        return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(resultVo)
                        .build()
                ,request.getUserLocale());
    }

    public ResponseEntity getServices(CC.CCServiceRequest request) {
        try {
            List<String> _prjInfoIds = ccRepository.getPrjInfoWithPortfolioId(request);
            request.setIntgPrjNoAndTrlineCds(_prjInfoIds);

            List<CCServiceVO> _result = ccRepository.getServices(request);
            
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(_result)
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            log.error(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    /**
     * 칼렌더는 3가지 정보를 조회하여 출력한다.
     * 1. 정기검사 내역
     * 2. 정기점검 내역
     * 3. 고장신고 내역
     */
    public ResponseEntity getCalender(CC.CCCurrentCalenderRequest request) {
        try {

            List<String> _prjInfoIds = ccRepository.getPrjInfoWithPortfolioId(request);
            request.setIntgPrjNoAndTrlineCds(_prjInfoIds);
            List<CalenderVO> _calData = hcccRepository.getHcccReceptionInfoForMonth(request);
            List<CalenderVO> _inspData = ccRepository.selectInsepectCurrentCalender(request);
            List<CalenderVO> _selfInspData = ccRepository.selectSelfInsepectCurrentCalender(request);

            List<CalenderVO> _result = new ArrayList<>();
            _result.addAll(_calData);
            _result.addAll(_inspData);
            _result.addAll(_selfInspData);

            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(_result)
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            log.error(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity getElevators(CC.CCCurrentElevatorSearchByPortfolio request){
        try {

            List<CCElevatorInfoVO> _elinfoList = ccRepository.selectElevatorList(request);
            List<CCElevatorInfoVO> _lastList = hcccRepository.selectElevatorList(_elinfoList);

            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(_lastList)
                    .build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }
    public ResponseEntity getElevator(CC.CCCurrentElevatorRequest request){
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(ccRepository.selectElevator(request))
                    .build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }
    public ResponseEntity getElevator(CC.CCCurrentElevatorChangeDateRequest request){
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(ccRepository.selectElevatorInfoWithChangeDate(request))
                    .build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }
    public ResponseEntity getBillHistory(CC.CCCurrentBillHistoryRequest request){
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(ccRepository.selectBillHistory(request))
                    .build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity getUnitsTree(CC.CCCurrentPortfolioRequest request){
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(ccRepository.selectUnitTreeByPortfolio(request))
                    .build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity getAlarmHis(CC.AlarmSearchREQ request){
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(ccRepository.getAlarmHis(request))
                    .build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity getMsgTemplate(CC.MsgTemplateSearchREQ request){
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(ccRepository.getMsgTemplate(request))
                    .build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity updateMsgTemplate(CC.MsgTemplateUpdateREQ request){

        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(ccRepository.updateMsgTemplate(request))
                    .build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity updateWidgets(CC.DashboardWidgetREQ request){

        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(ccRepository.updateWidgets(request))
                    .build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    /** 
     * 사용자가 서비스를 요청함
     * 고장신고
     * 청구서 재발행
     * 청구서 받는 방법 변경
     * 세금계산서수신처 변경
     * 납부방법 변경
     * 고객정보 변경
     */
    public ResponseEntity serviceRequest(CC.CCServiceREQ request){
        ServiceReqInfoVO sVO = null;
        try {
            sVO = ccRepository.selectServiceReqInfo(request);
        } catch(Exception ex) {
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
        // 이것은 하드코딩 0010 고장신고, tb_master_data_detail에 SERVICE_TYPE의 코드가 0010은 고장신고여야 한다. 
        if("0010".equals(request.getServiceType())) {
            String encodeStr  = null;
            String encodeStr2 = null;
            try {
                encodeStr  = URLEncoder.encode(request.getContents(), "UTF-8").replaceAll("\\+", "%20");
                encodeStr2 = URLEncoder.encode(request.getUserName(), "UTF-8");
            } catch(Exception e) {
                encodeStr = "URL encode error for contest.";
            }

            StringBuffer _params = new StringBuffer();
            _params.append("?sid="+sVO.getElevatorNo());
            _params.append("&intgPrjNo="+sVO.getIntgPrjNo());
            _params.append("&dealLineDiv="+sVO.getTrlineCd());
            _params.append("&prjNo="+sVO.getPrjNo());
            _params.append("&hoNo="+sVO.getHoNo());
            _params.append("&conslScd="+request.getServiceCd());
            _params.append("&bkdnFlor=0");
            _params.append("&acptCntn="+ encodeStr);
            _params.append("&acptGubun="+ (request.getMobile() ? "M" : "W"));
            _params.append("&custName="+ encodeStr2);
            _params.append("&custNumber="+ (request.getPhonenumber().replaceAll("-", "")));
            
            String reqUrl = hcccServiceReqApi + _params.toString();
            System.out.println(reqUrl);

            ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            RestTemplate restTemplate = new RestTemplateBuilder()
                    .requestFactory(() -> requestFactory)
                    .build();
            
            MasterData.MasterDataSelect mVO = new MasterData.MasterDataSelect();
            mVO.setMasterdataKey("SERVICE_REQ_YN");
            List<MasterDataDetailVO> rstVO = masterDataRepository.selectDetails(mVO);
            String serviceReqYN = ( rstVO == null || rstVO.isEmpty()) ? "n" : rstVO.get(0).getCode();

            if("y".equals(serviceReqYN) || "Y".equals(serviceReqYN)) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                ResponseEntity<String> response = restTemplate.exchange(reqUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);

                System.out.println("완료");

                int _stat = response.getStatusCode().value();
                if (_stat > 299) {
                    System.out.println(_stat + " Error!");
                    throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
                } else {
                    JSONObject jo = new JSONObject(response.getBody());
                    try {
                        if(sVO != null) {
                            sVO.setUserId(request.getUserId());
                            ccRepository.insertServiceRequest(sVO);
                        }
                        System.out.println("키 : " + sVO);
                    }catch (Exception e){
                        throw new RuntimeException(message.becauseFailureInsert(request.getUserLocale()));
                    }

                    return responseTemplate.withSuccess(ResponseBody.builder()
                                .result(ResultCode.SUCCESS.getValue())
                                .data(sVO)
                                .build()
                            , request.getUserLocale());
                }
            } else {
                log.info("서비스 요청하기를 n 로 설정함. 마스터키 : SERVICE_REQ_YN 참조");
                return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(sVO)
                    .build()
                , request.getUserLocale()); 
            }
        } else {
            // step #1. log to db
            try {
                if(sVO != null){
                    sVO.setUserId(request.getUserId());
                    ccRepository.insertServiceRequest(sVO);
                } 
                System.out.println("키 : " + sVO);
            }catch (Exception e){
                throw new RuntimeException(message.becauseFailureInsert(request.getUserLocale()));
            }
            
            // step #2. send email
            try{
                List<String> receiverEmail = new ArrayList();
                receiverEmail.add(sVO.getReceiverEmail());

                StringBuffer strBuf = new StringBuffer();
                strBuf.append("*** 미리포탈 서비스 요청 *** \n");
                strBuf.append("[유상계약번호] : "+ sVO.getCompsCntrNo() + " \n");
                strBuf.append("[승강기번호] : "+ sVO.getElevatorNo() + " \n");
                strBuf.append("[요청내역] \n");
                strBuf.append(sVO.getContents());
                
                MailVO.Request mailVO = MailVO.Request.builder()
                                        .fromAddress("MIRIPortaladmin@hyundaielevator.com")
                                        .subject(request.getServiceTypeNm() + "요청 건")
                                        .toAddress(receiverEmail)
                                        .content(strBuf.toString()).build();

                try {
                    mailService.send(mailVO);
                    
                    return responseTemplate.withSuccess(ResponseBody.builder()
                                .result(ResultCode.SUCCESS.getValue())
                                .data("메일이 전송되었습니다.")
                                .build(),request.getUserLocale());
                    }catch (Exception e){
                        throw new RuntimeException(message.becauseFailureSendMail(request.getUserLocale()));
                    }
            } catch(Exception ex) {
                throw new RuntimeException(message.becauseFailureSendMail(request.getUserLocale()));
            }
        }    
    }
}
