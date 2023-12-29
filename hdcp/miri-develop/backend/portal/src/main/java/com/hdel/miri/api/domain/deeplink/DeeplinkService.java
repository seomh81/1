package com.hdel.miri.api.domain.deeplink;

import com.hdel.miri.api.domain.auth.AuthVO;
import com.hdel.miri.api.domain.auth.TokenRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeeplinkService {

    private final Message message;
    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;
    private final ResponseTemplate responseTemplate;

    public ResponseEntity verifyToken(AbstractRequest request) {
        RequestAttributes attribs = RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = ((ServletRequestAttributes) attribs).getRequest();
        String accessToken = tokenProvider.resolveToken(req.getHeader("Authorization"));
        try {
            tokenProvider.validateToken(accessToken);
            AuthVO.SignIn user = AuthVO.SignIn.builder().userId(tokenProvider.extractUserId(accessToken)).build();
            if (tokenRepository.countByUserId(user.getUserId()) > 0) {
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(Boolean.TRUE)
                        .build(), request.getUserLocale());
            } else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .data(Boolean.FALSE)
                    .because(message.withRefreshExpiredToken(request.getUserLocale()))
                    .build(), request.getUserLocale());
        } catch (ExpiredJwtException expiredAccess) {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .data(Boolean.FALSE)
                    .because(message.withExpiredToken(request.getUserLocale()))
                    .build(), request.getUserLocale());
        }
    }
}
