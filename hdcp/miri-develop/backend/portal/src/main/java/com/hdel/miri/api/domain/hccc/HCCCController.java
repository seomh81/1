package com.hdel.miri.api.domain.hccc;

import com.hdel.miri.api.domain.cc.CC;
import com.hdel.miri.api.domain.contract.Contract;
import com.hdel.miri.api.domain.hrts.HRTS;
import com.hdel.miri.api.util.request.RequestWrapper;
import com.hdel.miri.api.util.response.ResponseFactory;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import com.hdel.miri.api.util.response.swagger.hccc.SwaggerHCCC;
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
@Tag(name = "HCCC 연계", description = "HCCC 연계 API")
@RestController
@RequestMapping("/v2/hccc")
@RequiredArgsConstructor
public class HCCCController {

        private final HCCCService hcccService;

        @PostMapping("/current/map-view")
        @Operation(summary = "내 (사용자) 맵뷰 조회", description = "내 (사용자) 맵뷰 조회 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerHCCC.GetHCCCELInfoJoinToHCCCResponse.class))),
                @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        public ResponseEntity getMapView(
                @Parameter(description = "Schemas - CCCurrentPortfolioRequest 참조", required = true)
                @RequestBody
                CC.CCCurrentPortfolioRequest request){
        return hcccService.getELInfoStatus(request);
        }

    @PostMapping("/current/fail-status")
    @Operation(summary = "내 (사용자) 고장처리 현황 조회", description = "내 (사용자) 고장처리 현황 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerHCCC.GetHCCCFailStatusVOResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity getFailStatus(
            @Parameter(description = "Schemas - CCCurrentPortfolioRequest 참조", required = true)
            @RequestBody
            CC.CCCurrentPortfolioRequest request
            , HttpServletRequest sRequest) throws IOException {
        request.setMobile(DeviceUtils.getCurrentDevice(new RequestWrapper(sRequest)).isMobile());
        return hcccService.getFailStatus(request);
    }

    @PostMapping("/current/count-info")
    @Operation(summary = "내 (사용자) 총 대수/고장대수 조회", description = "내 (사용자) 총 대수/고장대수 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerHCCC.GetHCCCCountVOResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity carRunInfo(
            @Parameter(description = "Schemas - ContractSearchPortfolio 참조", required = true)
            @RequestBody
            Contract.ContractSearchPortfolio request){

        return hcccService.getCurrentCountInfo(request);
    }

    @PostMapping("/current/run-avg")
    @Operation(summary = "내 (사용자) EL/ES 가동률 조회", description = "내 (사용자) EL/ES 가동률 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerHCCC.GetHCCCRunAvgVOResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity carRunAvgInfo(
            @Parameter(description = "Schemas - ContractSearchPortfolio 참조", required = true)
            @RequestBody
            Contract.ContractSearchPortfolio request){

        return hcccService.getRunAvg(request);
    }

    @PostMapping("/current/run-avg/period")
    @Operation(summary = "내 (사용자) EL/ES 가동률 조회 (전월, 3개월전, 6개월전)", description = "내 (사용자) EL/ES 가동률 조회 (전월, 3개월전, 6개월전) API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerHCCC.GetHCCCRunAvgVOResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity selectElRunAvgInPeriod(
            @Parameter(description = "Schemas - ContractSearchPortfolio 참조", required = true)
            @RequestBody
            Contract.ContractSearchPortfolioId request){

        return hcccService.selectElRunAvgInPeriod(request);
    }

    @PostMapping("/current/run-count")
    @Operation(summary = "내 (사용자) EL/ES 가동 현황 조회", description = "내 (사용자) EL/ES 가동 현황 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerHCCC.GetHCCCRunAvgVOResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity carRunCountInfo(
            @Parameter(description = "Schemas - ContractSearchPortfolio 참조", required = true)
            @RequestBody
            Contract.ContractSearchPortfolio request){

        return hcccService.getRunCountInfo(request);
    }

    @PostMapping("/current/fail-count")
    @Operation(summary = "내 (사용자) 고장 처리 카운트 정보 조회", description = "내 (사용자) 고장 처리 카운트 정보 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerHCCC.GetHCCCFailCountInfoVOResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity carFailCountInfo(
            @Parameter(description = "Schemas - ContractSearchPortfolio 참조", required = true)
            @RequestBody
            Contract.ContractSearchPortfolioExtendsMonth request){

        return hcccService.getFailCountInfo(request);
    }

    @PostMapping("/current/fail-remarks")
    @Operation(summary = "호기별 고장 특이사항 조회", description = "고장 특이사항 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerHCCC.GetHCCCFailCountInfoVOResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity getFailReamrks(
            @Parameter(description = "Schemas - ContractSearchPortfolio 참조", required = true)
            @RequestBody
            HCCC.HCCCFailRemakrSearchVO request){

        return hcccService.getFailReamrks(request);
    }

    @PostMapping("/current/targetReception")
    @Operation(summary = "고장 이력 별 상세 정보", description = "고장 이력 별 상세 정보 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerHCCC.GetHCCCTargetReceptionInfoVOResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity getTargetReceptionInfo(
            @Parameter(required = true)
            @RequestBody
            HCCC.HCCCtargetReceptionVO request){
        return hcccService.getTargetReception(request);
    }
}
