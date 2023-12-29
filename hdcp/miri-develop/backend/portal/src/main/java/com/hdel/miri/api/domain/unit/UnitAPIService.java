package com.hdel.miri.api.domain.unit;

import com.google.gson.Gson;
import com.hdel.miri.api.domain.cc.CCRepository;
import com.hdel.miri.api.domain.cc.CC.BuldNmVO;
import com.hdel.miri.api.domain.scrm.SCRM;
import com.hdel.miri.api.domain.scrm.SCRMRepository;
import com.hdel.miri.api.domain.srm.SRMRepository;
import com.hdel.miri.api.util.response.Message;
import com.hdel.miri.api.util.response.ResponseBody;
import com.hdel.miri.api.util.response.ResponseTemplate;
import com.hdel.miri.api.util.response.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.hdel.miri.api.domain.cc.CC;

import javax.mail.FetchProfile.Item;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UnitAPIService {

    private final Message message;
    private final ResponseTemplate responseTemplate;
    private final CCRepository ccRepository;
    private final SCRMRepository scrmRepository;
    private final SRMRepository srmRepository;

    @Value("${api.unit-service1}")
    private String unitService1;

    public ResponseEntity materialByProjnosCountInfo(UnitAPI.UnitMaterialsByProjnoCountInfoRequest request, HttpServletRequest sRequest){
        try {

            UnitAPI.UnitMaterialsByProjnoSubRequest params = new UnitAPI.UnitMaterialsByProjnoSubRequest();
            params.setProjnos(ccRepository.selectProjnosByPortfolio(request.getUserPortfolioMappingId().toString()));
            if(0 < params.getProjnos().size()){
                URL url = new URL(unitService1+"?visible=true");
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setDefaultUseCaches(false);
                http.setDoInput(true);
                http.setDoOutput(true);
                http.setRequestMethod("POST");
                http.setRequestProperty("Content-type", "application/json");
                http.setRequestProperty("Accept", "application/json");

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(http.getOutputStream()));
                bw.write(new Gson().toJson(params));
                bw.flush();
                bw.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));

                int responseCode = http.getResponseCode();
                UnitAPI.UnitMaterialsByProjnoResponse  resultJson = null;
                UnitAPI.UnitMaterialsByProjnoCountInfoResponse countInfo = new UnitAPI.UnitMaterialsByProjnoCountInfoResponse();
                log.info("Response Code : {}",responseCode);
                if(200 == responseCode){
                    StringBuffer sb = new StringBuffer();
                    String rLine = "";
                    while((rLine = br.readLine()) !=null ){ sb.append(rLine); }
                    resultJson = new Gson().fromJson(sb.toString(),UnitAPI.UnitMaterialsByProjnoResponse.class);


                    List<UnitAPI.UnitMaterialByProjno> resultProjnos = resultJson.getProjnos();
                    for(int i = 0,len = resultJson.getProjnos().size(); i < len ; i++){
                        List<UnitAPI.UnitMaterial> materialList = resultProjnos.get(i).getMaterials();
                        for(int j = 0,mLen = materialList.size(); j < mLen ; j++){
                            countInfo.setAllCount(countInfo.getAllCount() + 1);
                            UnitAPI.UnitMaterial material = materialList.get(j);
                            if(material==null){
                                countInfo.setOtherCount(countInfo.getOtherCount()+1);
                                continue;
                            }
                            switch(material.getMgroup()){
                                case "MAIN ROPE" :
                                    countInfo.setRopeCount(countInfo.getRopeCount()+1);
                                    break;
                                case "MAIN SHEAVE" :
                                    countInfo.setSheaveCount(countInfo.getSheaveCount()+1);
                                    break;
                                case "MAIN INV" :
                                    System.out.println("메인 인버터");
                                    countInfo.setMainInvCount(countInfo.getMainInvCount()+1);
                                    break;
                                case "SAFETY EDGE" :
                                    countInfo.setEdgeCount(countInfo.getEdgeCount()+1);
                                    break;
                                case "DOOR INV" :
                                    countInfo.setDoorInvCount(countInfo.getDoorInvCount()+1);
                                    break;
                            }
                        }
                    }

                } else if(400 == responseCode){
                    return responseTemplate.withBadRequest(request.getUserLocale());
                } else if(500 == responseCode){
                    BufferedReader errorRead = new BufferedReader(new InputStreamReader(http.getErrorStream()));
                    StringBuffer sb = new StringBuffer();
                    String rLine = "";
                    while((rLine = errorRead.readLine()) !=null ){ sb.append(rLine); }
                    return responseTemplate.withSuccess(ResponseBody.builder()
                            .result(ResultCode.SUCCESS.getValue())
                            .data(sb.toString())
                            .build(),request.getUserLocale());
                }
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(countInfo)
                        .build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(new UnitAPI.UnitMaterialsByProjnoCountInfoResponse())
                    .build(),request.getUserLocale());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity materialByProjnos(UnitAPI.UnitMaterialsByProjnoRequest request, HttpServletRequest sRequest){
        // 원프로젝트+호기번호로 빌딩명을 가져온다. 
        // projnos

        List<String> _prjhnos = request.getProjnos();

        if(_prjhnos == null || _prjhnos.isEmpty()) {
            List temp = new ArrayList();
            return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(temp)
                        .build(),request.getUserLocale());
        }

        List<BuldNmVO> _buldNms = ccRepository.getBuldNms(_prjhnos);
        Map<String, BuldNmVO> _buldNmsDic = _buldNms.stream().collect(Collectors.toMap(BuldNmVO::getPrjhno, item -> item));

        // System.out.println("각...."+_buldNmsDic.get("117149L01"));

        UnitAPI.UnitMaterialsByProjnoResponse  resultJson = null;
        URL url = null;
        HttpURLConnection http = null;
        BufferedReader br = null;
        try {
            UnitAPI.UnitMaterialsByProjnoSubRequest params = new UnitAPI.UnitMaterialsByProjnoSubRequest(request);
            url = new URL(unitService1+"?visible=true");
            http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("Content-type", "application/json");
            http.setRequestProperty("Accept", "application/json");

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(http.getOutputStream()));
            bw.write(new Gson().toJson(params));
            bw.flush();
            bw.close();

            br = new BufferedReader(new InputStreamReader(http.getInputStream()));

            int responseCode = http.getResponseCode();
            log.info("Request Info : {}",responseCode);
            if(200 == responseCode){
                StringBuffer sb = new StringBuffer();
                String rLine = "";
                while((rLine = br.readLine()) !=null ){ sb.append(rLine); }
                resultJson = new Gson().fromJson(sb.toString(),UnitAPI.UnitMaterialsByProjnoResponse.class);

                // 빌딩멸 매핑
                if(resultJson.getProjnos() != null && resultJson.getProjnos().size() >0 ) {
                   for(int i=0; i<resultJson.getProjnos().size(); i++) {
                        resultJson.getProjnos().get(i).setBuldNm(_buldNmsDic.get( resultJson.getProjnos().get(i).getProjno()).getBuld_nm());
                   }
                }
            } else if(400 == responseCode){
                return responseTemplate.withBadRequest(request.getUserLocale());
            } else if(500 == responseCode){
                br = new BufferedReader(new InputStreamReader(http.getErrorStream()));
                StringBuffer sb = new StringBuffer();
                String rLine = "";
                while((rLine = br.readLine()) !=null ){ sb.append(rLine); }
                resultJson = new Gson().fromJson(sb.toString(),UnitAPI.UnitMaterialsByProjnoResponse.class);

            }
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(resultJson)
                    .build(),request.getUserLocale());
        } catch (IOException e){
            String errorJson = null;
            try {
                errorJson = new String(http.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);
            } catch (IOException ex) {
                log.error(ex.getMessage(),e);
                throw new RuntimeException(ex.getMessage());
            }
            return ResponseEntity.internalServerError().contentType(MediaType.APPLICATION_JSON).body(errorJson);
        }

    }

    public ResponseEntity getPurchaseList(SCRM.SCRMCurrentPortfolioRequest request){
        try {
            request.setJoin(ccRepository.selectProjnosByPortfolioExtTRLine(request.getUserPortfolioMappingId().toString()));
            List<SCRM.SCRMUnitPurchaseMSTResponse> resultList1 = scrmRepository.selectUnitPurchaseMST(request);
            List<SCRM.SCRMUnitPurchaseMSTResponse> resultList2 = srmRepository.selectUnitPurchaseMST(request);

            List<SCRM.SCRMUnitPurchaseMSTResponse> resultList = ListUtils.sum(resultList1, resultList2);

            if(resultList.size() != 0){
                if(resultList.get(0)==null) resultList = new ArrayList<>();
            }
            return responseTemplate.withSuccess(ResponseBody.builder()
                            .result(ResultCode.SUCCESS.name())
                            .data(resultList)
                    .build(),request.getUserLocale());
        } catch ( Exception e ) {
            log.error(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }
    public ResponseEntity getPurchaseDetailList(SCRM.SCRMCurrentWBSNoRequest request){
        try {
            List<SCRM.SCRMCurrentWBSNoResponse> resultList1 = scrmRepository.selectUnitPurchaseDetail(request);
            List<SCRM.SCRMCurrentWBSNoResponse> resultList2 = srmRepository.selectUnitPurchaseDetail(request);

            List<SCRM.SCRMCurrentWBSNoResponse> resultList = ListUtils.sum(resultList1, resultList2);

            if(resultList.size() != 0){
                if(resultList.get(0)==null) resultList = new ArrayList<>();
            }
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.name())
                    .data(resultList)
                    .build(),request.getUserLocale());
        } catch ( Exception e ) {
            log.error(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }
}
