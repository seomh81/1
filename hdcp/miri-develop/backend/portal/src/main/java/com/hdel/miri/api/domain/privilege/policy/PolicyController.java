package com.hdel.miri.api.domain.privilege.policy;

import com.hdel.miri.api.domain.privilege.policy.valid.*;
import com.hdel.miri.api.util.request.AbstractRequest;
import com.hdel.miri.api.util.response.ResponseFactory;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import com.hdel.miri.api.util.response.swagger.privilege.policy.SwaggerPrivilegePolicy;
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
@Tag(name = "Privilege 정책 관리", description = "Privilege 정책 관리 API")
@RestController
@RequestMapping("/v2/privilege/policy")
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;

    /**
     * View - Functions API.
     * */
    @Operation(summary = "뷰-기능 매핑 조회", description = "조건에 맞는 대상을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegePolicy.GetPrivilegePolicyFunctionsByViewResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @GetMapping(value = "/view/funcs")
    public ResponseEntity getFunctionsByView(AbstractRequest request) {
        return policyService.getFunctionsByView(request);
    }
    @Operation(summary = "뷰-기능 추가", description = "View 에 Function 을 추가합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegePolicy.PrivilegePolicyBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/view/funcs/create")
    public ResponseEntity createFunctionsByView(
            @Parameter(description = "Schemas - FunctionsMappedByView")
            @RequestBody
            @Validated(value = { OnFunctionsMappedByView.class })
            Policy.FunctionsMappedByView request
    ) {
        return policyService.insertFunctionsByView(request);
    }

    @Operation(summary = "뷰-기능 삭제", description = "View 에 Function 을 삭제 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegePolicy.PrivilegePolicyBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/view/funcs/delete")
    public ResponseEntity deleteFunctionsByView(
            @Parameter(description = "Schemas - FunctionsMappedByView")
            @RequestBody
            @Validated(value = { OnFunctionsMappedByView.class })
            Policy.FunctionsMappedByView request
    ) {
        return policyService.deleteFunctionsByView(request);
    }

    /**
     * View - Vue API.
     * */
    @Operation(summary = "View-Vue file 링크", description = "View 와 VUE 파일을 링크 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegePolicy.PrivilegePolicyBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/view/vue/link")
    public ResponseEntity linkVueFileByView(
            @Parameter(description = "Schemas - VueFileLink")
            @RequestBody
            @Validated(value = { OnVueFileLink.class })
            Policy.VueFileLink request
    ) {
        return policyService.linkVueFileByView(request);
    }

    @Operation(summary = "View-Vue 언링크", description = "View 와 VUE 파일을 언링크 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegePolicy.PrivilegePolicyBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/view/vue/unlink")
    public ResponseEntity unlinkVueFileByView(
            @Parameter(description = "Schemas - VueFileLink")
            @RequestBody
            @Validated(value = { OnVueFileLink.class })
            Policy.VueFileLink request
    ) {
        return policyService.unlinkVueFileByView(request);
    }

    /**
     * Menu - Views API.
     * */
    @Operation(summary = "메뉴-뷰 매핑 조회", description = "조건에 맞는 대상을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegePolicy.GetPrivilegePolicyViewsByMenuResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @GetMapping(value = "/menu/views")
    public ResponseEntity getViewsByMenu(
            @Parameter(description = "Schemas - AbstractRequest")
            AbstractRequest request) {
        return policyService.getViewsByMenu(request);
    }

    @Operation(summary = "Menu 에 View 를 등록", description = "Menu 에 View 를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegePolicy.PrivilegePolicyBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/menu/views/create")
    public ResponseEntity createViewsByMenu(
            @Parameter(description = "Schemas - ViewsMappedByMenu")
            @RequestBody
            @Validated(value = { OnViewsMappedByMenu.class })
            Policy.ViewsMappedByMenu request
    ) {
        return policyService.insertViewsByMenu(request);
    }

    @Operation(summary = "Menu 에서 View 삭제", description = "Menu 에서 View 를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegePolicy.PrivilegePolicyBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/menu/views/delete")
    public ResponseEntity deleteViewsByMenu(
            @Parameter(description = "Schemas - ViewsMappedByMenu")
            @RequestBody
            @Validated(value = { OnViewsMappedByMenu.class })
            Policy.ViewsMappedByMenu request
    ) {
        return policyService.deleteViewsByMenu(request);
    }

    /**
     * Role - Users API.
     * */
    @Operation(summary = "Role-User Map 조회", description = "조건에 맞는 Role-User Map을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegePolicy.GetPrivilegePolicyUsersByRoleResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @GetMapping(value = "/role/users")
    public ResponseEntity getUsersByRole(
            @Parameter(description = "Schemas - AbstractRequest")
            AbstractRequest request
    ) {
        return policyService.getUsersByRole(request);
    }
    @Operation(summary = "Role에 사용자를 추가한다.", description = "Role에 사용자를 추가 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegePolicy.PrivilegePolicyBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/role/users/create")
    public ResponseEntity createUsersByRole(
            @Parameter(description = "Schemas - UsersMappedByRole")
            @RequestBody
            @Validated(value = { OnUsersMappedByRole.class })
            Policy.UsersMappedByRole request
    ) {
        return policyService.insertUsersByRole(request);
    }
    @Operation(summary = "Role 에서 사용자를 삭제한다.", description = "Role 에서 사용자를 삭제 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegePolicy.PrivilegePolicyBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/role/users/delete")
    public ResponseEntity deleteUsersByRole(
            @Parameter(description = "Schemas - UsersMappedByRole")
            @RequestBody
            @Validated(value = { OnUsersMappedByRole.class })
            Policy.UsersMappedByRole request
    ) {
        return policyService.deleteUsersByRole(request);
    }

    /**
     * Role - Menus API.
     * */
    @Operation(summary = "Role-Menu Map 조회", description = "조건에 맞는 Role-Menu Map을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegePolicy.GetPrivilegePolicyMenusByRoleResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @GetMapping(value = "/role/menus")
    public ResponseEntity getMenusByRole(
            @Parameter(description = "Schemas - AbstractRequest")
            AbstractRequest request
    ) {
        return policyService.getMenusByRole(request);
    }
    @Operation(summary = "Role 에서 메뉴를 추가", description = "Role 에서 메뉴를 추가 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegePolicy.PrivilegePolicyBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/role/menus/create")
    public ResponseEntity createMenusByRole(
            @Parameter(description = "Schemas - MenusMappedByRole")
            @Validated(value = { OnMenusMappedByRole.class })
            @RequestBody
            Policy.MenusMappedByRole request
    ) {
        return policyService.insertMenusByRole(request);
    }

    @Operation(summary = "Role 에서 메뉴를 삭제", description = "Role 에서 메뉴를 삭제 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegePolicy.PrivilegePolicyBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/role/menus/delete")
    public ResponseEntity deleteMenusByRole(
            @Parameter(description = "Schemas - MenusMappedByRole")
            @Validated(value = { OnMenusMappedByRole.class })
            @RequestBody
            Policy.MenusMappedByRole request

    ) {
        return policyService.deleteMenusByRole(request);
    }


}
