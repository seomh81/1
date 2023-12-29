package com.hdel.miri.api.domain.ad.video;

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
public class VideoADService {
    private final Message message;
    private final ResponseTemplate responseTemplate;
    private final VideoADRepository videoADRepository;

    public ResponseEntity getList(VideoAD.VideoADSearch request){
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                            .result(ResultCode.SUCCESS.getValue())
                            .data(videoADRepository.selectList(request))
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity insert(VideoAD.VideoADCreate request){
        try {
            if(0 < videoADRepository.insert(request)){
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
    public ResponseEntity update(VideoAD.VideoADUpdate request){
        try {
            if(0 < videoADRepository.update(request)){
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
    public ResponseEntity active(VideoAD.VideoADActivation request){
        try {
            videoADRepository.active(request);
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(Boolean.TRUE)
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            
            throw new RuntimeException(message.becauseFailureUpdate(request.getUserLocale()));
        }
    }
    public ResponseEntity inactive(VideoAD.VideoADActivation request){
        try {
            videoADRepository.inactive(request);
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(Boolean.TRUE)
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            
            throw new RuntimeException(message.becauseFailureUpdate(request.getUserLocale()));
        }
    }
    public ResponseEntity remove(VideoAD.VideoADRemove request){
        try {
            if(0 < videoADRepository.remove(request)){
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
