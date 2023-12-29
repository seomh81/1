package com.hdel.miri.api.domain.kakao;

import com.google.gson.Gson;
import com.hdel.miri.api.domain.cc.CCRepository;
import com.hdel.miri.api.domain.cc.CC.BuldNmVO;
import com.hdel.miri.api.util.response.Message;
import com.hdel.miri.api.util.response.ResponseBody;
import com.hdel.miri.api.util.response.ResponseTemplate;
import com.hdel.miri.api.util.response.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

import com.hdel.miri.api.domain.cc.CC;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

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
import com.ibm.db2.cmx.internal.core.HttpRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoAPIService {

    private final Message message;
    private final ResponseTemplate responseTemplate;
    private final CCRepository ccRepository;

    @Value("${api.kakao-api}")
    private String kakaoUrl;

    @Value("${api.kakao-key}")
    private String kakaoKey;

    public ResponseEntity GetKakaoAPI(){
        try {
                ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
                RestTemplate restTemplate = new RestTemplateBuilder()
                        .requestFactory(() -> requestFactory)
                        .build();
                    
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "KakaoAK " + kakaoKey);
    
                ResponseEntity<String> response = restTemplate.exchange(kakaoUrl+"?appkey="+kakaoKey, HttpMethod.GET, new HttpEntity<>(headers), String.class);
                
                int _stat = response.getStatusCode().value();
                if (_stat > 299) {
                    System.out.println(_stat + " Error!");
                    return responseTemplate.withSuccess(ResponseBody.builder()
                                    .result(ResultCode.FAILURE.getValue())
                                    .data("KAKAO API ERROR!")
                                    .build(),"ko_kr");
                } else {
                    return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(response.getBody())
                    .build(),"ko_kr");

                }
        } catch(Exception ex) {
            return responseTemplate.withSuccess(ResponseBody.builder()
            .result(ResultCode.FAILURE.getValue())
            .data("KAKAO API ERROR!")
            .build(),"ko_kr");
        }
    }
}
