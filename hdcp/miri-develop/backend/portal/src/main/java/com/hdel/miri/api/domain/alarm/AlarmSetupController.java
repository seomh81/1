package com.hdel.miri.api.domain.alarm;

import com.hdel.miri.api.domain.alarm.valid.OnAlarmUpdate;
import com.hdel.miri.api.util.response.ResponseFactory;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import com.hdel.miri.api.util.response.swagger.alarm.SwaggerAlarm;
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
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "알람 설정 API", description = "알람 설정 API")
@RestController
@RequestMapping("/v2/alarm-setup")
@RequiredArgsConstructor
public class AlarmSetupController {
    private final AlarmSetupService alarmSetupService;
    @Operation(summary = "사용자의 알람 설정 조회 ( 앱푸쉬 제외 : 웹 전용) 조회", description = "사용자의 알람 설정 조회 ( 앱푸쉬 제외 : 웹 전용 ) 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerAlarm.GetAlarmSetupResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @GetMapping(value = "/current/web")
    public ResponseEntity<?> getListByWeb(
            @Parameter(description = "Schemas - AlarmSetupCurrent")
            AlarmSetup.AlarmSetupCurrent request
    ) {
        return alarmSetupService.getSetupByNotPush(request);
    }

    @Operation(summary = "사용자의 알람 설정 조회 ( 앱푸쉬 전용 : 모바일 전용 ) 조회", description = "사용자의 알람 설정 조회 ( 앱푸쉬 전용 : 모바일 전용 ) 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerAlarm.GetAlarmSetupResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @GetMapping(value = "/current/mobile")
    public ResponseEntity<?> getListByMobile(
            @Parameter(description = "Schemas - AlarmSetupCurrent")
            AlarmSetup.AlarmSetupCurrent request
    ) {
        return alarmSetupService.getSetupByPush(request);
    }

    /**
     * Deprecated 예정 2023 05 19
     * @param request
     * @return
     */
    @Operation(summary = "사용자의 알람 설정 수정", description = "사용자의 알람 설정 수정 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerAlarm.AlarmSetupBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/current/update")
    public ResponseEntity<?> alarmUpdate(
            @Parameter(description = "Schemas - AlarmSetupUpdate")
            @RequestBody
            @Validated(value = { OnAlarmUpdate.class })
            AlarmSetup.AlarmSetupUpdate request
    ) {
        return alarmSetupService.update(request);
    }
}
