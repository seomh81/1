package com.hdel.miri.api.domain.privilege.role;

import com.hdel.miri.api.util.response.Message;
import com.hdel.miri.api.util.response.ResponseBody;
import com.hdel.miri.api.util.response.ResponseTemplate;
import com.hdel.miri.api.util.response.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final ResponseTemplate responseTemplate;
    private final Message message;

    public ResponseEntity insert(Role.RoleCreate request){
        if (0 < roleRepository.insert(request)){
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(Boolean.TRUE)
                    .build(),request.getUserLocale());
        } else return responseTemplate.withSuccess(ResponseBody.builder()
                .result(ResultCode.FAILURE.getValue())
                .because(message.becauseNotInsert(request.getUserLocale()))
                .build(),request.getUserLocale());
    }

    @Transactional(value = "db2TxManager")
    public ResponseEntity copy(Role.RoleCopy request){
        request.getSrcRoleIds().stream().forEach(roleId -> {
            try {
                HashMap<String, BigDecimal> copy = new HashMap<String, BigDecimal>(){{
                    put("srcRoleId",roleId);
                    put("tgtRoleId",request.getTgtRoleId());
                }};
                roleRepository.copy(copy);
            } catch (Exception e) {
                
                throw new RuntimeException("트랜잭션 실패");
            }
        });
        return responseTemplate.withSuccess(ResponseBody.builder()
                .result(ResultCode.SUCCESS.getValue())
                .data(Boolean.TRUE)
                .build(),request.getUserLocale());

    }

    public ResponseEntity update(Role.RoleUpdate request){
        if(0 < roleRepository.update(request)){
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(Boolean.TRUE)
                    .build(),request.getUserLocale());
        } else return responseTemplate.withSuccess(ResponseBody.builder()
                .result(ResultCode.FAILURE.getValue())
                .because(message.becauseNotUpdate(request.getUserLocale()))
                .build(),request.getUserLocale());
    }

    public ResponseEntity move(Role.RoleMove request){
        if(0 < roleRepository.move(request)){
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(Boolean.TRUE)
                    .build(),request.getUserLocale());
        } else return responseTemplate.withSuccess(ResponseBody.builder()
                .result(ResultCode.FAILURE.getValue())
                .because(message.becauseNotUpdate(request.getUserLocale()))
                .build(),request.getUserLocale());
    }

    public ResponseEntity delete(Role.RoleDelete request){
        try { roleRepository.delete(request);
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(Boolean.TRUE)
                    .build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.becauseNotDelete(request.getUserLocale()));
        }
    }
}
