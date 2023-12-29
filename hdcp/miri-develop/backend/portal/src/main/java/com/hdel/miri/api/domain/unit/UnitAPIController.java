package com.hdel.miri.api.domain.unit;

import com.hdel.miri.api.domain.scrm.SCRM;
import com.hdel.miri.api.util.request.AbstractRequest;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import com.hdel.miri.api.util.response.swagger.scrm.SwaggerSCRM;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Tag(name = "부품 관련 API", description = "부품 관련 API")
@RestController
@RequestMapping("/v2/unit-service")
@RequiredArgsConstructor
public class UnitAPIController {

    private final UnitAPIService unitAPIService;

    @PostMapping("/material/predict/projnos")
    @Operation(summary = "[01] 프로젝트 번호 기반 부품 교체 주기 데이터 조회", description = "[01] 프로젝트 번호 기반 부품 교체 주기 데이터 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerSetup.SetupBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity materialByProjnos(
            @Parameter(ref = "Schemas 참조 - UnitAPI.UnitMaterialsByProjnoRequest")
            @RequestBody UnitAPI.UnitMaterialsByProjnoRequest request, HttpServletRequest sRequest
            ){
        return unitAPIService.materialByProjnos(request,sRequest);
    }

    @PostMapping("/material/count-info")
    @Operation(summary = "부품 카운트 현황 조회", description = "부품 카운트 현황 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerSetup.SetupBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity materialByProjnosCountInfo(
            @Parameter(ref = "Schemas 참조 - UnitMaterialsByProjnoRequest")
            @RequestBody UnitAPI.UnitMaterialsByProjnoCountInfoRequest request, HttpServletRequest sRequest
    ){
        return unitAPIService.materialByProjnosCountInfo(request,sRequest);
    }

    @PostMapping("/purchase/list")
    @Operation(summary = "부품 구매 이력 리스트 조회", description = "부품 구매 이력 리스트 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerSCRM.SCRMUnitPurchaseMSTResponseListResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity getPurchaseList(
            @Parameter(description = "Schemas - SCRMCurrentPortfolioRequest 참조")
            @RequestBody
            SCRM.SCRMCurrentPortfolioRequest request
    ){
        return unitAPIService.getPurchaseList(request);
    }

    @PostMapping("/purchase/history")
    @Operation(summary = "부품 구매 이력 상세 부품정보 조회", description = "부품 구매 이력 상세 부품정보 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerSCRM.SCRMUnitPurchaseMSTResponseListResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity getPurchaseDetailList(
            @Parameter(description = "Schemas - SCRMCurrentPortfolioRequest 참조")
            @RequestBody
            SCRM.SCRMCurrentWBSNoRequest request
    ){
        return unitAPIService.getPurchaseDetailList(request);
    }
}
