package com.hdel.miri.api.domain.auth;

import com.hdel.miri.api.domain.auth.AuthVO.UserIdsVO;
import com.hdel.miri.api.domain.auth.AuthVO.ValidUserVO;
import com.hdel.miri.api.domain.setup.SetupRepository;
import com.hdel.miri.api.domain.user.UserRepository;
import com.hdel.miri.api.global.jwt.TokenProvider;
import com.hdel.miri.api.util.request.AbstractRequest;
import com.hdel.miri.api.util.response.Message;
import com.hdel.miri.api.util.response.ResponseBody;
import com.hdel.miri.api.util.response.ResponseTemplate;
import com.hdel.miri.api.util.response.ResultCode;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final ResponseTemplate responseTemplate;
    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;
    private final AuthRepository authRepository;
    private final AuthenticationManager authenticationManager;
    private final SetupRepository setupRepository;
    private final Message message;


    public static boolean isValidEmail(String email) {
        boolean err = false;
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";   
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if(m.matches()) {
            err = true; 
        }
        return err;
    }

    /**
     * SignIn
     * */
    public ResponseEntity<?> signin(AuthVO.SignIn request) {
            try {

                String isNotAllowedUser = authRepository.GetNotAllowStatusUser(request);

                log.info("{}","==========================================:"+request.getUserLocale());
                log.info("{}",message.becauseAlreadyRegisted(request.getUserLocale())); 
                log.info("{}",message.becauseAvailableAccount(request.getUserLocale()));

                request.setUserId(request.getUserId().trim());

                if(!isValidEmail(request.getUserId())) {
                    return responseTemplate.withInValidEmailForamt(ResponseBody.builder()
                        .result(ResultCode.FAILURE.getValue())
                        .data(Boolean.FALSE)
                        .because("이메일 형식을 확인하십시오.")
                        .build());
                }

                if(isNotAllowedUser != null) {
                    switch(isNotAllowedUser) {
                        case "0001":
                            return responseTemplate.withRequireEmailConfirm(
                                ResponseBody.builder()
                                .result(ResultCode.IN_PROGRESS.getValue())
                                .because(message.withFailureAuth(request.getUserLocale())).build(),request.getUserLocale());
                        case "0010":
                            return responseTemplate.withRequireProve(
                                ResponseBody.builder()
                                .result(ResultCode.IN_PROGRESS.getValue())
                                .because(message.withFailureAuth(request.getUserLocale())).build(),request.getUserLocale());
                    }
                }

                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        request.getUserId(),
                        request.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                AuthVO.Token tokenVo = tokenProvider.userTokenBuilder(authentication);
                tokenRepository.merge(tokenVo);

                // 구글 파이어베이스 아이디 Upsert
                if(request.getFirebaseUserId() != null && !"".equals(request.getFirebaseUserId())) authRepository.upsertFirebaseUserId(request);

                return responseTemplate.withAccessToken(
                        ResponseBody.builder()
                                .result(ResultCode.SUCCESS.getValue())
                                .data(AuthVO.Token.builder()
                                        .accessToken(tokenVo.getAccessToken())
                                        .refreshToken("Not Provided.")
                                        .refRenderingType(setupRepository.selectRenderType(request.getUserId()))
                                        .userId(tokenVo.getUserId())
                                        .build()).build(),request.getUserLocale());
            } catch (Exception e ){
                log.error(e.getMessage(),e);
            }
            return responseTemplate.withSuccess(
                    ResponseBody.builder()
                            .result(ResultCode.FAILURE.getValue())
                            .because(message.withFailureAuth(request.getUserLocale())).build(),request.getUserLocale());
    }

    /**
     * Refresh Access Token.
     * */
    public ResponseEntity refresh(AuthVO.RefreshToken request){
        RequestAttributes attribs = RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = ((ServletRequestAttributes) attribs).getRequest();
        String accessToken = tokenProvider.resolveToken(req.getHeader("Authorization"));
        log.info("refresh Service : {}", accessToken);
        try {
            tokenProvider.validateToken(accessToken);
        } catch (ExpiredJwtException expiredAccess){
            if(tokenRepository.countByUserId(request.getUserId()) > 0){
                AuthVO.Token tokenVo = tokenRepository.selectOne(request.getUserId());
                try{
                    if(tokenProvider.validateToken(tokenVo.getRefreshToken())){
                        return responseTemplate.withAccessToken(
                                ResponseBody.builder()
                                        .result(ResultCode.SUCCESS.getValue())
                                        .data(AuthVO.Token.builder()
                                                .accessToken(tokenProvider.createAccessToken(request))
                                                .refreshToken("Not Provided.")
                                                .userId(tokenVo.getUserId())
                                                .build()).build(),request.getUserLocale());
                    }
                }catch (ExpiredJwtException expiredRefresh){
                    return responseTemplate.withRequireSignIn(request);
                }
            }
        }
        return responseTemplate.withAlreadyToken(request);
    }

    /**
     * Sign Out.
     * */
    public ResponseEntity signOut(AbstractRequest request){
        RequestAttributes attribs = RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = ((ServletRequestAttributes) attribs).getRequest();
        String accessToken = tokenProvider.resolveToken(req.getHeader("Authorization"));
        try {
            tokenProvider.validateToken(accessToken);
            AuthVO.SignIn user = AuthVO.SignIn.builder().userId(tokenProvider.extractUserId(accessToken)).build();
            if(tokenRepository.countByUserId(user.getUserId()) > 0){
                if(tokenRepository.delete(user.getUserId()) > 0){
                    SecurityContextHolder.clearContext();
                    return responseTemplate.withSignOut(ResponseBody.builder().result(ResultCode.SUCCESS.getValue()).data(Boolean.TRUE).build(),request.getUserLocale());
                }
            }
        }catch (ExpiredJwtException expiredAccess){
            return responseTemplate.withExpiredToken(request.getUserLocale());
        }
        return responseTemplate.withBadRequest(request.getUserLocale());
    }

    /**
     * My Info.
     * */
    public ResponseEntity myInfo(AbstractRequest request){
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                            .result(ResultCode.SUCCESS.getValue())
                            .data(authRepository.selectByCurrentUser(request))
                    .build(),request.getUserLocale());
        } catch ( Exception e ) {
            return responseTemplate.withSuccess(ResponseBody.builder()
                            .result(ResultCode.FAILURE.getValue())
                            .because(message.becauseFailureSelect(request.getUserLocale()))
                    .build(),request.getUserLocale());
        }
    }

    /**
     * 사용자 대량 등록시 이미 존재하는 아이디인지 검증
     * */
    public ResponseEntity checkAccountValidate(UserIdsVO request){
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                            .result(ResultCode.SUCCESS.getValue())
                            .data(authRepository.checkAccountValidate(request))
                    .build(),request.getUserLocale());
        } catch ( Exception e ) {
            return responseTemplate.withSuccess(ResponseBody.builder()
                            .result(ResultCode.FAILURE.getValue())
                            .because(message.becauseFailureSelect(request.getUserLocale()))
                    .build(),request.getUserLocale());
        }
    }
     /**
     * 계약 누락 고객 맵핑 시 유효성 체크
     * */
    public ResponseEntity checkAccountValidate2(UserIdsVO request){
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                            .result(ResultCode.SUCCESS.getValue())
                            .data(authRepository.checkAccountValidate2(request))
                    .build(),request.getUserLocale());
        } catch ( Exception e ) {
            return responseTemplate.withSuccess(ResponseBody.builder()
                            .result(ResultCode.FAILURE.getValue())
                            .because(message.becauseFailureSelect(request.getUserLocale()))
                    .build(),request.getUserLocale());
        }
    }

}
