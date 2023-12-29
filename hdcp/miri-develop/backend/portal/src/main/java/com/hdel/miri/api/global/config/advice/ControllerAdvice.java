package com.hdel.miri.api.global.config.advice;

import com.hdel.miri.api.domain.logging.LoggingService;
import com.hdel.miri.api.util.response.ResponseTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.UnexpectedTypeException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvice {
    private final LoggingService loggingService;
    private final ResponseTemplate responseTemplate;
    /**
     * Other System Error Logging & Handling.
     * */
    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity handleException(Exception e, HttpServletRequest request) throws IOException {
        loggingService.error(request,e.getMessage());
        return responseTemplate.withInternalError(request);
    }
    /**
     * Other System Error Logging & Handling.
     * */
    @ExceptionHandler(value = { RuntimeException.class })
    public ResponseEntity handleException(RuntimeException e,HttpServletRequest request) throws IOException {
        loggingService.error(request,e.getMessage());
        return responseTemplate.withInternalError(request);
    }
    /**
     * 자격 증명 실패 예외처리
     * */
    @ExceptionHandler(BadCredentialsException.class)
    protected void handleException(BadCredentialsException e,HttpServletRequest request,HttpServletResponse response) throws IOException {
        loggingService.error(request,e.getMessage());
        responseTemplate.withBadRequest(request,response);
    }
    /**
     * 비인가 예외 처리
     * */
    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity handleException(AccessDeniedException e,HttpServletRequest request) throws IOException {
        loggingService.error(request,e.getMessage());
        return responseTemplate.withForbidden(request);
    }
    /**
     * RequestBody Missing.
     * */
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public void handleException(HttpMessageNotReadableException e,HttpServletRequest request,HttpServletResponse response) throws IOException {
        loggingService.error(request,e.getMessage());
        responseTemplate.withBadRequest(request,response);
    }
    /**
     * RequestBody Missing. - Bad Validate
     * MethodArgumentNotValidException | BindException
     * */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity handleException(MethodArgumentNotValidException e,HttpServletRequest request) throws IOException {
        loggingService.error(request,e.getMessage());
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());
        return responseTemplate.withBadValidate(errors,request);
    }
    @ExceptionHandler(value = {BindException.class})
    public ResponseEntity handleException(BindException e,HttpServletRequest request) throws IOException {
        loggingService.error(request,e.getMessage());
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());
        return responseTemplate.withBadValidate(errors,request);
    }
    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public void handleException(MissingServletRequestParameterException e,HttpServletRequest request,HttpServletResponse response) throws IOException {
        loggingService.error(request,e.getMessage());
        responseTemplate.withBadRequest(request,response);
    }
    @ExceptionHandler(value = {UnexpectedTypeException.class})
    public void handleException(UnexpectedTypeException e,HttpServletRequest request,HttpServletResponse response) throws IOException {
        loggingService.error(request,e.getMessage());
        responseTemplate.withBadRequest(request,response);
    }

    /**
     * Http Method Missing. - HttpRequestMethodNotSupportedException
     * */
    @ExceptionHandler(value = { HttpRequestMethodNotSupportedException.class })
    public ResponseEntity handleException(HttpRequestMethodNotSupportedException e,HttpServletRequest request) throws IOException {
        loggingService.error(request,e.getMessage());
        return responseTemplate.withMethodNotSupported(e);
    }

    /**
     * 첨부파일 확인 필요 - MissingServletRequestPartException
     * */
    @ExceptionHandler(value = { MissingServletRequestPartException.class })
    public void handleException(MissingServletRequestPartException e,HttpServletRequest request,HttpServletResponse response) throws IOException {
        loggingService.error(request,e.getMessage());
        responseTemplate.withBadRequest(request, response);
    }

    /**
     * 데이터 타입 확인 필요 - HttpMediaTypeNotSupportedException
     * */
    @ExceptionHandler(value = { HttpMediaTypeNotSupportedException.class })
    public ResponseEntity handleException(HttpMediaTypeNotSupportedException e,HttpServletRequest request) throws IOException {
        loggingService.error(request,e.getMessage());
        return responseTemplate.withMediaTypeNotSupported(e);
    }



}
