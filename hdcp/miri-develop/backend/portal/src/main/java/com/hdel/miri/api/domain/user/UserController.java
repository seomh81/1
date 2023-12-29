package com.hdel.miri.api.domain.user;

import com.hdel.miri.api.domain.user.vo.valid.*;
import com.hdel.miri.api.util.response.ResponseFactory;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import com.hdel.miri.api.util.response.swagger.user.SwaggerUser;
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
import org.springframework.web.bind.annotation.*;

/**
 * User API.
 * */
@Slf4j
@Tag(name = "사용자 API", description = "사용자 관련 API")
@RestController
@RequestMapping("/v2/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 사용자 리스트 조회
     * */
    @Operation(summary = "사용자 리스트 조회", description = "사용자 리스트 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUser.GetUserSummaryResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @GetMapping(value = "/list")
    public ResponseEntity list(
            @Parameter(description = "Schemas - UserSearch 참조")
            User.UserSearch request
    ) { return userService.getUserList(request); }

    /**
     * 사용자 리스트 조회
     * */
    @Operation(summary = "계약 누락 사용자 리스트 조회", description = "계약 누락 사용자 리스트 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUser.GetUserSummaryResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @GetMapping(value = "/nonContractList")
    public ResponseEntity nonContractList(
            @Parameter(description = "Schemas - UserSearch 참조")
            User.UserSearch request
    ) { return userService.getNonContractUserList(request); }

    /**
     * 사용자 정보변경
     * */
    @PostMapping("/update")
    @Operation(summary = "사용자 정보 수정", description = "사용자 정보 수정 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUser.GetUserBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity update(
            @Parameter(description = "Schemas - UserUpdate 참조")
            @Validated(value = { OnChangePassword.class })
            @RequestBody
            User.UserUpdate request){
        return userService.update(request);
    }

    /**
     * 사용자 정보변경 ( 어드민 페이지 용도 )
     * */
    @PostMapping("/manage-update")
    @Operation(summary = "사용자 정보 수정 ( For Admin )", description = "사용자 정보 수정  ( For Admin ) API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUser.GetUserBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity update(
            @Parameter(description = "Schemas - UserUpdateAdminPage 참조")
            @RequestBody
            @Validated(value = {OnUserUpdate.class})
        User.UserUpdateAdminPage request){
        return userService.updateAdminPage(request);
    }

    /**
     * 사용자 정보변경
     * */
    @GetMapping("/confirm-accept")
    @Operation(summary = "사용자 이메일 인증", description = "사용자 이메일 인증")
    public ResponseEntity updateAccept(
            @RequestParam("key") String key
            , @RequestParam("user") String user
    ){
        return userService.updateAccept(key,user);
    }

    /**
     * 사용자 상태 변경
     * */
    @PostMapping("/update/account-status")
    @Operation(summary = "사용자 상태 정보 수정", description = "사용자 상태 정보 수정 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUser.GetUserBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity update(
            @Parameter(description = "Schemas - UserAccountStatus 참조")
            @Validated(value = { OnUserUpdate.class })
            @RequestBody
            User.UserAccountStatus request){
        return userService.updateAccountStatus(request);
    }

    /**
     * 사용자 상태 변경
     * */
    @PostMapping("/update/manage/account-status")
    @Operation(summary = "사용자 상태 정보 수정 (Only Manage)", description = "사용자 상태 정보 수정 (Only Manage) API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUser.GetUserBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity update(
            @Parameter(description = "Schemas - UserAccountStatusRequest 참조")
            @Validated(value = { OnUserUpdate.class })
            @RequestBody
            User.UserAccountStatusRequest request){
        return userService.updateAccountStatus(request);
    }

    /**
     * 사용자 패스워드 변경
     * */
    @PostMapping("/change-password")
    @Operation(summary = "사용자 패스워드 변경", description = "사용자 패스워드 변경 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUser.GetUserSendMailResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity changePassword(
            @Parameter(description = "Schemas - ChangePassword 참조")
            @Validated(value = { OnChangePassword.class })
            @RequestBody
            User.ChangePassword request){
        return userService.changePassword(request);
    }

    /**
     * 사용자 패스워드 초기화
     * */
    
    @PostMapping(value = "/reset-password")
    @Operation(summary = "사용자 패스워드 초기화", description = "사용자 패스워드 초기화 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUser.GetUserSendMailResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity resetPassword(
            @Parameter(description = "Schemas - RestPassword 참조")
            @Validated(value = { OnResetPassword.class })
            @RequestBody User.RestPassword request) {

        return userService.resetPassword(request);

    }
    /**
     * 사용자 계정 찾기
     * */
    @PostMapping(value = "/find-account")
    @Operation(summary = "사용자 계정 찾기", description = "사용자 계정 찾기 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUser.GetUserSendMailResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    public ResponseEntity findAccount(
            @Parameter(description = "Schemas - FindAccount 참조")
            @Validated(value = { OnFindAccount.class })
            @RequestBody User.FindAccount request) {
        return userService.findAccount(request);
    }

    /**
     * 사용자 계정 찾기
     * */
    @Operation(summary = "사용자 아이디 중복 체크", description = "사용자 아이디 중복 체크 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUser.GetUserBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/already")
    public ResponseEntity isAlready(
            @Parameter(description = "Schemas - UserAlready 참조")
            @RequestBody User.UserAlready request) {
        return userService.isAlready(request);
    }

    /**
     * 사용자 계정 찾기
     * */
    @Operation(summary = "사용자 아이디 중복 체크", description = "사용자 아이디 중복 체크 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUser.GetUserAlreadyExtListResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/manage-already")
    public ResponseEntity isAlreadyManage(
            @Parameter(description = "Schemas - MultipleUserAlreadyExt 참조")
            @RequestBody User.MultipleUserAlreadyExt request) {
        return userService.isManageAlready(request);
    }

    /**
     * 사용자 비밀번호 찾기
     * */
    @Operation(summary = "사용자 비밀번호 찾기(초기화)", description = "사용자 비밀번호 찾기(초기화) API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUser.GetUserSendMailResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/find-password")
    public ResponseEntity findPassword(
            @Parameter(description = "Schemas - FindPassword 참조")
            @Validated(value = { OnFindPassword.class })
            @RequestBody User.FindPassword request) {
        return userService.findPassword(request);
    }

    @Operation(summary = "사용자 삭제", description = "사용자 삭제 처리 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUser.GetUserBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/remove")
    public ResponseEntity<?> remove(
            @Parameter(description = "Schemas - UserRemove 참조")
            @RequestBody
            @Validated(value = { OnUserRemove.class})
            User.UserRemove request) {
        return userService.remove(request);
    }

    @Operation(summary = "사용자 계정 복구", description = "사용자 계정 복구 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUser.GetUserBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/recovery")
    public ResponseEntity<?> recovery(
            @Parameter(description = "Schemas - UserRecovery 참조")
            @RequestBody
            @Validated(value = { OnUserRecovery.class})
            User.UserRecovery request) {
        return userService.recovery(request);
    }

    @Operation(summary = "사용자 인증 메일 재발송", description = "사용자 인증 메일 재발송 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUser.GetUserBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/mail/resend")
    public ResponseEntity<?> mailResend(
            @Parameter(description = "Schemas - UserRecovery 참조")
            @RequestBody
            @Validated(value = { OnUserRecovery.class})
            User.UserRecovery request) {
        return userService.mailResend(request);
    }
    
    @PreAuthorize("hasAnyAuthority({'SYSTEM'})")
    @Operation(summary = "시스템 어드민 가입", description = "시스템 어드민 가입 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUser.GetUserBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/join/systemadmin")
    public ResponseEntity joinSystemAdmin (
        @Parameter(description = "Schemas - DefaultOtherUserCreate 참조")
        @Validated(value = { OnUserCreate.class })
        @RequestBody
        User.DefaultOtherUserCreate request
    ) {
        return userService.joinSystemAdmin(request);
    }

    @Operation(summary = "일반사용자 가입", description = "일반사용자 가입 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUser.GetUserBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/join/user")
    public ResponseEntity joinUser (
            @Parameter(description = "Schemas - DefaultOtherUserCreate 참조")
            @Validated(value = { OnUserCreate.class })
            @RequestBody
            User.DefaultOtherUserCreate request
    ) {
        return userService.joinUser(request);
    }

    @Operation(summary = "건물 관리자 가입", description = "건물 관리자 가입 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUser.GetUserBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/join/manager")
    public ResponseEntity joinManager (
            @Parameter(description = "Schemas - DefaultUserCreate 참조")
            @Validated(value = { OnUserCreate.class })
            @RequestBody
            User.DefaultUserCreate request
    ) {
        return userService.joinManager(request);
    }

    @Operation(summary = "메이저 고객 가입", description = "메이저 / 매니저 고객 영업사원에 의한 가입 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUser.GetUserBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/join/major")
    public ResponseEntity joinMajor (
            @Parameter(description = "Schemas - DefaultUserCreate 참조")
            @Validated(value = { OnUserCreate.class })
            @RequestBody
            User.DefaultOtherUserCreate request
    ) {
        return userService.joinMajor(request);
    }

    @Operation(summary = "엔지니어 고객 가입", description = "엔지니어 고객 가입 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUser.GetUserBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/join/engineer")
    public ResponseEntity joinEngineer (
            @Parameter(description = "Schemas - DefaultUserCreate 참조")
            @Validated(value = { OnUserCreate.class })
            @RequestBody
            User.DefaultOtherUserCreate request
    ) {
        return userService.joinEngineer(request);
    }

    @Operation(summary = "내부직원 고객 가입", description = "내부직원 고객 가입 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUser.GetUserBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/join/account")
    public ResponseEntity joinAccount (
            @Parameter(description = "Schemas - DefaultUserCreate 참조")
            @Validated(value = { OnUserCreate.class })
            @RequestBody
            User.DefaultOtherUserCreate request
    ) {
        return userService.joinAccount(request);
    }

    @Operation(summary = "건물관리인 고객 대량 가입", description = "메이저 / 매니저 고객 영업사원에 의한 대량 가입 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUser.GetUserBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/join/many")
    public ResponseEntity joinMany (
            @Parameter(description = "Schemas - DefaultUserCreate 참조")
            @Validated(value = { OnUserCreate.class })
            @RequestBody
            User.DefaultOtherUserCreateList request
    ) {
        return userService.joinMany(request);
    }

    @Operation(summary = "건물관리인 계정 자동 등록", description = "건물 관리인 고객 자동 가입 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUser.GetUserBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/join/autoManager")
    public ResponseEntity joinAutoManager (
            @Parameter(description = "Schemas - DefaultUserCreate 참조")
            @Validated(value = { OnUserCreate.class })
            @RequestBody
            User.DefaultOtherUserCreateAuto request
    ) {
        return userService.joinAutoManager(request);
    }

    @Operation(summary = "계약 누락 고객 대량 맵핑", description = "계약 누락 고객 대량 맵핑 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUser.GetUserBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/setNonContractUser")
    public ResponseEntity setNonContractUser (
            @Parameter(description = "Schemas - setNonContractUserList 참조")
            @RequestBody
            User.setNonContractUserList request
    ) {
        return userService.mappingMany(request);
    }

    @Operation(summary = "현재 사용자 메뉴 정보 조회", description = "현재 사용자 메뉴 정보 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerUser.GetUserViewRenderingInfoResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/current/rendering")
    public ResponseEntity currentRenderingInfo (
            @Parameter(description = "Schemas - UserRenderingRequest 참조")
            @RequestBody
            User.UserRenderingRequest request
    ) {
        return userService.currentViewRendering(request);
    }
}
