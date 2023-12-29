package com.hdel.miri.api.domain.alarm;

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

import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmSetupService {
    private final Message message;
    private final ResponseTemplate responseTemplate;
    private final AlarmSetupRepository alarmSetupRepository;

    public ResponseEntity getSetupByNotPush(AlarmSetup.AlarmSetupCurrent request){
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(alarmSetupRepository.selectListByNotPush(request))
                    .build(),request.getUserLocale());
        } catch (Exception e ){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }


    }
    public ResponseEntity getSetupByPush(AlarmSetup.AlarmSetupCurrent request){
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(alarmSetupRepository.selectListByPush(request))
                    .build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    @Transactional(value = "db2TxManager",propagation = Propagation.NESTED)
    public ResponseEntity update(AlarmSetup.AlarmSetupUpdate request){
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(alarmSetupRepository.update(request))
                    .build(),request.getUserLocale());
        } catch (Exception e) {
            throw new RuntimeException(message.becauseFailureUpdate(request.getUserLocale()));
        }
    }
}
