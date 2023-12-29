package com.hdel.miri.api.domain.miri;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdel.miri.api.domain.cc.CC;
import com.hdel.miri.api.domain.cc.CCRepository;
import com.hdel.miri.api.domain.hccc.HCCCService;
import com.hdel.miri.api.domain.hrts.HRTS.HRTSConnectionResultVO;
import com.hdel.miri.api.domain.hrts.HRTS.HRTSMonitResultVO;
import com.hdel.miri.api.domain.hrts.HRTS.HRTSRuleVO;
import com.hdel.miri.api.domain.portfolio.PortfolioRepository;
import com.hdel.miri.api.util.response.Message;
import com.hdel.miri.api.util.response.ResponseBody;
import com.hdel.miri.api.util.response.ResponseTemplate;
import com.hdel.miri.api.util.response.ResultCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.DatatypeConverter;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
@RequiredArgsConstructor
public class MIRIService {
    @Value("${api.miri-robot-api}")
    private String miriRobotApi;

    @Value("${api.miri-robot-key}")
    private String miriRobotKey;

    @Value("${api.miri-robot-sec}")
    private String miriRobotSec;

    @Value("${api.miri-call-api}")
    private String miriCallApi;

    @Value("${api.miri-call-key}")
    private String miriCallKey;

    @Value("${api.miri-view-api}")
    private String miriViewApi;

    @Value("${api.miri-view-key}")
    private String miriViewKey;


    private final Message message;
    private final ResponseTemplate responseTemplate;
    private final PortfolioRepository portfolioRepository;
    private final CCRepository ccRepository;

    private String getFormattedDate(String _val) {
            return _val.substring(0, 4) + "-" + _val.substring(4, 6) + "-" + _val.substring(6,8);
    }

    private List<String> getFormattedElevatorNoList(List<String> _val) {

        List<String> rtnVal = _val.stream().map(evNo -> {
            return evNo.substring(0,4) + "-" + evNo.substring(4,7);
        }).collect(Collectors.toList());

        return rtnVal;
    }

    public ResponseEntity getMiriRobotRequestCount(MIRI.MIRIServiceSearch request) {
        try {
            request.setMiriType("robot");
            // 국가 승강기 번호 List를 가져온다. 
            List<String> elGovNoList = portfolioRepository.getElevatorNoByUserPortfolioMappingId(request);

            if(elGovNoList != null && !elGovNoList.isEmpty()) {
                Map<String,Object> params = new LinkedHashMap<>(); // 파라미터 세팅
                params.put("startDate", getFormattedDate(request.getStartDate()));
                params.put("endDate", getFormattedDate(request.getEndDate()));      
                params.put("elGovNoList", getFormattedElevatorNoList(elGovNoList));
                JSONObject reqParams = new JSONObject(params);
    
                final String authorizationHeader = HmacRequestSigner.getHdeAuthorizationHeader(miriRobotKey, miriRobotSec);
                    
                ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
                RestTemplate restTemplate = new RestTemplateBuilder()
                        .requestFactory(() -> requestFactory)
                        .build();
                    
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Authorization", authorizationHeader);
    
                String requestBody = reqParams.toString();
                    
                ResponseEntity<String> response = restTemplate.exchange(miriRobotApi, HttpMethod.POST, new HttpEntity<>(requestBody, headers), String.class);
                
                int _stat = response.getStatusCode().value();
                if (_stat > 299) {
                    System.out.println(_stat + " Error!");
                    throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
                } else {
                    JSONObject jo = new JSONObject(response.getBody());
                    return responseTemplate.withSuccess(ResponseBody.builder()
                                .result(ResultCode.SUCCESS.getValue())
                                .data(MIRI.MIRIServiceResultVO.builder().callCount(""+jo.getInt("callCount")).build())
                                .build()
                            , request.getUserLocale());
                }
            } else {
                return responseTemplate.withSuccess(ResponseBody.builder()
                .result(ResultCode.SUCCESS.getValue())
                .data(MIRI.MIRIServiceResultVO.builder().callCount("미가입").build())
                .build()
            , request.getUserLocale());
            }            
        } catch(Exception ex) {
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity getMiriCallRequestCount(MIRI.MIRIServiceSearch request) {
        try {
            request.setMiriType("call");
            // 국가 승강기 번호 List를 가져온다. 
            List<String> elGovNoList = portfolioRepository.getElevatorNoByUserPortfolioMappingId(request);

            if(elGovNoList != null && !elGovNoList.isEmpty()) {
                Map<String,Object> params = new LinkedHashMap<>(); // 파라미터 세팅
                params.put("startDt", request.getStartDate()+"01");
                params.put("endDt", request.getEndDate()+"31");      
                params.put("elGovNoList", getFormattedElevatorNoList(elGovNoList));
                JSONObject reqParams = new JSONObject(params);
                    
                ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
                RestTemplate restTemplate = new RestTemplateBuilder()
                        .requestFactory(() -> requestFactory)
                        .build();
                    
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setBearerAuth(miriCallKey);

                String requestBody = reqParams.toString();
                    
                ResponseEntity<String> response = restTemplate.exchange(miriCallApi, HttpMethod.POST, new HttpEntity<>(requestBody, headers), String.class);
                
                int _stat = response.getStatusCode().value();
                if (_stat > 299) {
                    System.out.println(_stat + " Error!");
                    throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
                } else {
                    JSONObject jo = new JSONObject(response.getBody());
                    return responseTemplate.withSuccess(ResponseBody.builder()
                                .result(ResultCode.SUCCESS.getValue())
                                .data(MIRI.MIRIServiceResultVO.builder().callCount(""+jo.getInt("requestCount")).build())
                                .build()
                            , request.getUserLocale());
                }
            } else {
                return responseTemplate.withSuccess(ResponseBody.builder()
                .result(ResultCode.SUCCESS.getValue())
                .data(MIRI.MIRIServiceResultVO.builder().callCount("미가입").build())
                .build()
                , request.getUserLocale());
            }
        } catch(Exception ex) {
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }    
    }


    public ResponseEntity getMiriViewRequestCount(MIRI.MIRIServiceSearch request) {
        try {
            request.setMiriType("view");
            // 국가 승강기 번호 List를 가져온다. 
            List<String> elGovNoList = portfolioRepository.getElevatorNoByUserPortfolioMappingId(request);

            if(elGovNoList != null && !elGovNoList.isEmpty()) {
                Map<String,Object> params = new LinkedHashMap<>(); // 파라미터 세팅
                params.put("key", miriViewKey);
                params.put("elNo", elGovNoList);
                JSONObject reqParams = new JSONObject(params);
                    
                ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
                RestTemplate restTemplate = new RestTemplateBuilder()
                        .requestFactory(() -> requestFactory)
                        .build();
                    
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                String requestBody = reqParams.toString();
                    
                ResponseEntity<String> response = restTemplate.exchange(miriViewApi, HttpMethod.POST, new HttpEntity<>(requestBody, headers), String.class);
                
                int _stat = response.getStatusCode().value();
                if (_stat > 299) {
                    System.out.println(_stat + " Error!");
                    throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
                } else {
                    JSONObject jo = new JSONObject(response.getBody());
                    return responseTemplate.withSuccess(ResponseBody.builder()
                                .result(ResultCode.SUCCESS.getValue())
                                .data(MIRI.MIRIServiceResultVO.builder().callCount(jo.getString("status")).build())
                                .build()
                            , request.getUserLocale());
                }
            } else {
                return responseTemplate.withSuccess(ResponseBody.builder()
                .result(ResultCode.SUCCESS.getValue())
                .data(MIRI.MIRIServiceResultVO.builder().callCount("미가입").build())
                .build()
                , request.getUserLocale());
            }
        } catch(Exception ex) {
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    static class HmacRequestSigner {
        // Request Header 해당 형식으로 기입 필요
        private static final String HEADER_FORMAT = "%s apiKey=%s,ts=%s,nonce=%s,signature=%s";

        // HMAC algorithm으로 (HmacMD5, HmacSHA256) 지원하며 원하는 알고리즘 값을 입력
        private static final String HMAC_ALGORITHM = "HmacSHA256";

        // Charset은 UTF-8로 설정
        private static final String UTF_8 = "UTF-8";
        // nonce 입력을 위한 난수 생성기
        private static final SecureRandom secureRandom = new SecureRandom();

        /**
         * HmacRequestSigner을 위한 함수이며, Authorization header 세팅값 return
         * 제공된 API KEY, API SECRET을 세팅한다.
         */
        public static String getHdeAuthorizationHeader(final String id, final String key) throws InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, UnsupportedEncodingException {
            final String data = id;
            long now = System.currentTimeMillis();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            final String timestamp = simpleDateFormat.format(now);
            final String nonce = DatatypeConverter.printHexBinary(generateRandomBytes(16)).toLowerCase(Locale.US);
            final String signature = getSignature(key, data, timestamp, nonce);

            return String.format(HEADER_FORMAT, HMAC_ALGORITHM, id, timestamp, nonce, signature);
        }

        /*
        * 하기의 메소드의 순서대로 signature 생성해야 한다.
        */
        private static String getSignature(final String key, final String data, final String timestamp, final String nonce) throws InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, UnsupportedEncodingException {
            final byte[] keyBytes = DatatypeConverter.parseHexBinary(key);
            final byte[] nonceBytes = DatatypeConverter.parseHexBinary(nonce);
            final byte[] encryptedNonce = encrytAlgorithm(nonceBytes, keyBytes);
            final byte[] encryptedTimestamp = encrytAlgorithm(timestamp.getBytes(UTF_8), encryptedNonce);
            final byte[] signature = encrytAlgorithm(data.getBytes(UTF_8), encryptedTimestamp);

            return DatatypeConverter.printHexBinary(signature).toLowerCase(Locale.US);
        }

        /*
        * 알고리즘 암호화 한다.
        */
        private static byte[] encrytAlgorithm(final byte[] data, final byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
            final Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(key, HMAC_ALGORITHM));

            return mac.doFinal(data);
        }

        /*
        * nonce 값을 세팅하기 위한 난수를 생성한다.
        */
        private static byte[] generateRandomBytes(final int size) {
            final byte[] key = new byte[size];
            secureRandom.nextBytes(key);
            
            return key;
        }
    }

    public ResponseEntity getMIRIPortalLoginData(MIRI.MIRIPortalLoginSearch request){

        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(ccRepository.getMIRIPortalData(request))
                    .build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }
}