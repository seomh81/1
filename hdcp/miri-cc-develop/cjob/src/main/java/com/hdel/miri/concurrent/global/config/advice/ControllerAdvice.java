package com.hdel.miri.concurrent.global.config.advice;

import com.hdel.miri.concurrent.util.response.ResponseTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.validation.UnexpectedTypeException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {
    private final ResponseTemplate responseTemplate;

    /**
     * Other System Error Logging & Handling.
     * */
    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity handleException(Exception e){
        log.info(e.getMessage(),e);
        return responseTemplate.responseWithInternalError();
    }
    /**
     * 자격 증명 실패 예외처리
     * */
    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity handleException(BadCredentialsException e) {
        return responseTemplate.responseWithBadRequest(LocaleContextHolder.getLocale());
    }
    /**
     * 비인가 예외 처리
     * */
    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity handleException(AccessDeniedException e){
        return responseTemplate.responseWithForbidden();
    }
    /**
     * RequestBody Missing.
     * */
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity handleException(HttpMessageNotReadableException e){
        return responseTemplate.responseWithBadRequest(LocaleContextHolder.getLocale());
    }
    /**
     * RequestBody Missing. - Bad Validate
     * MethodArgumentNotValidException | BindException
     * */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity handleException(MethodArgumentNotValidException e){
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());
        return responseTemplate.responseWithBadValidate(errors);
    }
    @ExceptionHandler(value = {BindException.class})
    public ResponseEntity handleException(BindException e){
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());
        return responseTemplate.responseWithBadValidate(errors);
    }
    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public ResponseEntity handleException(MissingServletRequestParameterException e){
        log.info(e.getMessage());
        return responseTemplate.responseWithBadRequest(LocaleContextHolder.getLocale());
    }
    @ExceptionHandler(value = {UnexpectedTypeException.class})
    public ResponseEntity handleException(UnexpectedTypeException e){
        log.info(e.getMessage());
        return responseTemplate.responseWithBadRequest(LocaleContextHolder.getLocale());
    }



    /**
     * Http Method Missing. - HttpRequestMethodNotSupportedException
     * */
    @ExceptionHandler(value = { HttpRequestMethodNotSupportedException.class })
    public ResponseEntity handleException(HttpRequestMethodNotSupportedException e){
        return responseTemplate.responseWithMethodNotSupported(e);
    }
    /**
     * 첨부파일 확인 필요 - MissingServletRequestPartException
     * */
    @ExceptionHandler(value = { MissingServletRequestPartException.class })
    public ResponseEntity handleException(MissingServletRequestPartException e){
        return responseTemplate.responseWithBadRequest(LocaleContextHolder.getLocale());
    }


}
