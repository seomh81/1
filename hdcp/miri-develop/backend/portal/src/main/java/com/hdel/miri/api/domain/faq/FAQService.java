package com.hdel.miri.api.domain.faq;

import com.hdel.miri.api.util.response.Message;
import com.hdel.miri.api.util.response.ResponseBody;
import com.hdel.miri.api.util.response.ResponseTemplate;
import com.hdel.miri.api.util.response.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class FAQService {
    private final Message message;
    private final ResponseTemplate responseTemplate;
    private final FAQRepository faqRepository;

    public ResponseEntity getList(FAQ.FAQSearchOption request) {
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(faqRepository.selectList(request))
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }

    }

    public ResponseEntity getListMobile(FAQ.FAQSearchOptionMobile request) {
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(faqRepository.selectListMobile(request))
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }

    }

    public ResponseEntity create(FAQ.FAQCreate request){
        try {
            if(0 <faqRepository.insert(request)){
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(Boolean.TRUE)
                        .build(),request.getUserLocale());
            } else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotInsert(request.getUserLocale()))
                    .build(),request.getUserLocale());
        } catch (Exception e){
            throw new RuntimeException(message.becauseFailureInsert(request.getUserLocale()));
        }

    }

    public ResponseEntity update(FAQ.FAQUpdate request){
        try {
            if(0 <faqRepository.update(request)){
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(Boolean.TRUE)
                        .build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotUpdate(request.getUserLocale()))
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            throw new RuntimeException(message.becauseFailureUpdate(request.getUserLocale()));
        }
    }

    public ResponseEntity remove(FAQ.FAQRemove request){
        try {
            if(0 <faqRepository.remove(request)){
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(Boolean.TRUE)
                        .build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotDelete(request.getUserLocale()))
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            throw new RuntimeException(message.becauseFailureDelete(request.getUserLocale()));
        }

    }
}
