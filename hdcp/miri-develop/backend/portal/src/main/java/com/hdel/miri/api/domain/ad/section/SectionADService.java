package com.hdel.miri.api.domain.ad.section;

import com.hdel.miri.api.domain.storage.StorageService;
import com.hdel.miri.api.util.request.AbstractRequest;
import com.hdel.miri.api.util.response.Message;
import com.hdel.miri.api.util.response.ResponseBody;
import com.hdel.miri.api.util.response.ResponseTemplate;
import com.hdel.miri.api.util.response.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class SectionADService {
    private final Message message;
    private final ResponseTemplate responseTemplate;

    private final SectionADRepository sectionADRepository;
    private final StorageService storageService;

    public ResponseEntity getList(SectionAD.SectionADSearch request){
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(sectionADRepository.selectList(request))
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }
    public ResponseEntity getImages(AbstractRequest request){
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(sectionADRepository.selectImages())
                    .build(),request.getUserLocale());
        } catch ( Exception e ){

            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }
    public ResponseEntity getDetails(SectionAD.SectionADDetailSearch request){
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(sectionADRepository.selectDetails(request))
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }
    public ResponseEntity insert(SectionAD.SectionADCreate request){
        try {
            if(0 < sectionADRepository.insert(request)){
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

    public ResponseEntity insertDetails(SectionAD.SectionADDetailCreate request, List<MultipartFile> files){
        try {
            request = storageService.save(request,files);
            sectionADRepository.insertDetails(request);
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(Boolean.TRUE)
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            
            throw new RuntimeException(message.becauseFailureInsert(request.getUserLocale()));
        }
    }

    public ResponseEntity update(SectionAD.SectionADUpdate request){
        try {
            if(0 < sectionADRepository.update(request)){
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

    public ResponseEntity active(SectionAD.SectionADActivation request){
        try {
            sectionADRepository.active(request);
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(Boolean.TRUE)
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            
            throw new RuntimeException(message.becauseFailureUpdate(request.getUserLocale()));
        }
    }
    public ResponseEntity inactive(SectionAD.SectionADActivation request){
        try {
            sectionADRepository.inactive(request);
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(Boolean.TRUE)
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            
            throw new RuntimeException(message.becauseFailureUpdate(request.getUserLocale()));
        }
    }

    @Transactional(value = "db2TxManager",propagation = Propagation.NESTED)
    public ResponseEntity remove(SectionAD.SectionADRemove request){
        try {
            //sectionADRepository.deleteDetails(request);
            sectionADRepository.remove(request);
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(Boolean.TRUE)
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            
            throw new RuntimeException(message.becauseFailureDelete(request.getUserLocale()));
        }
    }

    public ResponseEntity deleteDetail(SectionAD.SectionADDetailDelete request){
        try {
            if(0 < sectionADRepository.deleteDetail(request)){
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
