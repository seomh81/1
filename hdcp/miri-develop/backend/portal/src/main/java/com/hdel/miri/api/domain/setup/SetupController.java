package com.hdel.miri.api.domain.setup;

import com.hdel.miri.api.domain.setup.valid.OnChangeLandingPage;
import com.hdel.miri.api.domain.setup.valid.OnChangeLocale;
import com.hdel.miri.api.domain.setup.valid.OnChangeTheme;
import com.hdel.miri.api.util.response.ResponseFactory;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import com.hdel.miri.api.util.response.swagger.setup.SwaggerSetup;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User Setup API.
 * */
@Slf4j
@Tag(name = "사용자 설정 API", description = "사용자 설정 API")
@RestController
@RequestMapping("/v2/setup")
@RequiredArgsConstructor
public class SetupController {

    private final SetupService setupService;

    /**
     * 사용자 테마 변경
     * */
    @PostMapping("/current/change-theme")
    @Operation(summary = "사용자 테마 수정", description = "사용자 테마 수정 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerSetup.SetupBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity update(
            @Parameter(description = "Schemas - SetupThemeUpdate 참조")
            @Validated(value = { OnChangeTheme.class })
            @RequestBody
            Setup.SetupThemeUpdate request
    ){
        return setupService.changeTheme(request);
    }

    /**
     * 사용자 언어 변경
     * */
    @PostMapping("/current/change-locale")
    @Operation(summary = "사용자 언어 수정", description = "사용자 언어 수정 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerSetup.SetupBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity update(
            @Parameter(description = "Schemas - SetupLocaleUpdate 참조")
            @Validated(value = { OnChangeLocale.class })
            @RequestBody
            Setup.SetupLocaleUpdate request
            ){
        return setupService.changeLocale(request);
    }

    /**
     * 사용자 언어 변경
     * */
    @PostMapping("/current/change-landing-page")
    @Operation(summary = "사용자 랜딩 페이지 수정", description = "사용자 랜딩 페이지 수정 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerSetup.SetupBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity update(
            @Parameter(description = "Schemas - SetupLandingPageUpdate 참조")
            @Validated(value = { OnChangeLandingPage.class })
            @RequestBody
            Setup.SetupLandingPageUpdate request
    ){
        return setupService.changeLandingPage(request);
    }
}
