package com.hdel.miri.api.util.response;

import com.hdel.miri.api.domain.auth.AuthVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@Slf4j
@Component
public class ResponseTemplate {
    @Resource
    private Message msource;
    private static Message message;
    @PostConstruct
    public void initialize() {
        message = msource;
    }

    /**
     * 조건에 충족하지 않을 때 BadRequest 응답 :: 필터 전용
     * */
    public static void ifElseWithBadRequest(boolean result, HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if(result) { filterChain.doFilter(request,response);
        } else withBadRequest(request,response);
    }
    /**
     * 허용하지 않은 형태가 아닌 범위의 여러가지 형태의 요청이 접근할 경우
     * static Method -> 필터 전용
     * else -> ResponseEntity
     * */
    public ResponseEntity withBadRequest(String loc){
        return ResponseFactory.create(HttpStatus.BAD_REQUEST, message.withBadRequest(loc),null);
    }
    public static void withBadRequest(HttpServletRequest request,HttpServletResponse response) throws IOException {
        ResponseFactory.streamToJson(response, HttpStatus.BAD_REQUEST, message.withBadRequest(request),null);
    }

    public static void withUnAuthorized(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseFactory.streamToJson(response, HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getReasonPhrase()
        , ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(new String("Not Authorized"))
                .build());
    }


    public static void withAlreadySignoutedToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseFactory.streamToJson(response, HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getReasonPhrase()
        , ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(new String("Alrady Signouted Token"))
                .build());
    }

    /**
     * 토큰 인증이 만료가 확인된 요청이 접근할 경우
     * static Method -> 필터 전용
     * else -> ResponseEntity
     * */
    public ResponseEntity withExpiredToken(String loc) {
        return ResponseFactory.create(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getReasonPhrase()
                , ResponseBody.builder()
                                .result(ResultCode.REFRESH.getValue())
                                .because(message.withExpiredToken(loc))
                        .build());
    }
    public static void withExpiredToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseFactory.streamToJson(response, HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getReasonPhrase()
                , ResponseBody.builder()
                            .result(ResultCode.REFRESH.getValue())
                            .because(message.withExpiredToken(request))
                        .build());
    }


    /**
     * 정상적으로 처리가 완료된 경우 - withSuccess
     * 액세스 토큰이 발행된 경우 - withAccessToken
     * 사용자 인증 해제가 완료된 경우 - withSignOut
     * 사용자가 이미 토큰 발급 받은 경우 ( 액세스 재발급 요청 ) - withAlreadyToken
     * 리프레쉬 토큰이 만료되어 재인증 필요한 경우 - withRequireSignIn
     * 접근 권한이 없는 경우 - withForbidden
     * 서버 내부에서 오류가 발생한 경우 - withInternalError
     * RequestParam | RequestBody 유효성 검증에 실패한 경우 - withBadValidate
     * 지원하지 않는 메소드로 접근한 경우 - withMethodNotSupported
     * 지원하지 않는 미디어타입으로 접근한 경우 - withMediaTypeNotSupported
     * */
    public ResponseEntity withSuccess(ResponseBody data, String loc){
        return ResponseFactory.create(HttpStatus.OK,message.withSuccess(loc),data);
    }
    public ResponseEntity withInValidEmailForamt(ResponseBody data){
        return ResponseFactory.create(HttpStatus.BAD_REQUEST, "이메일 형식을 확인하십시오.", data);
    }
    public ResponseEntity withAccessToken(ResponseBody data, String loc){
        return ResponseFactory.create(HttpStatus.OK,message.withIssueAccessToken(loc),data);
    }
    public ResponseEntity withSignOut(ResponseBody data, String loc){
        return ResponseFactory.create(HttpStatus.OK,message.withSignOut(loc),data);
    }
    public ResponseEntity withAlreadyToken(AuthVO.RefreshToken request){
        return ResponseFactory.create(HttpStatus.BAD_REQUEST, message.withAlreadyToken(request.getUserLocale()),null);
    }
    public ResponseEntity withRequireProve(ResponseBody data, String loc){
        return ResponseFactory.create(HttpStatus.UNAUTHORIZED, message.withRequiredApprove(loc)
                , data);
    }

    public ResponseEntity withRequireEmailConfirm(ResponseBody data, String loc){
        return ResponseFactory.create(HttpStatus.UNAUTHORIZED, message.withRequiredEmailConfirm(loc)
                , data);
    }

    public ResponseEntity withRequireSignIn(AuthVO.RefreshToken request){
        return ResponseFactory.create(HttpStatus.UNAUTHORIZED, message.withRequiredSignIn(request.getUserLocale())
                , ResponseBody.builder().result(ResultCode.PROVE.getValue())
                        .because(message.withRefreshExpiredToken(request.getUserLocale()))
                        .build());
    }
    public ResponseEntity withForbidden(HttpServletRequest request) throws IOException {
        return ResponseFactory.create(HttpStatus.FORBIDDEN,message.withUnauthorization(request),null);
    }
    public ResponseEntity withInternalError(HttpServletRequest request) throws IOException {
        return ResponseFactory.create(HttpStatus.INTERNAL_SERVER_ERROR,message.withInternalError(request),null);
    }
    public ResponseEntity withBadValidate(List<String> errors,HttpServletRequest request) throws IOException {
        return ResponseFactory.create(HttpStatus.BAD_REQUEST,message.withBadValidate(request), ResponseBody.builder().result(ResultCode.FAILURE.getValue()).because(errors).build());
    }
    public ResponseEntity withMethodNotSupported(HttpRequestMethodNotSupportedException e){
        return ResponseFactory.create(HttpStatus.METHOD_NOT_ALLOWED,e.getMessage(), ResponseBody.builder().result(ResultCode.FAILURE.getValue()).because(e.getSupportedMethods()).build());
    }
    public ResponseEntity withMediaTypeNotSupported(HttpMediaTypeNotSupportedException e){
        return ResponseFactory.create(HttpStatus.UNSUPPORTED_MEDIA_TYPE,e.getMessage(), ResponseBody.builder().result(ResultCode.FAILURE.getValue()).because(e.getSupportedMediaTypes()).build());
    }

}
