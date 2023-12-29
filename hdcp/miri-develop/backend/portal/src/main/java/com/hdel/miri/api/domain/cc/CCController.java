package com.hdel.miri.api.domain.cc;

import com.hdel.miri.api.domain.contract.Contract;
import com.hdel.miri.api.util.request.RequestWrapper;
import com.hdel.miri.api.util.response.ResponseFactory;
import com.hdel.miri.api.util.response.ResponseTemplate;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import com.hdel.miri.api.util.response.swagger.cc.SwaggerConcurrent;
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

@Slf4j
@Tag(name = "Concurrent 조회 API", description = "Concurrent 조회 API")
@RestController
@RequestMapping("/v2/cc")
@RequiredArgsConstructor
public class CCController {

    private final CCService ccService;

    @Operation(summary = "포트폴리오별 정기점검&정기검사 통합 리스트 조회", description = "포트폴리오별 정기점검&정기검사 통합 리스트 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerConcurrent.GetCCIntegratedInspectVOResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/manage/integrated/insp/list")
    public ResponseEntity<?> getIntegratedInspectionList(
            @Parameter(description = "Schemas - CCCurrentPortfolioRequestExtSearch")
            @RequestBody
            CC.CCCurrentPortfolioRequestExtSearch request
    ) {
        return ccService.getIntegratedInspectionList(request);
    }

    @Operation(summary = "포트폴리오별 정기점검&정기검사 통합 상세 마스터 조회", description = "포트폴리오별 정기점검&정기검사 통합 통합 상세 마스터 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerConcurrent.GetCCIntegratedMasterInfoVOResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/manage/integrated/master")
    public ResponseEntity<?> getIntegratedInspectionDetailMasterInfo(
            @Parameter(description = "Schemas - ElvSearch")
            @RequestBody
            CC.ElvSearch request
    ) {
        return ccService.getIntegratedInspectionDetailMasterInfo(request);
    }

    @Operation(summary = "전체호기 현황 > 점검 현황 조회 (모바일)", description = "전체호기 현황 > 점검 현황 조회 API (모바일)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerConcurrent.GetConcurrentInspTotalResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/car/insp/count-info")
    public ResponseEntity<?> getInspCountTotalMobile(
            @Parameter(description = "Schemas - CCCurrentPortfolioRequest")
            @RequestBody
            CC.CCCurrentPortfolioRequest request
    ) {
        return ccService.getInspCountTotal(request);
    }

    @Operation(summary = "호기별 정기점검 조회 (웹)", description = "호기별 정기점검 조회 API (웹)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerConcurrent.GetConcurrentSelfInspHeadResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/manage/car/self-insp/head")
    public ResponseEntity<?> getSelfInsp(
            @Parameter(description = "Schemas - InspectSearchByPortfolio")
            @RequestBody
            CC.InspectSearchByPortfolio request
    ) {
        return ccService.getSelfInspectManage(request);
    }
    @Operation(summary = "호기별 정기점검 상세 조회 (웹)", description = "호기별 정기점검 상세 조회 API (웹)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerConcurrent.GetConcurrentSelfInspectHistoryVOResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/manage/car/self-insp/details")
    public ResponseEntity<?> getSelfInsp(
            @Parameter(description = "Schemas - InspectSearchBySelfInspectHead")
            @RequestBody
            CC.InspectSearchBySelfInspectHead request
    ) {
        return ccService.getSelfInspectManageDetails(request);
    }

    @Operation(summary = "호기별 정기점검 조회 (모바일)", description = "호기별 정기점검 조회 API (모바일)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerConcurrent.GetConcurrentSelfInspResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/car/self-insp/detail")
    public ResponseEntity<?> getSelfInspMobile(
            @Parameter(description = "Schemas - InspectSearch")
            @RequestBody
            CC.InspectSearch request
            , HttpServletRequest sRequest
    ) throws IOException {
        request.setMobile(DeviceUtils.getCurrentDevice(new RequestWrapper(sRequest)).isMobile());
        return ccService.getSelfInspect(request);
    }

    @Operation(summary = "호기별 정기검사 조회 (웹)", description = "호기별 정기검사 조회 API (웹)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerConcurrent.GetConcurrentInspHeadResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/manage/car/insp/head")
    public ResponseEntity<?> getInsp(
            @Parameter(description = "Schemas - InspectSearchByPortfolio")
            @RequestBody
            CC.InspectSearchByPortfolio request
    ) {
        return ccService.getInspectManage(request);
    }

    @Operation(summary = "호기별 정기검사 상세 조회 (웹)", description = "호기별 정기검사 상세 조회 API (웹)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerConcurrent.GetConcurrentInspectFailItemsResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/manage/car/insp/details")
    public ResponseEntity<?> getInsp(
            @Parameter(description = "Schemas - InspectSearchByInspectHead")
            @RequestBody
            CC.InspectSearchByInspectHead request
    ) {
        return ccService.getInspectManageDetails(request);
    }


    @Operation(summary = "호기별 정기검사 조회 (모바일)", description = "호기별 정기검사 조회 API (모바일)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerConcurrent.GetConcurrentInspResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/car/insp/detail")
    public ResponseEntity<?> getInspMobile(
            @Parameter(description = "Schemas - InspectSearch")
            @RequestBody
            CC.InspectSearch request
            , HttpServletRequest sRequest
    ) throws IOException {
        request.setMobile(DeviceUtils.getCurrentDevice(new RequestWrapper(sRequest)).isMobile());
        return ccService.getInspect(request);
    }

    @Operation(summary = "포트폴리오별 정기점검 카운트 현황 조회", description = "포트폴리오별 정기점검 카운트 현황 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerConcurrent.GetCCDashBoradeSelfInspectCountInfoVOResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/portfolio/self-insp/count-info")
    public ResponseEntity<?> getCalender(
            @Parameter(description = "Schemas - InspectSearchByPortfolioExtMonth")
            @RequestBody
            CC.InspectSearchByPortfolioExtMonth request
    ) {
        return ccService.getSelfInspCountInfoByPortfolio(request);
    }

    @Operation(summary = "내(사용자) 캘린더 조회", description = "내(사용자) 캘린더 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerConcurrent.GetCCCalenderVOListResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/current/calender")
    public ResponseEntity<?> getCalender(
            @Parameter(description = "Schemas - CCCurrentCalenderRequest")
            @RequestBody
            CC.CCCurrentCalenderRequest request
    ) {
        return ccService.getCalender(request);
    }

    @Operation(summary = "내(사용자) 서비스 상세조회", description = "내(사용자) 서비스 상세 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerConcurrent.GetCCServiceVOListResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/current/service")
    public ResponseEntity<?> getServices(
            @Parameter(description = "Schemas - CCServiceRequest")
            @RequestBody
            CC.CCServiceRequest request
    ) {
        return ccService.getServices(request);
    }

    @Operation(summary = "내(사용자) 엘리베이터 리스트 조회", description = "내(사용자) 엘리베이터 리스트 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerConcurrent.GetConcurrentElevatorListResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/current/elevators")
    public ResponseEntity<?> getElevators(
            @Parameter(description = "Schemas - CCCurrentElevatorSearchByPortfolio")
            @RequestBody
            CC.CCCurrentElevatorSearchByPortfolio request
    ) {
        return ccService.getElevators(request);
    }
    @Operation(summary = "내(사용자) 엘리베이터 조회", description = "내(사용자) 엘리베이터 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerConcurrent.GetConcurrentElevatorResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/current/elevator")
    public ResponseEntity<?> getElevator(
            @Parameter(description = "Schemas - CCCurrentElevatorRequest")
            @RequestBody
            CC.CCCurrentElevatorRequest request
    ) {
        return ccService.getElevator(request);
    }

    @Operation(summary = "내(사용자) 엘리베이터 조회", description = "내(사용자) 엘리베이터 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerConcurrent.CCCurrentElevatorChangeDateResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/current/elevator/chanage-date")
    public ResponseEntity<?> getElevatorChangeDate(
            @Parameter(description = "Schemas - CCCurrentElevatorSearchByPortfolio")
            @RequestBody
            CC.CCCurrentElevatorChangeDateRequest request
    ) {
        return ccService.getElevator(request);
    }

    @Operation(summary = "계약 납부상세 조회", description = "계약 납부상세 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerConcurrent.GetCCBillHistoryListResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/bill/history")
    public ResponseEntity<?> getBillHistory(
            @Parameter(description = "Schemas - CCCurrentBillHistoryRequest")
            @RequestBody
            CC.CCCurrentBillHistoryRequest request
    ) {
        return ccService.getBillHistory(request);
    }

        @Operation(summary = "부품 수명에측 트리 조회", description = "부품 수명에측 트리 조회 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerConcurrent.GetCCUnitsTreeResponse.class))),
                @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/units/tree")
        public ResponseEntity<?> getUnitsTree(
                @Parameter(description = "Schemas - CCCurrentPortfolioRequest")
                @RequestBody
                CC.CCCurrentPortfolioRequest request
        ) {
        return ccService.getUnitsTree(request);
        }

        @Operation(summary = "알람전송 이력 조회", description = "알람전송 이력 조회 API, /alarm/search")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerConcurrent.GetCCUnitsTreeResponse.class))),
                @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/alarm/search")
        public ResponseEntity<?> getBillHistory(
                @Parameter(description = "Schemas - CC.AlarmSearchREQ")
                @RequestBody
                CC.AlarmSearchREQ request
        ) {
        return ccService.getAlarmHis(request);
        }

        @Operation(summary = "알람전송 메세지 템플릿 조회", description = "알람전송 메세지 템플릿 조회 API, /alarm/msg-template/search")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerConcurrent.GetCCUnitsTreeResponse.class))),
                @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/alarm/msg-template/search")
        public ResponseEntity<?> getMsgTemplate(
                @Parameter(description = "Schemas - CC.MsgTemplateSearchREQ")
                @RequestBody
                CC.MsgTemplateSearchREQ request
        ) {
        return ccService.getMsgTemplate(request);
        }

        @Operation(summary = "알람전송 메세지 템플릿 수정", description = "알람전송 메세지 템플릿 수정 API, /alarm/msg-template/update")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerConcurrent.GetCCUnitsTreeResponse.class))),
                @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/alarm/msg-template/update")
        public ResponseEntity<?> updateMsgTemplate(
                @Parameter(description = "Schemas - CC.MsgTemplateUpdateREQ")
                @RequestBody
                CC.MsgTemplateUpdateREQ request
        ) {
        return ccService.updateMsgTemplate(request);
        }

        @Operation(summary = "LV2 위젯설정 저장", description = "LV2 위젯설정 저장 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerConcurrent.GetCCUnitsTreeResponse.class))),
                @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/dashboard/widget/update")
        public ResponseEntity<?> updateMsgTemplate(
                @Parameter(description = "Schemas - CC.DashboardWidgetREQ")
                @RequestBody
                CC.DashboardWidgetREQ request
        ) {
        return ccService.updateWidgets(request);
        }

        @Operation(summary = "서비스 요청", description = "서비스 요청 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerConcurrent.GetCCUnitsTreeResponse.class))),
                @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/service/request")
        public ResponseEntity<?> serviceRequest(
                @Parameter(description = "Schemas - CC.CCServiceREQ")
                @RequestBody
                CC.CCServiceREQ request
                ,HttpServletRequest sRequest
        ) throws IOException {
                request.setMobile(DeviceUtils.getCurrentDevice(new RequestWrapper(sRequest)).isMobile());
        return ccService.serviceRequest(request);
        }
}
