package com.hdel.miri.api.domain.masterdata;

import com.hdel.miri.api.domain.masterdata.valid.*;
import com.hdel.miri.api.util.response.ResponseFactory;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import com.hdel.miri.api.util.response.swagger.masterdata.SwaggerMasterData;
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
@Tag(name = "마스터데이터 관리", description = "마스터 데이터 관리 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/v2/masterdata")
public class MasterDataController {

    private final MasterDataService masterDataService;

    @Operation(summary = "마스터 키 선택 값으로 디테일 조회", description = "마스터 키 선택 값으로 디테일 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerMasterData.GetMasterDetailAPIResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/head/select")
    public ResponseEntity<?> masterHeadList(
            @Parameter(description = "Schemas - MasterDataSelect 참조")
            @RequestBody
            @Validated(value = { OnMasterDataSelect.class })
            MasterData.MasterDataSelect request) {
        return masterDataService.getDetails(request);
    }

    @Operation(summary = "마스터 데이터 리스트 조회", description = "조건에 맞는 마스터 데이터 리스트를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerMasterData.GetMasterHeadAPIResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @GetMapping(value = "/head/list")
    public ResponseEntity<?> masterHeadList(
            @Parameter(description = "Schemas - MasterDataHeadSearch 참조")
            MasterData.MasterDataHeadSearch request) {
        return masterDataService.getMasterHeadList(request);
    }

    @Operation(summary = "마스터 데이터 항목 조회", description = "조건에 맞는 마스터 데이터 항목을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerMasterData.GetMasterDetailAPIResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @GetMapping(value = "/detail/list")
    public ResponseEntity<?> masterDetailList(
            @Parameter(description = "Schemas - MasterDataDetailSearch 참조")
            @Validated(value = { OnMasterDataDetailSearch.class })
            MasterData.MasterDataDetailSearch request
            ) {
        return masterDataService.getMasterDetailList(request);
    }

    @Operation(summary = "마스터 데이터 리스트 추가", description = "마스터 데이타 리스트를 생성합니다. ")
    @PostMapping(value = "/head/create")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerMasterData.MasterDataBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity<?> createMasterHead(
            @Parameter(description = "Schemas의 MasterDataHeadCreate 참고해주세요.")
            @RequestBody
            @Validated(value = { OnMasterDataHeadCreate.class })
            MasterData.MasterDataHeadCreate request) {
        return masterDataService.createMasterHead(request);
    }

    @Operation(summary = "마스터 데이터 항목 추가", description = "마스터 데이타 항목을 추가 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerMasterData.MasterDataBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/detail/create")
    public ResponseEntity<?> createMasterDetail(
            @Parameter(description = "Schemas의 MasterDataDetailCreate 참고해주세요.")
            @RequestBody
            MasterData.MasterDataDetailCreate request
            ) {
        return masterDataService.createMasterDetail(request);
    }

    @Operation(summary = "마스터 데이터 항목 IMPORT", description = "마스터 데이타 항목을 IMPORT 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerMasterData.MasterDataBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/detail/import")
    public ResponseEntity<?> createMasterDetailImport(
            @Parameter(description = "Schemas의 MasterDataDetailCreate 참고해주세요.")
            @RequestBody
            MasterData.MasterDataDetailImport request
            ) {
        return masterDataService.createMasterDetailImport(request);
    }

    @Operation(summary = "마스터 데이터 리스트 삭제", description = "마스터 데이타 리스트를 삭제합니다. ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerMasterData.MasterDataBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/head/delete")
    public ResponseEntity<?> RemoveMasterHead(
            @Parameter(description = "Schemas의 MasterDataHeadDelete 참고해주세요.")
            @RequestBody
            @Validated(value = { OnMasterDataHeadDelete.class})
            MasterData.MasterDataHeadDelete request) {
        return masterDataService.deleteMasterHead(request);
    }

    @Operation(summary = "마스터 데이터 항목 삭제", description = "마스터 데이타 항목을 삭제 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerMasterData.MasterDataBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/detail/delete")
    public ResponseEntity<?> RemoveMasterDetail(
            @Parameter(description = "Schemas의 MasterDataDetailDelete 참고해주세요.")
            @RequestBody
            @Validated(value = { OnMasterDataDetailDelete.class })
            MasterData.MasterDataDetailDelete request) {
        return masterDataService.deleteMasterDetail(request);
    }

    @Operation(summary = "마스터 데이터 리스트명 변경", description = "마스터 데이타 리스트명을 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerMasterData.MasterDataBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/head/update")
    public ResponseEntity<?> UpdateMasterHead(
            @Parameter(description = "Schemas의 MasterDataHeadUpdate 참고해주세요.")
            @RequestBody
            @Validated(value = { OnMasterDataHeadUpdate.class})
            MasterData.MasterDataHeadUpdate request) {
        return masterDataService.updateMasterHead(request);
    }

    @Operation(summary = "마스터 데이터 항목 수정", description = "마스터 데이타를 수정 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerMasterData.MasterDataBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/detail/update")
    public ResponseEntity<?> UpdateMasterDetail(
            @Parameter(description = "Schemas의 MasterDataDetailUpdate 참고해주세요.")
            @RequestBody
            @Validated(value = { OnMasterDataDetailUpdate.class })
            MasterData.MasterDataDetailUpdate request) {
        return masterDataService.updateMasterDetail(request);
    }

}
