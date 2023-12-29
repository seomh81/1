package com.hdel.miri.api.domain.hrts;

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
@RequestMapping("/v2/hrts")
@RequiredArgsConstructor
public class HRTSController {

    private final HRTSService hrtsService;

    @Operation(summary = "평균 운행거리 조회 ( 12개월 )", description = "평균 운행거리 조회 ( 12개월 ) API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerHRTS.GetHRTSLongResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/car/distance/average")
    public ResponseEntity<?> getDistanceAverage(
            @Parameter(description = "Schemas - InspectSearch")
            @RequestBody
            HRTS.HRTSDistanceAvgSearch request
    ) {
        return hrtsService.getDistanceAverage(request);
    }

    @Operation(summary = "AI 진단 마스터 정보 조회", description = "AI 진단 마스터 정보 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerHRTS.GetHRTSMasterJoinListResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/car/master/list")
    public ResponseEntity<?> getSelfInsp(
            @Parameter(description = "Schemas - InspectSearch")
            @RequestBody
            HRTS.HRTSMasterSearch request
            , HttpServletRequest sRequest
    ) throws IOException {
        request.setMobile(DeviceUtils.getCurrentDevice(new RequestWrapper(sRequest)).isMobile());
        return hrtsService.getMasterInfo(request);
    }
    @Operation(summary = "AI 진단 마스터 정보 조회", description = "AI 진단 마스터 정보 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerHRTS.GetHRTSMasterJoinListResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/car/master/list/test")
    public ResponseEntity<?> getSelfInspTest(
            @Parameter(description = "Schemas - InspectSearch")
            @RequestBody
            HRTS.HRTSMasterSearch request
            , HttpServletRequest sRequest
    ) throws IOException {
        request.setMobile(DeviceUtils.getCurrentDevice(new RequestWrapper(sRequest)).isMobile());
        return hrtsService.getMasterInfoTest(request);
    }
    @Operation(summary = "AI 진단 상세 마스터 정보 조회(웹)", description = "AI 진단 상세 마스터 정보 조회 API(웹)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerHRTS.GetHRTSMasterJoinWebResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/manage/master/ai")
    public ResponseEntity<?> getRemoteInspWeb(
            @Parameter(description = "Schemas - HRTSMonitVO")
            @RequestBody
            HRTS.HRTSMonitVO request
    ) {
        return hrtsService.getRemoteInspWeb(request);
    }

    @Operation(summary = "AI 진단 마스터 리스트 조회(웹)", description = "AI 진단 마스터 리스트 조회 API(웹)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerHRTS.GetHRTSRemoteInspectVOListResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/manage/master/list")
    public ResponseEntity<?> getRemoteSelfInspWeb(
            @Parameter(description = "Schemas - HRTSMasterSearchWeb")
            @RequestBody
            HRTS.HRTSMasterSearchWeb request
    )  {
        return hrtsService.getMasterInfoWeb(request);
    }

    @PostMapping("/car/run-info")
    @Operation(summary = "HRTS 운행 정보 보고", description = "HRTS 운행 정보 보고서 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerHRTS.GetHRTSResultRunAvgVOResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity<?> carRunInfo(
            @Parameter(description = "Schemas - HRTSRunAvgSearch 참조")
            @RequestBody
            HRTS.HRTSRunAvgSearch request
    ){
        return hrtsService.getRunInfo(request);
    }
    @PostMapping("/car/run-time/average")
    @Operation(summary = "HRTS 평균 일일 운행시간 조회", description = "HRTS 평균 일일 운행시간 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerHRTS.GetHRTSCurrentMonthRunTimeTargetVOOResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity<?> carRunTimeAverage(
            @Parameter(description = "Schemas - HRTSRunAvgSearch 참조")
            @RequestBody
            HRTS.HRTSRunAvgSearch request
    ){
        return hrtsService.getRunTimeAvgerageCurrentMonth(request);
    }

    @PostMapping("/car/docc-info")
    @Operation(summary = "HRTS 도어개폐 보고", description = "HRTS 도어개폐 보고서 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerHRTS.GetHRTSResultDOCCAvgVOResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity<?> carDOCCInfo(
            @Parameter(description = "Schemas - HRTSRunAvgSearch 참조")
            @RequestBody
            HRTS.HRTSRunAvgSearch request){

        return hrtsService.getDOCCInfo(request);
    }

    @PostMapping("/car/performance-result")
    @Operation(summary = "HRTS 성능 평가 결과(당월)", description = "HRTS 성능 평가 결과(당월) API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerHRTS.GetHRTSPerformanceCheckResultVOResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity<?> performanceResult(
            @Parameter(description = "Schemas - HRTSRunAvgSearch 참조")
            @RequestBody
            HRTS.HRTSRunAvgSearch request){
        return hrtsService.getPerformanceResult(request);
    }

    @PostMapping("/realtime/connect")
    @Operation(summary = "HRTS 실시간 접속", description = "HRTS 실시간 접속 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerHRTS.GetHRTSConnectionResultVOResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity<?> hrtsConnect(
            @Parameter(description = "Schemas - HRTSConnectionVO 참조", required = true)
            @RequestBody
            HRTS.HRTSConnectionVO request){
        return hrtsService.getHrtsConnection(request);
    }

    @PostMapping("/realtime/monit")
    @Operation(summary = "HRTS 실시간 모니터링", description = "HRTS 실시간 모니터링 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerHRTS.GetHRTSMonitResultVOResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity<?> hrtsMonit(
            @Parameter(description = "Schemas - HRTSMonitVO 참조", required = true)
            @RequestBody
            HRTS.HRTSMonitVO request){
        return hrtsService.getHrtsStatus(request);
    }

    @PostMapping("/realtime/rules")
    @Operation(summary = "HRTS 룰 조회", description = "HRTS 룰 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerHRTS.GetHRTSRuleVOResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity<?> getHrtsRules(
            @Parameter(description = "Schemas - HRTSConnectionVO 참조", required = true)
            @RequestBody
            HRTS.HRTSConnectionVO request){
        return hrtsService.getHrtsRules(request);
    }

    @PostMapping("/manage/master/ai/report")
    @Operation(summary = "HRTS 리포트 다운로드", description = "HRTS 리포트 다운로드API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerHRTS.GetHRTSRuleVOResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity<?> getHrtsReport(
            @Parameter(description = "Schemas - HRTSMonitVO 참조", required = true)
            @RequestBody
            HRTS.HRTSReportReqVO request){
        return hrtsService.getHrtsReport(request);
    }
}
