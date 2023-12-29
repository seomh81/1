package com.hdel.miri.concurrent.domain.auth;

import com.hdel.miri.concurrent.global.jwt.TokenProvider;
import com.hdel.miri.concurrent.util.request.AbstractRequest;
import com.hdel.miri.concurrent.util.response.ResponseBody;
import com.hdel.miri.concurrent.util.response.ResponseTemplate;
import com.hdel.miri.concurrent.util.response.ResultStatus;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final ResponseTemplate responseTemplate;
    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;
    private final AuthRepository authRepository;
    private final AuthenticationManager authenticationManager;
    private final MessageSource messageSource;

    /**
     * SignIn
     * */
    public ResponseEntity<?> signin(AuthVO.SignIn signIn) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signIn.getUserId(),
                signIn.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        AuthVO.Token tokenVo = tokenProvider.userTokenBuilder(authentication);

        tokenRepository.merge(tokenVo);

        return responseTemplate.responseWithAccessToken(
                ResponseBody.builder()
                        .result(ResultStatus.SUCCESS.getStatus())
                        .data(AuthVO.Token.builder()
                                .accessToken(tokenVo.getAccessToken())
                                .refreshToken("Not Provided.")
                                .userId(tokenVo.getUserId())
                                .build()).build());

    }

    /**
     * Refresh Access Token.
     * */
    public ResponseEntity refresh(AuthVO.RefreshToken vo){
        RequestAttributes attribs = RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = ((ServletRequestAttributes) attribs).getRequest();
        String accessToken = tokenProvider.resolveToken(req.getHeader("Authorization"));
        log.info("refresh Service : {}", accessToken);
        try {
            tokenProvider.validateToken(accessToken);
        } catch (ExpiredJwtException expiredAccess){
            if(tokenRepository.countByUserId(vo.getUserId()) > 0){
                AuthVO.Token tokenVo = tokenRepository.selectOne(vo.getUserId());
                try{
                    if(tokenProvider.validateToken(tokenVo.getRefreshToken())){
                        return responseTemplate.responseWithAccessToken(
                                ResponseBody.builder()
                                        .result(ResultStatus.SUCCESS.getStatus())
                                        .data(AuthVO.Token.builder()
                                                .accessToken(tokenProvider.createAccessToken(vo))
                                                .refreshToken("Not Provided.")
                                                .userId(tokenVo.getUserId())
                                                .build()).build());
                    }
                }catch (ExpiredJwtException expiredRefresh){
                    return responseTemplate.responseWithRequireSignIn();
                }
            }
        }
        return responseTemplate.responseWithAlreadyToken();
    }

    /**
     * Sign Out.
     * */
    public ResponseEntity signOut(AbstractRequest request){
        log.info("signOut Service ..");
        RequestAttributes attribs = RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = ((ServletRequestAttributes) attribs).getRequest();
        String accessToken = tokenProvider.resolveToken(req.getHeader("Authorization"));
        try {
            tokenProvider.validateToken(accessToken);
            AuthVO.SignIn user = AuthVO.SignIn.builder().userId(tokenProvider.extractUserId(accessToken)).build();
            if(tokenRepository.countByUserId(user.getUserId()) > 0){
                if(tokenRepository.delete(user.getUserId()) > 0){
                    SecurityContextHolder.clearContext();
                    return responseTemplate.responseWithSignOut(ResponseBody.builder().result(ResultStatus.SUCCESS.getStatus()).data(Boolean.TRUE).build(),new Locale(request.getUserLocale()));
                }
            }
        }catch (ExpiredJwtException expiredAccess){
            return responseTemplate.responseWithExpiredToken(new Locale(request.getUserLocale()));
        }
        return responseTemplate.responseWithBadRequest(new Locale(request.getUserLocale()));
    }

    /**
     * My Info.
     * */
    public ResponseEntity myInfo(AbstractRequest request){
        try {
            return responseTemplate.responseWithSuccess(ResponseBody.builder()
                            .result(ResponseBody.ResultEnum.SUCCESS.name())
                            .data(authRepository.selectByCurrentUser(request))
                    .build());
        } catch ( Exception e ) {
            log.info(e.getMessage(),e);
            return responseTemplate.responseWithSuccess(ResponseBody.builder()
                            .result(ResponseBody.ResultEnum.FAILURE.name())
                            .because(messageSource.getMessage("because.fail.select",null,new Locale(request.getUserLocale())))
                    .build());
        }
    }

}
