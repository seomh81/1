package com.hdel.miri.api.domain.privilege.func;

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
public class FunctionService {
    private final FunctionRepository functionRepository;
    private final ResponseTemplate responseTemplate;
    private final Message message;

    public ResponseEntity getList(FunctionVO.FunctionSearch request){
        return responseTemplate.withSuccess(ResponseBody.builder()
                .result(ResultCode.SUCCESS.getValue())
                .data(functionRepository.selectList(request))
                .build(),request.getUserLocale());
    }

    public ResponseEntity insert(FunctionVO.FunctionCreate request){
        if (0 < functionRepository.insert(request)){
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(Boolean.TRUE)
                    .build(),request.getUserLocale());
        } else return responseTemplate.withSuccess(ResponseBody.builder()
                .result(ResultCode.FAILURE.getValue())
                .because(message.becauseNotInsert(request.getUserLocale()))
                .build(),request.getUserLocale());
    }

    public ResponseEntity update(FunctionVO.FunctionUpdate request){
        if(0 < functionRepository.update(request)){
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(Boolean.TRUE)
                    .build(),request.getUserLocale());
        } else return responseTemplate.withSuccess(ResponseBody.builder()
                .result(ResultCode.FAILURE.getValue())
                .because(message.becauseNotInsert(request.getUserLocale()))
                .build(),request.getUserLocale());
    }

    public ResponseEntity delete(FunctionVO.FunctionDelete request){
        if(0 < functionRepository.delete(request)){
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
