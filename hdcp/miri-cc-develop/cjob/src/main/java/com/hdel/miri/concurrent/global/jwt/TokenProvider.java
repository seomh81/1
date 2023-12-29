package com.hdel.miri.concurrent.global.jwt;

import com.hdel.miri.concurrent.domain.auth.AuthVO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final String BEARER_HEAD = "Bearer ";
    private final long accessTokenExpired = 1000*60*10 ; //2시간 - 변경예정;
    private final long refreshTokenExpired = 1000*60*10; //24시간 - 변경예정;
    private final String secret = "123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789";
    private Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

    private final UserDetailsService userDetailsService;

    /**
     * AccessToken - 2 Hour
     * */
    public String createAccessToken(AuthVO.SignIn vo){
        return Jwts.builder()
                .setSubject(vo.getUserId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+accessTokenExpired))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
    public String createAccessToken(AuthVO.RefreshToken vo){
        return Jwts.builder()
                .setSubject(vo.getUserId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+accessTokenExpired))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
    /**
     * AccessToken - 24 Hour
     * */
    public String createRefreshToken(AuthVO.SignIn vo){
        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+refreshTokenExpired))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Request Header - BearerToken - Resolver.
     * */
    public String resolveToken(String bearer) {
        if(bearer!=null && bearer.contains(BEARER_HEAD) && bearer.startsWith(BEARER_HEAD)){
            bearer = bearer.replace(BEARER_HEAD,"");
            return bearer;
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        }catch (SecurityException e) {
            log.error("Invalid JWT SecurityException.");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT signature.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT Access token.");
            throw e;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.error("Invalid JWT token.");
        }
        return false;
    }

    public Authentication getAuthenticationByToken(String token) {
        try {
            String emailId = extractUserId(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(emailId);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());
            return authentication;
        }catch (io.jsonwebtoken.security.SecurityException ex) {
            log.error("Invalid JWT signature.");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT signature.");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token.");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token.");
        } catch (Exception ex) {
            log.error("Invalid JWT token.");
        }
        return null;
    }

    public AuthVO.Token userTokenBuilder(Authentication authentication) {
        AuthVO.Principal userPrincipal = (AuthVO.Principal) authentication.getPrincipal();

        String accessToken = createAccessToken(AuthVO.SignIn.builder()
                .userId(userPrincipal.getUserId())
                .build());

        String refreshToken = createRefreshToken(AuthVO.SignIn.builder()
                .userId(userPrincipal.getUserId())
                .build());

        return AuthVO.Token.builder()
                .userId(userPrincipal.getUserId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
