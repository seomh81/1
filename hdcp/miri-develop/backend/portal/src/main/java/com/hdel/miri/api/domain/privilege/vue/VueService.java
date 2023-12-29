package com.hdel.miri.api.domain.privilege.vue;

import com.hdel.miri.api.util.response.Message;
import com.hdel.miri.api.util.response.ResponseBody;
import com.hdel.miri.api.util.response.ResponseTemplate;
import com.hdel.miri.api.util.response.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class VueService {
    private final VueRepository vueRepository;
    private final ResponseTemplate responseTemplate;
    private final Message message;

    public ResponseEntity getList(Vue.VueSearch request){
        return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(vueRepository.selectList(request))
                .build(),request.getUserLocale());
    }

    public ResponseEntity insert(Vue.VueCreate request){
        if (0 < vueRepository.insert(request)){
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(Boolean.TRUE)
                    .build(),request.getUserLocale());
        } else return responseTemplate.withSuccess(ResponseBody.builder()
                .result(ResultCode.FAILURE.getValue())
                .because(message.becauseNotInsert(request.getUserLocale()))
                .build(),request.getUserLocale());
    }

    public ResponseEntity update(Vue.VueUpdate request){
        if(0 < vueRepository.update(request)){
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(Boolean.TRUE)
                    .build(),request.getUserLocale());
        } else return responseTemplate.withSuccess(ResponseBody.builder()
                .result(ResultCode.FAILURE.getValue())
                .because(message.becauseNotInsert(request.getUserLocale()))
                .build(),request.getUserLocale());
    }

    public ResponseEntity delete(Vue.VueDelete request){
        if(0 < vueRepository.delete(request)){
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(Boolean.TRUE)
                    .build(),request.getUserLocale());
        } else return responseTemplate.withSuccess(ResponseBody.builder()
                .result(ResultCode.FAILURE.getValue())
                .because(message.becauseNotDelete(request.getUserLocale()))
                .build(),request.getUserLocale());
    }
}
