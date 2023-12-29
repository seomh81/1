package com.hdel.miri.concurrent.global.jwt;

import com.hdel.miri.concurrent.domain.auth.TokenRepository;
import com.hdel.miri.concurrent.global.config.OpenURIConfig;
import com.hdel.miri.concurrent.util.response.ResponseTemplate;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    @Value("${spring.profiles.active}")
    private String profilesActive;
    private final OpenURIConfig openURIConfig;
    private static final String BEARER_HEADER = "Authorization";
    private final TokenProvider tokenProvider;
    private final TokenRepository tokenRepository;
    private final ResponseTemplate responseTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI           = request.getRequestURI();
        String bearer               = request.getHeader(BEARER_HEADER);
        AntPathMatcher pathMatcher  = new AntPathMatcher();
        String resolveAccessToken   = null;

        log.info("\n request URI Info : {} \n Authorization : {} ", requestURI, bearer);

        if ((profilesActive.equals("local")||profilesActive.equals("dev")||profilesActive.equals("prod"))
                && isUris(openURIConfig.getSwaggerURIs(), requestURI,pathMatcher)){
            /** Swagger URIs*/
            filterChain.doFilter(request,response);
            return;
        }
        resolveAccessToken = tokenProvider.resolveToken(bearer);
        if(isUris(openURIConfig.getImagesURIs(), requestURI, pathMatcher)){
            filterChain.doFilter(request,response);
        } else if (isUris(openURIConfig.getConcurrentURI(), requestURI, pathMatcher)){
            /** ConcurrentJob URIs */
            filterChain.doFilter(request, response);
        } else if (isUris(openURIConfig.getOpenAuthURIs(), requestURI, pathMatcher)){
            /** Open URIs
             * SignIn, Find Account, ...
             * */
            if(resolveAccessToken == null){
                filterChain.doFilter(request, response);
            }else responseTemplate.responseWithBadRequest(response);

        } else if (isUris(openURIConfig.getRefreshURI(), requestURI, pathMatcher)){
            /** Refresh URIs */
            responseTemplate.ifElseWithBadRequest(resolveAccessToken != null ,request,response,filterChain);
        } else {
            /**
             * AnyRequest
             * */
            log.info("Any Request.");
            if(resolveAccessToken != null && StringUtils.hasText(resolveAccessToken)){
                try{
                    if (tokenProvider.validateToken(resolveAccessToken)){
                        Authentication authentication = tokenProvider.getAuthenticationByToken(resolveAccessToken);
                        authentication.getAuthorities().stream().forEach(grantedAuthority -> {
                            log.info("MY ROLE : {}",grantedAuthority.getAuthority());
                        });
                        if(authentication.isAuthenticated() &&
                                (0 < tokenRepository.countByUserId(tokenProvider.extractUserId(resolveAccessToken)))){
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            filterChain.doFilter(request,response);
                        } else {
                            responseTemplate.responseWithBadRequest(response);
                        }
                    } else responseTemplate.responseWithBadRequest(response);
                } catch (ExpiredJwtException e){ responseTemplate.responseWithExpiredToken(response); }
            } else responseTemplate.responseWithBadRequest(response);
        }

    }

    /**
     * URI Pattern Check.
     * */
    private boolean isUris(String[] target,String reuqestUri, AntPathMatcher pathMatcher){
        return (0L < Arrays.asList(target).stream().filter(s -> (pathMatcher.matchStart(s, reuqestUri))).count()?true:false);
    }

}
