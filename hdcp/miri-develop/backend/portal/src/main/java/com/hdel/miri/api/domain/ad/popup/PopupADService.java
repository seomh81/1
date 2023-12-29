package com.hdel.miri.api.domain.ad.popup;

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
public class PopupADService {
    private final Message message;
    private final ResponseTemplate responseTemplate;
    
    private final PopupADRepository popupADRepository;

    public ResponseEntity getList(PopupAD.PopupADSearch request){
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(popupADRepository.selectList(request))
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity insert(PopupAD.PopupADCreate request){
        try {
            if(0 < popupADRepository.insert(request)){
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(Boolean.TRUE)
                        .build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotInsert(request.getUserLocale()))
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            
            throw new RuntimeException(message.becauseFailureInsert(request.getUserLocale()));
        }


    }
    public ResponseEntity update(PopupAD.PopupADUpdate request){
        try {
            if(0 < popupADRepository.update(request)){
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

    public ResponseEntity active(PopupAD.PopupADActivation request){
        try {
            popupADRepository.active(request);
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(Boolean.TRUE)
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            
            throw new RuntimeException(message.becauseFailureUpdate(request.getUserLocale()));
        }
    }
    public ResponseEntity inactive(PopupAD.PopupADActivation request){
        try {
            popupADRepository.inactive(request);
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(Boolean.TRUE)
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            
            throw new RuntimeException(message.becauseFailureUpdate(request.getUserLocale()));
        }
    }

    public ResponseEntity remove(PopupAD.PopupADRemove request){
        try {
            if(0 < popupADRepository.remove(request)){
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
