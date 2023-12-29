package com.hdel.miri.api.domain.hrts;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdel.miri.api.domain.cc.CC;
import com.hdel.miri.api.domain.cc.CCRepository;
import com.hdel.miri.api.domain.cc.CC.PrjNoVO;
import com.hdel.miri.api.domain.hccc.HCCCService;
import com.hdel.miri.api.domain.hrts.HRTS.HRTSConnectionResultVO;
import com.hdel.miri.api.domain.hrts.HRTS.HRTSMonitResultVO;
import com.hdel.miri.api.domain.hrts.HRTS.HRTSRuleVO;
import com.hdel.miri.api.util.response.Message;
import com.hdel.miri.api.util.response.ResponseBody;
import com.hdel.miri.api.util.response.ResponseTemplate;
import com.hdel.miri.api.util.response.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HRTSService {

    @Value("${api.hrts-conn}")
    private String apiHrtsConn;

    @Value("${api.hrts-monit}")
    private String apiHrtsMonit;

    @Value("${api.hrts-rules}")
    private String apiHrtsRules;

    @Value("${api.hrts-hccc}")
    private String apiHrtsHccc;

    @Value("${cloud.aws.s3.bucket}")
    private String hrtsBucket;

    private final Message message;
    private final HRTSRepository hrtsRepository;
    private final CCRepository ccRepository;
    private final ResponseTemplate responseTemplate;
    private final HCCCService hcccService;
    private final AmazonS3 amazonS3;

    private String getS(int month){
        return String.valueOf((month < 10 ? "0".concat(String.valueOf(month)) : month));
    }

    private String getFirstDay(LocalDateTime time){
        return getS(time.getYear())
                .concat(getS(time.getMonth().getValue()))
                .concat(getS(1));
    }
    private String getLastDay(LocalDateTime time){
        YearMonth yearMonth = YearMonth.of(time.getYear(),time.getMonth().getValue());
        LocalDate end = yearMonth.atEndOfMonth();
        return getS(time.getYear())
                .concat(getS(time.getMonth().getValue()))
                .concat(getS(time.getMonth().maxLength()));
    }

    private HRTS.HRTSRunAvgSearch calculated(LocalDateTime start, HRTS.HRTSRunAvgSearch request){
        LocalDateTime currentDate = start;

        request.setFirstYear(getS(currentDate.getYear()));
        request.setFirstMonth(getS(currentDate.getMonth().getValue()));
        request.setFirstStart(getFirstDay(currentDate));
        request.setFirstEnd(getLastDay(currentDate));

        currentDate = currentDate.minusMonths(1);
        request.setSecondYear(getS(currentDate.getYear()));
        request.setSecondMonth(getS(currentDate.getMonth().getValue()));
        request.setSecondStart(getFirstDay(currentDate));
        request.setSecondEnd(getLastDay(currentDate));

        currentDate = currentDate.minusMonths(1);
        request.setThirdYear(getS(currentDate.getYear()));
        request.setThirdMonth(getS(currentDate.getMonth().getValue()));
        request.setThirdStart(getFirstDay(currentDate));
        request.setThirdEnd(getLastDay(currentDate));
        return request;
    }
    private HRTS.HRTSDistanceAvgSearch calculated(HRTS.HRTSDistanceAvgSearch request,List<CC.ELInfoJoinToHCCC> join){

        List<HRTS.HRTSDistance> list = new ArrayList<>();
        LocalDateTime currentDate = LocalDateTime.now();
        request.setEnd(getLastDay(currentDate));
        currentDate = currentDate.minusMonths(12);
        request.setStart(getFirstDay(currentDate));
        for(int i = 0 , len = join.size() ; i < len ; i++){
            HRTS.HRTSDistance distanceTarget = new HRTS.HRTSDistance();
            distanceTarget.setPrjNo(join.get(i).getPrjNo());
            distanceTarget.setCarNo(join.get(i).getHoNo());
            list.add(distanceTarget);
        }
        request.setTargets(list);
        return request;
    }
    public ResponseEntity getMasterInfoWeb(HRTS.HRTSMasterSearchWeb request){
        try {
            request.setJoinList(ccRepository.selectProjnosByPortfoliAIMasterJoin(request));
            if(0 < request.getJoinList().size()){
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(hrtsRepository.selectAIMasterInfoForWeb(request))
                        .build(), request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(new ArrayList<>())
                    .build(), request.getUserLocale());
        } catch (Exception e){
            log.error(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity getRemoteInspWeb(HRTS.HRTSMonitVO request){
        String prjKey = request.getProjNo().concat(request.getHoNo());
        try {
            URL url = new URL(apiHrtsHccc.concat(prjKey));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            List<Map<String,String>> baseInfoList = (List<Map<String, String>>) new ObjectMapper().readValue(new String(bis.readAllBytes()), HashMap.class).get("base_info");

            if(baseInfoList == null || baseInfoList.isEmpty()) {
                HRTS.HRTSMasterJoinWeb master = hrtsRepository.selectAIMasterJoinWeb(request);
                master.setAddress("-");
                master.setModel("-");
                master.setService_type("-");
                master.setSetupDate("-");
                master.setExpirationDate("-");
                return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(master)
                    .build(), request.getUserLocale()); 
            }

            HRTS.HRTSMasterJoinWeb master = hrtsRepository.selectAIMasterJoinWeb(request);
            if(0 < baseInfoList.size()){
                Map<String,String> hcccInfo = baseInfoList.get(0);
                master.setAddress(hcccInfo.get("address"));
                master.setModel(hcccInfo.get("model"));
                master.setService_type(hcccInfo.get("service_type"));
                master.setSetupDate(hcccInfo.get("setup_date"));
                master.setExpirationDate(hcccInfo.get("expiration_date"));
            }
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(master)
                    .build(), request.getUserLocale());
        }catch ( Exception e ){
            log.error(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }
    public ResponseEntity getMasterInfo(HRTS.HRTSMasterSearch request){
        String prjKey = request.getPrjNo().concat(request.getCarNo());
        try {
            URL url = new URL(apiHrtsHccc.concat(prjKey));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            List<Map<String,String>> baseInfoList = (List<Map<String, String>>) new ObjectMapper().readValue(new String(bis.readAllBytes()), HashMap.class).get("base_info");
            List<HRTS.HRTSMasterJoin> master = hrtsRepository.selectAIMasterJoin(request);
            if(baseInfoList != null)
            {
                if(0 < baseInfoList.size()){
                    Map<String,String> hcccInfo = baseInfoList.get(0);
                    for (int i=0,len=master.size();i<len;i++){
                        master.get(i).setAddress(hcccInfo.get("address"));
                        master.get(i).setSetupDate(hcccInfo.get("setup_date"));
                        master.get(i).setExpirationDate(hcccInfo.get("expiration_date"));
                    }
                }
            }
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(master)
                    .build(), request.getUserLocale());
        } catch ( Exception e ){
            log.error(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }
    public ResponseEntity getMasterInfoTest(HRTS.HRTSMasterSearch request){
        String prjKey = request.getPrjNo().concat(request.getCarNo());
        try {
            URL url = new URL(apiHrtsHccc.concat(prjKey));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            List<Map<String,String>> baseInfoList = (List<Map<String, String>>) new ObjectMapper().readValue(new String(bis.readAllBytes()), HashMap.class).get("base_info");
            List<HRTS.HRTSMasterJoin> master = hrtsRepository.selectAIMasterJoinTest(request);
            if(baseInfoList != null)
            {
                if(0 < baseInfoList.size()){
                    Map<String,String> hcccInfo = baseInfoList.get(0);
                    for (int i=0,len=master.size();i<len;i++){
                        master.get(i).setAddress(hcccInfo.get("address"));
                        master.get(i).setSetupDate(hcccInfo.get("setup_date"));
                        master.get(i).setExpirationDate(hcccInfo.get("expiration_date"));
                    }
                }
            }
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(master)
                    .build(), request.getUserLocale());
        } catch ( Exception e ){
            log.error(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }
    public ResponseEntity getRunTimeAvgerageCurrentMonth(HRTS.HRTSRunAvgSearch request){
        String strDate = request.getRequestDate().concat(" 00:00:00.000");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss.SSS");
        calculated(LocalDateTime.parse(strDate,formatter),request);
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                            .result(ResultCode.SUCCESS.getValue())
                            .data(hrtsRepository.selectRunTimeCurrentMonth(request))
                            .build()
                    , request.getUserLocale());
        } catch ( Exception e ){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity getDistanceAverage(HRTS.HRTSDistanceAvgSearch request){
        try {
            List<PrjNoVO> joinList = ccRepository.selectProjNosFromMappingId(request);
            HRTS.HRTSAvgRunDistanceResultVO resultValue = hrtsRepository.selectRunAvgTargetAll(joinList);

            return responseTemplate.withSuccess(ResponseBody.builder()
                            .result(ResultCode.SUCCESS.getValue() + "/" + resultValue.getStartDate() + "/" + resultValue.getEndDate())
                            .data(resultValue.getElAvgRunDistance())
                            .build()
                    , request.getUserLocale());
        } catch ( Exception e ){
            log.error(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity getRunInfo(HRTS.HRTSRunAvgSearch request){
        // String strDate = request.getRequestDate().concat(" 00:00:00.000");
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss.SSS");
        // LocalDateTime dateTime = LocalDateTime.parse(strDate, formatter);
        // System.out.println(dateTime);


        // calculated(LocalDateTime.parse(strDate,formatter),request);

        HRTS.ResultRunAvgVO result = new HRTS.ResultRunAvgVO();
        try {
            result.setType(hrtsRepository.selectRunAvgType(request));
            result.setTarget(hrtsRepository.selectRunAvgTarget(request));
            
            return responseTemplate.withSuccess(ResponseBody.builder()
                            .result(ResultCode.SUCCESS.getValue())
                            .data(result)
                            .build()
                    , request.getUserLocale());
        } catch ( Exception e ){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity getDOCCInfo(HRTS.HRTSRunAvgSearch request){
        String strDate = request.getRequestDate().concat(" 00:00:00.000");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss.SSS");
        LocalDateTime dateTime = LocalDateTime.parse(strDate, formatter);
        System.out.println(dateTime);


        calculated(LocalDateTime.parse(strDate,formatter),request);
        HRTS.ResultDOCCAvgVO result = new HRTS.ResultDOCCAvgVO();

        try {
            result.setType(hrtsRepository.selectDOCCType(request));
            result.setTarget(hrtsRepository.selectDOCCTarget(request));
            return responseTemplate.withSuccess(ResponseBody.builder()
                            .result(ResultCode.SUCCESS.getValue())
                            .data(result)
                            .build()
                    , request.getUserLocale());
        } catch ( Exception e ){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    // TODO : 테이블 변경에 의한 처리방안 고민필요 -> 2023.4.7
    public ResponseEntity getPerformanceResult(HRTS.HRTSRunAvgSearch request){

        // String strDate = request.getRequestDate().concat(" 00:00:00.000");
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss.SSS");
        // LocalDateTime dateTime = LocalDateTime.parse(strDate, formatter);
        // System.out.println(dateTime);
        // calculated(LocalDateTime.parse(strDate,formatter),request);

        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                            .result(ResultCode.SUCCESS.getValue())
                            .data( hrtsRepository.selectHRTSPerformanceCheckResult(request) )
                            .build()
                    , request.getUserLocale());
        } catch ( Exception e ){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }

    }

    /**
     * HRTS원격접속을 시도한다. 
     * on
     * 0 : 연결해제
     * 1 : 연결시도(접속)
     * 3 : 접속상태 확인
     * 접속상태
     * 0 : 접속 해제 또는 접속 실패
     * 1 : 접속 성공
     * 4 : 접속 유지중
     * 5 : 미연결상태
     * 7 : 단독형 현장으로 접속 요청 전송 (SMS 전송)
     * @param request
     * @return ResponseEntity
     */
    public ResponseEntity getHrtsConnection(HRTS.HRTSConnectionVO request) {
        // #projectcarno/connection/on/#command
        String callUrl = apiHrtsConn;
        callUrl = callUrl.replace("#projectcarno", request.getProjNo()+request.getHoNo());
        callUrl = callUrl.replace("#command", request.getCommand() );

        log.error("  request.getCommand() ==> "+  request.getCommand());
        String getOn = request.getCommand();
        
        JSONObject responseJson = null;
        int exitDoLoop = 0;
        int maxLoopCnt = 15;
        int apiStatus = 0;
        int connectionStatus = 0;
        String elType= "PT";

        URL url;
        URL urlS;
        HttpURLConnection conn = null;
        HttpURLConnection connS = null;
        BufferedReader  br = null;
        BufferedReader  brS = null;
        StringBuilder   sb = null; 
        StringBuilder   sbS = null;      

        try 
        {            
            url = new URL(callUrl); 
            log.error("start1===================>");
            conn = (HttpURLConnection) url.openConnection();      
            log.error("start2===================>");      
            conn.setRequestProperty("Content-Type", "application/json");
            log.error("start3===================>");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("POST");

            log.error("start4===================>"+conn.getContentType());   
            
            int responseCode = conn.getResponseCode();
            log.error("start5===================>"+responseCode);
            if (responseCode > 299) {
                System.out.println(responseCode + " Error!");
            } else {
                // HRTS에 접속후 결과를 받는다. 
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                responseJson = new JSONObject(sb.toString());
                int rtnRst = responseJson.getInt("result");

                // 접속 성공 메세지
                if(rtnRst == 1) {
                    do{
                        callUrl = apiHrtsConn;
                        callUrl = callUrl.replace("#projectcarno", request.getProjNo()+request.getHoNo());
                        callUrl = callUrl.replace("#command", "3");
                        
                        urlS = new URL(callUrl);
                        connS = (HttpURLConnection) urlS.openConnection();
                        connS.setRequestProperty("Content-Type", "application/json");
                        connS.setConnectTimeout(5000);
                        connS.setReadTimeout(5000);
                        connS.setRequestMethod("POST");

                        responseCode = connS.getResponseCode();
                        if (responseCode > 299) {
                            exitDoLoop = 1;
                            System.out.println(responseCode + " Error!");
                        } else {
                            brS = new BufferedReader(new InputStreamReader(connS.getInputStream()));
                            sbS = new StringBuilder();
                            line = "";
                            while ((line = brS.readLine()) != null) {
                                sbS.append(line);
                            }
                            responseJson = new JSONObject(sbS.toString());
                            apiStatus = responseJson.getInt("result");
                            connectionStatus = responseJson.getInt("status");
                            elType = responseJson.getString("eltype");

                            // log.error("SBS-toString::>"+sbS.toString());
                            // System.out.println("API호출상태:"+apiStatus);
                            // System.out.println("연결상태:"+connectionStatus);
                            // System.out.println("한턴돌음....."+maxLoopCnt);
                            // 0 : 접속해지 요청
                            if("0".equals(getOn) ){
                                   if(connectionStatus == 5){
                                        exitDoLoop = 1;
                                        connS.disconnect();
                                        brS.close();
                                        log.error(":::::::연결해지:::["+request.getProjNo()+request.getHoNo()+"]:::: exitDoLoop:>["+exitDoLoop+"], connectionStatus["+ connectionStatus+ "], maxLoopCnt["+maxLoopCnt+"]");
                                        break ;
                                   }else{
                                        maxLoopCnt -= 1; 
                                   }
                            }else{
                            // 1: 접속성공, 4: 접속 유지중
                                if(connectionStatus == 1 || connectionStatus == 4 ) {
                                    exitDoLoop = 1;
                                    connS.disconnect();
                                    brS.close();
                                    log.error(":::::::연결:::["+request.getProjNo()+request.getHoNo()+"]:::: exitDoLoop:>["+exitDoLoop+"], connectionStatus["+ connectionStatus+ "], maxLoopCnt["+maxLoopCnt+"]");
                                    break ;
                                } else {
                                    maxLoopCnt -= 1;
                                  
                                }
                            }

                            if (maxLoopCnt == 0 ) {
                                connS.disconnect();
                                brS.close();
                                log.error(":::::::호출중단:::["+request.getProjNo()+request.getHoNo()+"]:::: exitDoLoop:>["+exitDoLoop+"], connectionStatus["+ connectionStatus+ "], maxLoopCnt["+maxLoopCnt+"]");
                                break ;
                            }
                            brS.close();
                        }
                        TimeUnit.SECONDS.sleep(4);
                        log.error(":::::::미연결:::["+request.getProjNo()+request.getHoNo()+"]:::: exitDoLoop:>["+exitDoLoop+"], connectionStatus["+ connectionStatus+ "], maxLoopCnt["+maxLoopCnt+"]");                                                        
                        connS.disconnect();

                    } while(exitDoLoop != 1 || maxLoopCnt == 0 );
                }
                br.close();
            }
            conn.disconnect();            
        } catch(Exception ex) {
            log.error("====INTERNAL========>"+ex.toString());
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        } finally {
            if (conn != null)
                conn.disconnect(); 

            if (connS != null)
                connS.disconnect();     
        }
        // 이또한 접속실패 5번 시도해서 정상수치를 못 받은경우
        if(connectionStatus != 1 || connectionStatus != 4) {
            responseJson = new JSONObject();
            responseJson.put("connectResult", "true");

            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(HRTSConnectionResultVO.builder().projNo(request.getProjNo()).hoNo(request.getHoNo()).result(1).elType(elType).build())
                    .build()
            , request.getUserLocale());
        } else {
            log.error("접속성공 메세지 송신.....");
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }

    }


    /**
     * HRTS접속후 당 메소드를 통해서 실시간 데이타를 가져온다. 
     * 2초에 1회 호출하는것을 전제로 한다. 
     */
    public ResponseEntity getHrtsStatus(HRTS.HRTSMonitVO request) {
        // #projectcarno
        String callUrl = apiHrtsMonit;
        callUrl = callUrl.replace("#projectcarno", request.getProjNo()+request.getHoNo());
        
        JSONObject respJson = null;
        HttpURLConnection conn = null;
        try 
        {
            URL url = new URL(callUrl);
            log.error("monit1===================>");
            conn = (HttpURLConnection) url.openConnection();
            log.error("monit2===================>");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(6000);
            conn.setReadTimeout(6000);
            conn.setRequestMethod("GET");
            log.error("monit3===================>");

            int responseCode = conn.getResponseCode();
            log.error("monit4===================>"+responseCode);

            if (responseCode > 299) {
                System.out.println(responseCode + " Error!");
                throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
            } else {
                // HRTS에 접속후 결과를 받는다. 
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                respJson = new JSONObject(sb.toString());

                br.close();
            }
        } catch(Exception ex) {
            log.error("exception====>"+ ex.toString());
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        } finally {
            if(conn != null)
                conn.disconnect();
        }
log.error("monit5===================>");
        int rtnRst = respJson.getInt("result");
log.error("monit6===================>"+rtnRst);        
        if(rtnRst == 1) {
            /// 0 : 정지, 1 : 정상, 3 : 고장
            int runStatus = respJson.getInt("faultstate") == 1 ? 3 : respJson.getInt("run");

            // 0 : 정지, 1 : 상항, 2 : 하향
            int moveDirection = respJson.getInt("updirection") == 1 ? 1 : respJson.getInt("dndirection") == 1 ? 2 : 0;
log.error("monit7===================>");
            // 0 : 닫힘, 1 : 열림, UI상 문이 양쪽으로 있는 통과형은 고려하지 않으므로 하나라도 열려있으면 열림, 그외 닫힘이다. 
            int doorStatus = 0;
            if(respJson.has("doorstatus")) {
                doorStatus = respJson.getInt("doorstatus") == 1 ? 1 : 0;
            } else {
                doorStatus = respJson.getInt("fdoorstatus") == 1 ? 1 : 0;
                if(doorStatus != 1) 
                doorStatus = respJson.getInt("rdoorstatus") == 1 ? 1 : 0;
            }
log.error("monit8===================>");
            HRTSMonitResultVO rst = HRTSMonitResultVO.builder()
                                    .result(rtnRst)
                                    .projNo(request.getProjNo())
                                    .hoNo(request.getHoNo())
                                    .timestamp(respJson.getString("timestamp"))
                                    .currentFloor(respJson.getString("currentdispfloor"))
                                    .currentdispfloor(respJson.getString("currentdispfloor"))
                                    .runStatus(runStatus)
                                    .moveDirection(moveDirection)
                                    .runMode(respJson.getInt("auto"))
                                    .faultStatus(respJson.getInt("faultstate"))
                                    .doorStatus(doorStatus)
                                    .fdoorStatus( respJson.has("fdoorstatus") ? respJson.getInt("fdoorstatus") == 1 ? 1 : 0 : 0)
                                    .rdoorStatus(respJson.has("rdoorstatus") ? respJson.getInt("rdoorstatus") == 1 ? 1 : 0 : 0)
                                    .build();
log.error("monit9===================>");
            return responseTemplate.withSuccess(ResponseBody.builder()
                .result(ResultCode.SUCCESS.getValue())
                .data(rst)
                .build()
            , request.getUserLocale());
        } else {
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    /**
     * HRTS접속후 당 메소드를 통해서 엘리베이터의 Rule을 가져온. 
     * 사용자가 누를때 값을 조회해 온다. 
     */
    public ResponseEntity getHrtsRules(HRTS.HRTSConnectionVO request) {
        String callUrl = apiHrtsRules;
        callUrl = callUrl.replace("#projectcarno", request.getProjNo()+request.getHoNo());
        callUrl = callUrl.replace("#command", request.getCommand());

        JSONObject respJson = null;
        HttpURLConnection conn = null;
        try 
        {
            URL url = new URL(callUrl);
            log.error("rule1===================>");
            conn = (HttpURLConnection) url.openConnection();
            log.error("rule2===================>");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(6000);
            conn.setReadTimeout(6000);
            conn.setRequestMethod("GET");
            log.error("rule3===================>");

            int responseCode = conn.getResponseCode();     
             log.error("rule4===================>"+responseCode);       
            if (responseCode > 299) {
                System.out.println(responseCode + " Error!");
                throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
            } else {
                     log.error("rule5===================>");    
                // HRTS에 접속후 결과를 받는다. 
                BufferedReader  br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder   sb = new StringBuilder();
                String line = "";
                log.error("rule6===================>"); 
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                respJson = new JSONObject(sb.toString());
                log.error("rule7===================>"); 
                br.close();
            }        
        } catch( RuntimeException re){
            log.error("runtimeexception====>"+ re.toString());
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        } catch(Exception ex) {
            log.error("exception====>"+ ex.toString());
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));    
        }finally{
            if(conn != null)
                conn.disconnect();
        }

        int rtnRst = respJson.getInt("result");
log.error("rule8===================>"+ rtnRst); 
     
        if(rtnRst == 1) {
            JSONObject tmpObj = (JSONObject)respJson.getJSONArray("services").get(0);
            log.error("rule9===================>" + tmpObj.toString());
            HRTSRuleVO rst = HRTSRuleVO.builder().projNo(request.getProjNo())
                                                .hoNo(request.getHoNo())
                                                .result(rtnRst)
                                                .command(request.getCommand())
                                                .value(tmpObj.getString("value"))
                                                .build();
            return responseTemplate.withSuccess(ResponseBody.builder()
                .result(ResultCode.SUCCESS.getValue())
                .data(rst)
                .build()
            , request.getUserLocale());
        } else {
            log.error("rule10===================>"+message.becauseFailureSelect(request.getUserLocale())); 
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity getHrtsReport(HRTS.HRTSReportReqVO request){

        HRTS.HRTSReportVO _fileInfo = hrtsRepository.getHrtsReport(request);

        if(_fileInfo != null) {
            try {
                    // S3Object _s3file = amazonS3.getObject(new GetObjectRequest(hrtsBucket, "202211/106207/202211_청라롯데캐슬아파트_1동2호기(106207L02)_20221207출력.xlsx"));
                    S3Object _s3file = amazonS3.getObject(new GetObjectRequest(hrtsBucket, _fileInfo.getS3Key()));
                        
                    S3ObjectInputStream objectInputStream = _s3file.getObjectContent();
                    byte[] bytes = IOUtils.toByteArray(objectInputStream);
                    String fileName = URLEncoder.encode(_fileInfo.getFileNm(), "UTF-8").replaceAll("\\+", "%20");

                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                    httpHeaders.setContentLength(bytes.length);
                    httpHeaders.setContentDispositionFormData("attachment", fileName);
                    return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
            } catch(Exception ex) {
                System.out.println(ex);
                return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
            }        
        } else {
            return new ResponseEntity<>(null, new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }
}