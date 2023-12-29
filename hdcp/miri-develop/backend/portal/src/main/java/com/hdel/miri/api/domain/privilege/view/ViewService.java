package com.hdel.miri.api.domain.privilege.view;

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
public class ViewService {
    private final ViewRepository viewRepository;
    private final ResponseTemplate responseTemplate;
    private final Message message;

    public ResponseEntity getList(View.ViewSearch request){
        return responseTemplate.withSuccess(ResponseBody.builder()
                .result(ResultCode.SUCCESS.getValue())
                .data(viewRepository.selectList(request))
                .build(),request.getUserLocale());
    }

    public ResponseEntity insert(View.ViewCreate request){
        if (0 < viewRepository.insert(request)){
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(Boolean.TRUE)
                    .build(),request.getUserLocale());
        } else return responseTemplate.withSuccess(ResponseBody.builder()
                .result(ResultCode.FAILURE.getValue())
                .because(message.becauseNotInsert(request.getUserLocale()))
                .build(),request.getUserLocale());
    }

    public ResponseEntity update(View.ViewUpdate request){
        if(0 < viewRepository.update(request)){
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(Boolean.TRUE)
                    .build(),request.getUserLocale());
        } else return responseTemplate.withSuccess(ResponseBody.builder()
                .result(ResultCode.FAILURE.getValue())
                .because(message.becauseNotInsert(request.getUserLocale()))
                .build(),request.getUserLocale());
    }

    public ResponseEntity delete(View.ViewDelete request){
        try { viewRepository.delete(request);
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(Boolean.TRUE)
                    .build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.becauseNotDelete(request.getUserLocale()));
        }
    }
}
