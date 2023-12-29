package com.hdel.miri.api.domain.kakao;

import com.hdel.miri.api.util.request.AbstractRequest;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import com.hdel.miri.api.util.response.swagger.setup.SwaggerSetup;
import com.ibm.db2.cmx.internal.core.HttpRequest;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Tag(name = "KAKAO API", description = "카카오 관련 API")
@RestController
@RequestMapping("/v2/kakao")
@RequiredArgsConstructor
public class KakaoAPIController {

    private final KakaoAPIService kakaoAPIService;

    @GetMapping("/getapi")
    @Operation(summary = "카카오 지도 API 가져오기", description = "카카오 지도 API 가져오기 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerSetup.SetupBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity GetKakaoAPI()
    {
        return kakaoAPIService.GetKakaoAPI();
    }
}
