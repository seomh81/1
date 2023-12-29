package com.hdel.miri.api.domain.privilege.policy;

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

import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class PolicyService {

    private final Message message;
    private final PolicyRepository policyRepository;
    private final ResponseTemplate responseTemplate;
    /**
     * View - Functions API.
     * */
    public ResponseEntity getFunctionsByView(AbstractRequest request){
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(policyRepository.selectFunctionsByView())
                    .build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }
    public ResponseEntity insertFunctionsByView(Policy.FunctionsMappedByView request){
        try {
            if(0 < policyRepository.insertFunctionsByView(request)){
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(Boolean.TRUE)
                        .build(),request.getUserLocale());
            } else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotInsert(request.getUserLocale()))
                    .build(),request.getUserLocale());
        } catch (Exception e) {
            throw new RuntimeException(message.becauseFailureInsert(request.getUserLocale()));
        }
    }
    public ResponseEntity deleteFunctionsByView(Policy.FunctionsMappedByView request){
        try {
            if(0 < policyRepository.deleteFunctionsByView(request)){
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(Boolean.TRUE)
                        .build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotDelete(request.getUserLocale()))
                    .build(),request.getUserLocale());
        } catch (Exception e) {
            throw new RuntimeException(message.becauseFailureDelete(request.getUserLocale()));
        }
    }

    /**
     * View - VueFile API.
     * */
    public ResponseEntity linkVueFileByView(Policy.VueFileLink request){
        try {
          if( 0 < policyRepository.updateLinkVueFile(request)) {
              return responseTemplate.withSuccess(ResponseBody.builder()
                      .result(ResultCode.SUCCESS.getValue())
                      .data(Boolean.TRUE)
                      .build(),request.getUserLocale());
          } else return responseTemplate.withSuccess(ResponseBody.builder()
                  .result(ResultCode.FAILURE.getValue())
                  .because(message.becauseNotUpdate(request.getUserLocale()))
                  .build(),request.getUserLocale());
        } catch (Exception e) {
            throw new RuntimeException(message.becauseFailureUpdate(request.getUserLocale()));
        }
    }
    public ResponseEntity unlinkVueFileByView(Policy.VueFileLink request){
        try {
            if( 0 < policyRepository.updateUnlinkVueFile(request)) {
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(Boolean.TRUE)
                        .build(),request.getUserLocale());
            } else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotUpdate(request.getUserLocale()))
                    .build(),request.getUserLocale());
        } catch (Exception e) {
            throw new RuntimeException(message.becauseFailureUpdate(request.getUserLocale()));
        }
    }

    /**
     * Menu - Views API.
     * */
    public ResponseEntity getViewsByMenu(AbstractRequest request){
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(policyRepository.selectViewsByMenu())
                    .build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }
    public ResponseEntity insertViewsByMenu(Policy.ViewsMappedByMenu request){
        try {
            if(0 < policyRepository.insertViewsByMenu(request)){
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(Boolean.TRUE)
                        .build(),request.getUserLocale());
            } else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotInsert(request.getUserLocale()))
                    .build(),request.getUserLocale());
        } catch (Exception e) {
            throw new RuntimeException(message.becauseFailureInsert(request.getUserLocale()));
        }
    }
    public ResponseEntity deleteViewsByMenu(Policy.ViewsMappedByMenu request){
        try {
            if(0 < policyRepository.deleteViewsByMenu(request)){
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(Boolean.TRUE)
                        .build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotDelete(request.getUserLocale()))
                    .build(),request.getUserLocale());
        } catch (Exception e) {
            throw new RuntimeException(message.becauseFailureDelete(request.getUserLocale()));
        }
    }

    /**
     * Role - Users API.
     * */
    public ResponseEntity getUsersByRole(AbstractRequest request){
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(policyRepository.selectUsersByRole())
                    .build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }
    public ResponseEntity insertUsersByRole(Policy.UsersMappedByRole request){
        try {
            if(0 < policyRepository.insertUsersByRole(request)){
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(Boolean.TRUE)
                        .build(),request.getUserLocale());
            } else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotInsert(request.getUserLocale()))
                    .build(),request.getUserLocale());
        } catch (Exception e) {
            throw new RuntimeException(message.becauseFailureInsert(request.getUserLocale()));
        }
    }
    public ResponseEntity deleteUsersByRole(Policy.UsersMappedByRole request){
        try {
            if(0 < policyRepository.deleteUsersByRole(request)){
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(Boolean.TRUE)
                        .build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotDelete(request.getUserLocale()))
                    .build(),request.getUserLocale());
        } catch (Exception e) {
            throw new RuntimeException(message.becauseFailureDelete(request.getUserLocale()));
        }
    }

    /**
     * Role - Menus API.
     * */

    public ResponseEntity getMenusByRole(AbstractRequest request){
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(policyRepository.selectMenusByRole())
                    .build(),request.getUserLocale());
        }catch (Exception e){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }
    public ResponseEntity insertMenusByRole(Policy.MenusMappedByRole request){
        try {
            if(0 < policyRepository.insertMenusByRole(request)){
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(Boolean.TRUE)
                        .build(),request.getUserLocale());
            } else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotInsert(request.getUserLocale()))
                    .build(),request.getUserLocale());
        } catch (Exception e) {
            log.info(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureInsert(request.getUserLocale()));
        }
    }
    public ResponseEntity deleteMenusByRole(Policy.MenusMappedByRole request){
        try {
            if(0 < policyRepository.deleteMenusByRole(request)){
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(Boolean.TRUE)
                        .build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotDelete(request.getUserLocale()))
                    .build(),request.getUserLocale());
        } catch (Exception e) {
            throw new RuntimeException(message.becauseFailureDelete(request.getUserLocale()));
        }
    }
}
