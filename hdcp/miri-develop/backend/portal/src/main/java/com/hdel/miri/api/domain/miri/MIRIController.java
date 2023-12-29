package com.hdel.miri.api.domain.miri;

import com.hdel.miri.api.util.request.RequestWrapper;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import com.hdel.miri.api.util.response.swagger.hrts.SwaggerHRTS;
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
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * HRTS API.
 * */
@Slf4j
@Tag(name = "HRTS 연계", description = "HRTS 연계 API")
@RestController
@RequestMapping("/v2/miri")
@RequiredArgsConstructor
public class MIRIController {

        private final MIRIService miriService;

        @Operation(summary = "미리 로봇 호출홧수 가져오기", description = "미리 로봇 호출홧수 가져오기 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerHRTS.GetHRTSLongResponse.class))),
                @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/robot/callcount")
        public ResponseEntity<?> getMiriRobotRequestCount(
                @Parameter(description = "Schemas - MIRIServiceSearch")
                @RequestBody
                MIRI.MIRIServiceSearch request
        ) {
                return miriService.getMiriRobotRequestCount(request);
        }

        @Operation(summary = "미리 콜 호출홧수 가져오기", description = "미리 콜 호출홧수 가져오기 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerHRTS.GetHRTSLongResponse.class))),
                @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/call/callcount")
        public ResponseEntity<?> getMiriCallRequestCount(
                @Parameter(description = "Schemas - MIRIServiceSearch")
                @RequestBody
                MIRI.MIRIServiceSearch request
        ) {
                return miriService.getMiriCallRequestCount(request);
        }

        @Operation(summary = "미리 뷰 호출홧수 가져오기", description = "미리 뷰 호출홧수 가져오기 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerHRTS.GetHRTSLongResponse.class))),
                @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/view/callcount")
        public ResponseEntity<?> getMiriViewRequestCount(
                @Parameter(description = "Schemas - MIRIServiceSearch")
                @RequestBody
                MIRI.MIRIServiceSearch request
        ) {
                return miriService.getMiriViewRequestCount(request);
        }

        @Operation(summary = "KPI용 MIRI 포탈 Login Data 조회", description = "KPI용 MIRI 포탈 Login Data 조회 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerHRTS.GetHRTSLongResponse.class))),
                @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/portal/loginData")
        public ResponseEntity<?> getMIRIPortalLoginData(
                @Parameter(description = "Schemas - MIRIPortalLoginSearch")
                @RequestBody
                MIRI.MIRIPortalLoginSearch request
        ) {
                return miriService.getMIRIPortalLoginData(request);
        }
}
