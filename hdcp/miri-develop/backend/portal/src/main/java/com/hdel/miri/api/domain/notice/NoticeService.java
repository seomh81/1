package com.hdel.miri.api.domain.notice;

import com.hdel.miri.api.domain.notice.Notice.NoticeSearchMobile;
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
public class NoticeService {
    private final Message message;
    private final ResponseTemplate responseTemplate;
    private final NoticeRepository noticeRepository;

    public ResponseEntity getList(Notice.NoticeSearch request) {
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(noticeRepository.selectList(request))
                    .build(),request.getUserLocale());
        } catch ( Exception e) {
            log.error(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity create(Notice.NoticeCreate request){
        try {
            if(0 < noticeRepository.insert(request)){
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

    public ResponseEntity update(Notice.NoticeUpdate request){
        try {
            if(0 < noticeRepository.update(request)){
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

    public ResponseEntity remove(Notice.NoticeRemove request){
        try {
            if(0 < noticeRepository.remove(request)){
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
