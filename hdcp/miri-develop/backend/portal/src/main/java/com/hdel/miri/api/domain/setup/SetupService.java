package com.hdel.miri.api.domain.setup;

import com.hdel.miri.api.util.response.Message;
import com.hdel.miri.api.util.response.ResponseBody;
import com.hdel.miri.api.util.response.ResponseTemplate;
import com.hdel.miri.api.util.response.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SetupService {
    private final SetupRepository setupRepository;
    private final ResponseTemplate responseTemplate;
    private final Message message;

    /***
     * 로케일 변경
     */
    public ResponseEntity changeTheme(Setup.SetupThemeUpdate request){
        try {
            if(0 < setupRepository.updateTheme(request)){
                return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.SUCCESS.getValue()).data(Boolean.TRUE).build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotDelete(request.getUserLocale())).build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.becauseFailureUpdate(request.getUserLocale()));
        }
    }
    /***
     * 로케일 변경
     */
    public ResponseEntity changeLocale(Setup.SetupLocaleUpdate request){
        try {
            if(0 < setupRepository.updateLocale(request)){
                return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.SUCCESS.getValue()).data(Boolean.TRUE).build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotDelete(request.getUserLocale())).build(),request.getUserLocale());
        }catch (Exception e){

            throw new RuntimeException(message.becauseFailureUpdate(request.getUserLocale()));
        }
    }

    /***
     * 로케일 변경
     */
    public ResponseEntity changeLandingPage(Setup.SetupLandingPageUpdate request){
        try {
            if(0 < setupRepository.updateLandingPage(request)){
                return responseTemplate.withSuccess(ResponseBody.builder().result(ResultCode.SUCCESS.getValue()).data(Boolean.TRUE).build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotDelete(request.getUserLocale())).build(),request.getUserLocale());
        }catch (Exception e){

            throw new RuntimeException(message.becauseFailureUpdate(request.getUserLocale()));
        }
    }
}
