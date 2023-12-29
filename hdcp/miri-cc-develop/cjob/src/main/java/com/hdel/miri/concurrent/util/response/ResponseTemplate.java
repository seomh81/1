package com.hdel.miri.concurrent.util.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Locale;


@Slf4j
@Component
public class ResponseTemplate {
    @Resource
    private MessageSource source;
    private static MessageSource messageSource;

    @PostConstruct
    public void initialize() {
        messageSource = source;
    }


    /**
     * 조건에 충족하지 않을 때 BadRequest 응답 :: 필터 전용
     * */
    public static void ifElseWithBadRequest(boolean result, HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if(result) { filterChain.doFilter(request,response);
        } else responseWithBadRequest(response);
    }
    /**
     * 허용하지 않은 형태가 아닌 범위의 여러가지 형태의 요청이 접근할 경우
     * static Method -> 필터 전용
     * else -> ResponseEntity
     * */
    public ResponseEntity responseWithBadRequest(Locale locale){
        return ResponseFactory.create(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), messageSource.getMessage("m.badrequest",null,locale),null);
    }
    public static void responseWithBadRequest(HttpServletResponse response) throws IOException {
        ResponseFactory.streamToJson(response, HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), messageSource.getMessage("m.badrequest",null,LocaleContextHolder.getLocale()),null);
    }
    /**
     * 토큰 인증이 만료가 확인된 요청이 접근할 경우
     * static Method -> 필터 전용
     * else -> ResponseEntity
     * */
    public ResponseEntity responseWithExpiredToken(Locale locale) {
        return ResponseFactory.create(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase()
                , ResponseBody.builder()
                                .result(ResponseBody.ResultEnum.REFRESH.name())
                                .because(messageSource.getMessage("m.expired.token",null,locale))
                        .build());
    }
    public static void responseWithExpiredToken(HttpServletResponse response) throws IOException {
        ResponseFactory.streamToJson(response, HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase()
                , ResponseBody.builder()
                            .result(ResponseBody.ResultEnum.REFRESH.name())
                            .because(messageSource.getMessage("m.expired.token",null,LocaleContextHolder.getLocale()))
                        .build());
    }


    /**
     * 정상적으로 처리가 완료된 경우 - responseWithSuccess
     * 액세스 토큰이 발행된 경우 - responseWithAccessToken
     * 사용자 인증 해제가 완료된 경우 - responseWithSignOut
     * 사용자가 이미 토큰 발급 받은 경우 ( 액세스 재발급 요청 ) - responseWithAlreadyToken
     * 리프레쉬 토큰이 만료되어 재인증 필요한 경우 - responseWithRequireSignIn
     * 접근 권한이 없는 경우 - responseWithForbidden
     * 서버 내부에서 오류가 발생한 경우 - responseWithInternalError
     * RequestParam | RequestBody 유효성 검증에 실패한 경우 - responseWithBadValidate
     * */
    public ResponseEntity responseWithSuccess(ResponseBody data){
        return ResponseFactory.create(HttpStatus.OK,HttpStatus.OK.value(),messageSource.getMessage("m.success",null,LocaleContextHolder.getLocale()),data);
    }
    public ResponseEntity responseWithAccessToken(ResponseBody data){
        return ResponseFactory.create(HttpStatus.OK,HttpStatus.OK.value(),messageSource.getMessage("m.issuer.accesstoken",null,LocaleContextHolder.getLocale()),data);
    }
    public ResponseEntity responseWithSignOut(ResponseBody data, Locale locale){
        return ResponseFactory.create(HttpStatus.OK,HttpStatus.OK.value(),messageSource.getMessage("m.signout",null,locale),data);
    }
    public ResponseEntity responseWithAlreadyToken(){
        return ResponseFactory.create(HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value(),messageSource.getMessage("m.already.token",null,LocaleContextHolder.getLocale()),null);
    }
    public ResponseEntity responseWithRequireSignIn(){
        return ResponseFactory.create(HttpStatus.UNAUTHORIZED,HttpStatus.UNAUTHORIZED.value(),messageSource.getMessage("m.required.singin",null,LocaleContextHolder.getLocale())
                , ResponseBody.builder()
                                .result(ResponseBody.ResultEnum.PROVE.name())
                                .because(messageSource.getMessage("m.expired.refresh.token",null,LocaleContextHolder.getLocale()))
                        .build());
    }
    public ResponseEntity responseWithForbidden(){
        return ResponseFactory.create(HttpStatus.FORBIDDEN,HttpStatus.FORBIDDEN.value(),messageSource.getMessage("m.unauthorization",null,LocaleContextHolder.getLocale()),null);
    }
    public ResponseEntity responseWithInternalError(){
        return ResponseFactory.create(HttpStatus.INTERNAL_SERVER_ERROR,HttpStatus.INTERNAL_SERVER_ERROR.value(),messageSource.getMessage("m.internal.error",null,LocaleContextHolder.getLocale()),null);
    }
    public ResponseEntity responseWithBadValidate(List<String> errors){
        return ResponseFactory.create(HttpStatus.BAD_REQUEST,HttpStatus.BAD_REQUEST.value(),messageSource.getMessage("m.bad.validate",null,LocaleContextHolder.getLocale()), ResponseBody.builder().result(ResultStatus.FAILURE.getStatus()).because(errors).build());
    }
    public ResponseEntity responseWithMethodNotSupported(HttpRequestMethodNotSupportedException e){
        return ResponseFactory.create(HttpStatus.METHOD_NOT_ALLOWED,HttpStatus.METHOD_NOT_ALLOWED.value(),e.getMessage(), ResponseBody.builder().result(ResultStatus.FAILURE.getStatus()).because(e.getSupportedMethods()).build());
    }


}
