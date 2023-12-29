package com.hdel.miri.concurrent.domain.auth;

import com.hdel.miri.concurrent.domain.auth.valid.OnRefreshToken;
import com.hdel.miri.concurrent.domain.auth.valid.OnSignIn;
import com.hdel.miri.concurrent.util.request.AbstractRequest;
import com.hdel.miri.concurrent.util.response.ResponseFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Auth API.
 * */
@Slf4j
@Tag(name = "사용자 인증 API", description = "사용자 인증 관련 API")
@RestController
@RequestMapping("/v2/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    /**
     * Jasypt Test Value.
     * */
    @Value("${spring.app.test.string1}")
    private String ENC_TEST_VALUE;

    /**
     * JWT Token 발급 사용자 인증
     * */
    @PostMapping("/signin")
    @Operation(summary = "사용자 로그인", description = "사용자 로그인 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 에러", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class)))
    })
    public ResponseEntity signIn(
            @Parameter(description = "Schemas - SignIn 참조", required = true)
            @Validated(value = {OnSignIn.class})
            @RequestBody AuthVO.SignIn request){
        log.info("request Info : {}",request.getCurrentUser());
        return authService.signin(request);
    }

    /**
     * JWT Token 발급 사용자 인증
     * */
    @PostMapping("/refresh")
    @Operation(summary = "Access Token 재발급", description = "Access Token 재발급 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 에러", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class)))
    })
    public ResponseEntity refresh(
            @Parameter(description = "Schemas - Refresh 참조")
            @Validated(value = { OnRefreshToken.class })
            @RequestBody
            AuthVO.RefreshToken vo){
        return authService.refresh(vo);
    }

    /**
     * JWT Token 발급 사용자 인증 해제
     * */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/signout")
    @Operation(summary = "사용자 로그아웃", description = "사용자 로그아웃 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 에러", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class)))
    })
    public ResponseEntity signOut(
            @Parameter(description = "Schemas - AbstractRequest 참조")
            @RequestBody
            AbstractRequest request){
        return authService.signOut(request);
    }

    /**
     * My Info
     * */
    @PostMapping("/my")
    @Operation(summary = "나의 정보", description = "나의 정보 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 에러", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseFactory.Response.class)))
    })
    public ResponseEntity my(
            @Parameter(description = "Schemas - AbstractRequest")
            AbstractRequest request
    ){
        return authService.myInfo(request);
    }

    /**
     * Test
     * */
    @PreAuthorize("hasAnyAuthority({'USER','SYSTEM'})")
    @GetMapping("/test")
    public ResponseEntity test(
            AbstractRequest request){
        log.info("Test  : {} ",request.getCurrentUser());
        return ResponseEntity.ok("");
    }
}
