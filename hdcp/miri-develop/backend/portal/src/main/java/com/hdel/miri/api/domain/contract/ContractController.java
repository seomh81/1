package com.hdel.miri.api.domain.contract;


import com.hdel.miri.api.domain.contract.valid.OnContractDelete;
import com.hdel.miri.api.domain.contract.valid.OnContractInsert;
import com.hdel.miri.api.domain.contract.valid.OnMiriCallSearch;
import com.hdel.miri.api.util.request.AbstractRequest;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import com.hdel.miri.api.util.response.swagger.contract.SwaggerContract;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "계약 API", description = "계약 관련 API")
@RestController
@RequestMapping("/v2/contract")
@RequiredArgsConstructor
public class ContractController {
        private final ContractService contractService;


        @PreAuthorize("isAnonymous()")
        @Operation(summary = "미인증 사용자 계약 조회", description = "미인증 사용자 계약 조회 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContract.GetContractAPIResponse.class))),
                @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/anonymous/search")
        public ResponseEntity<?> anonymousSearch(
                @Parameter(description = "Schemas - ContractSearchAnonymous")
                @RequestBody
                Contract.ContractSearchAnonymous request
        ) {
                return contractService.anonymousContractSearch(request);
        }

        @Operation(summary = "인증 사용자 계약 조회(관리자용)", description = "인증 사용자 계약 조회(관리자용) API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContract.GetContractAPIResponse.class))),
                @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/auth/search")
        public ResponseEntity<?> authSearch(
                @Parameter(description = "Schemas - ContractSearchAnonymous")
                @RequestBody
                Contract.ContractSearchAnonymous request
        ) {
                return contractService.anonymousContractSearch(request);
        }

        @Operation(summary = "사원번호 계약 조회", description = "사원번호 계약 조회 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContract.GetContractEmployeeAPIResponse.class))),
                @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/auth/employee-search")
        public ResponseEntity<?> employeeSearch(
                @Parameter(description = "Schemas - ContractSearchEmployee")
                @RequestBody
                Contract.ContractSearchEmployee request
        ) {
                return contractService.employeeContractSearch(request);
        }


        @Operation(summary = "매니저 계약 조회", description = "매니저 계약 조회 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContract.GetContractListResponse.class))),
                @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/current-manager/contract")
        public ResponseEntity<?> anonymousSearch(
                @Parameter(description = "Schemas - AbstractRequest")
                @RequestBody
                AbstractRequest request
        ) {
                return contractService.currentManagerContractSearch(request);
        }

        @Operation(summary = "내 (사용자) 전체 - 계약 담당자 연락처 조회", description = "내 (사용자) 전체 - 계약 담당자 연락처 조회 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContract.GetContractAPIResponse.class))),
                @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/current/contact/list")
        public ResponseEntity currentContactList(
                @Parameter(description = "Schemas - ContractSearchCurrentUser")
                @RequestBody
                Contract.ContractSearchPortfolio request
        ) {
                return contractService.currentContactList(request);
        }

        @Operation(summary = "내 (사용자) 전체 - 계약 엔지니어 연락처 조회", description = "내 (사용자) 전체 - 계약 엔지니어 연락처 조회 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContract.GetContractAPIResponse.class))),
                @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/current/contact/engineer")
        public ResponseEntity currentContactEngineerList(
                @Parameter(description = "Schemas - ContractSearchCurrentUser")
                @RequestBody
                Contract.ContractSearchPortfolio request
        ) {
                return contractService.currentContactEngineerList(request);
        }

        @Operation(summary = "내 (사용자) 전체 - 계약 상세 리스트 조회", description = "내 (사용자) 전체 - 계약 상세 리스트 조회 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContract.GetContractAPIResponse.class))),
                @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/current/details")
        public ResponseEntity contractDetails(
                @Parameter(description = "Schemas - ContractSearchCurrentUserWithcondition")
                @RequestBody
                Contract.ContractSearchCurrentUserWithCondition request
        ) {
                return contractService.contractDetailsByCurrentUser(request);
        }


        @Operation(summary = "내 (사용자) 동행 일수 조회", description = "내 (사용자) 동행 일수 조회 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContract.ContractIntegerResponse.class))),
                @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/current/companion-days")
        public ResponseEntity companionDays(
                @Parameter(description = "Schemas - ContractSearchCurrentUser")
                @RequestBody
                Contract.ContractSearchCurrentUser request
        ) {
                return contractService.currentCompanionDays(request);
        }

        @Operation(summary = "내 (사용자) 포트폴리오 - 계약 상세 리스트 조회", description = "내 (사용자) 포트폴리오 - 계약 상세 리스트 조회 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContract.GetContractAPIResponse.class))),
                @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/portfolio/details")
        public ResponseEntity contractDetails(
                @Parameter(description = "Schemas - ContractSearchPortfolio")
                @RequestBody
                Contract.ContractSearchPortfolio request
        ) {
                return contractService.contractDetailsByPortfolio(request);
        }

        @Operation(summary = "내 (사용자) 포트폴리오 - 계약정보 요약 조회", description = "내 (사용자) 포트폴리오 - 계약정보 요약 조회 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContract.GetContractAPIResponse.class))),
                @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/portfolio/details/summary")
        public ResponseEntity contractDetailsSummary(
                @Parameter(description = "Schemas - ContractSearchPortfolio")
                @RequestBody
                Contract.ContractSearchPortfolio request
        ) {
                return contractService.contractDetailsByPortfolioSummary(request);
        }

        @Operation(summary = "내 (사용자) 포트폴리오 - 계약 추가", description = "내 (사용자) 포트폴리오 - 계약 추가 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContract.ContractBoolResponse.class))),
                @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/current/insert")
        public ResponseEntity<?> currentInsert(
                @Parameter(description = "Schemas - ContractInsert")
                @RequestBody
                @Validated(value = { OnContractInsert.class })
                Contract.ContractInsert request
        ) {
                return contractService.currentInsert(request);
        }

        @Operation(summary = "내 (사용자) 포트폴리오 - 계약 정보 삭제", description = "내 (사용자) 포트폴리오 - 계약 정보 삭제 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContract.ContractBoolResponse.class))),
                @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/current/delete")
        public ResponseEntity<?> delete(
                @Parameter(description = "Schemas - ContractDelete")
                @RequestBody
                @Validated(value = { OnContractDelete.class })
                Contract.ContractDelete request
        ) {
                return contractService.delete(request);
        }

        @Operation(summary = "관리자가 사용자관리에서 계약 삭제", description = "관리자가 사용자관리에서 계약 삭제 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContract.ContractBoolResponse.class))),
                @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/current/lobby/delete")
        public ResponseEntity<?> removePortfolio(
                @Parameter(description = "Schemas - PortfolioDelete")
                @RequestBody
                @Validated(value = { OnContractDelete.class })
                Contract.ContractDelete request
        ) {
                return contractService.deleteFromLobby(request);
        }

        @Operation(summary = "맵뷰에 미리콜 Deeplink버튼 보일지 여부 가져오기", description = "맵뷰에 미리콜 Deeplink버튼 보일지 여부 가져오기 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContract.GetContractMIRIStatusResponse.class))),
                @ApiResponse(responseCode = "400", description = "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/current/miricall")
        public ResponseEntity<?> isAvailableMiricall(
                @Parameter(description = "Schemas - ContractMiriServiceAPI")
                @RequestBody
                Contract.ContractMiriServiceAPI request
        ) {
                return contractService.isAvailableMiricall(request);
        }

        @Operation(summary = "맵뷰에 미리View Deeplink버튼 보일지 여부 가져오기", description = "맵뷰에 미리View Deeplink버튼 보일지 여부 가져오기 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContract.GetContractMIRIStatusResponse.class))),
                @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/current/miriview")
        public ResponseEntity<?> isAvailableMiriview(
                @Parameter(description = "Schemas - ContractMiriServiceAPI")
                @RequestBody
                Contract.ContractMiriServiceAPI request
        ) {
                return contractService.isAvailableMiriview(request);
        }

        @Operation(summary = "맵뷰에 미리콜 계약유무 가져오기", description = "맵뷰에 미리콜 계약유무 가져오기 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContract.GetContractMIRIStatusResponse.class))),
                @ApiResponse(responseCode = "400", description = "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/current/miricall_yn")
        public ResponseEntity<?> isAvailableMiricall2 (
                @Parameter(description = "Schemas - ContractMiriServiceAPI")
                @RequestBody
                Contract.ContractMiriServiceAPI2 request
        ) {
                return contractService.isAvailableMiricall2(request);
        }

        @Operation(summary = "맵뷰에 미리View Deeplink버튼 보일지 여부 가져오기", description = "맵뷰에 미리View Deeplink버튼 보일지 여부 가져오기 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContract.GetContractMIRIStatusResponse.class))),
                @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/current/miriview_yn")
        public ResponseEntity<?> isAvailableMiriview2 (
                @Parameter(description = "Schemas - ContractMiriServiceAPI")
                @RequestBody
                Contract.ContractMiriServiceAPI2 request
        ) {
                return contractService.isAvailableMiriview2(request);
        }

        @Operation(summary = "사용자의 Lobby에 신규계약을 추가한다.", description = "사용자의 Lobby에 신규계약을 추가하는 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContract.GetContractMIRIStatusResponse.class))),
                @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/current/add")
        public ResponseEntity<?> NewContractInsert(
                @Parameter(description = "Schemas - NewContractInsert")
                @RequestBody
                Contract.NewContractInsert request
        ) {
                return contractService.NewContractInsert(request);
        }

        @Operation(summary = "from 유저의 lobby를 to 유저에게 복사한다.", description = "from 유저의 lobby를 to 유저에게 복사하는 API")
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerContract.GetContractMIRIStatusResponse.class))),
                @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
                @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
        })
        @PostMapping(value = "/copyFromContractTo")
        public ResponseEntity<?> copyFromContractTo(
                @Parameter(description = "Schemas - NewContractInsert")
                @RequestBody
                Contract.ContractCopyListVO request
        ) {
                return contractService.copyFromContractTo(request);
        }
}
