package com.hdel.miri.api.domain.privilege.role;

import com.hdel.miri.api.domain.privilege.role.valid.*;
import com.hdel.miri.api.util.response.ResponseFactory;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import com.hdel.miri.api.util.response.swagger.privilege.role.SwaggerPrivilegeRole;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Role 관리", description = "Role 관리 API")
@RestController
@RequestMapping("/v2/privilege/role")
@RequiredArgsConstructor
public class RoleController {
    
    private final RoleService roleService;
    
    @Operation(summary = "Role 추가", description = "Role을 추가 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegeRole.PrivilegeRoleBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/create")
    public ResponseEntity<?> create(
            @Parameter(description = "Schemas - RoleCreate")
            @RequestBody
            @Validated(value = { OnRoleCreate.class })
            Role.RoleCreate request
    )
    {
        return roleService.insert(request);
    }

    @Operation(summary = "Role 수정", description = "Role을 수정 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegeRole.PrivilegeRoleBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/update")
    public ResponseEntity<?> update(
            @Parameter(description = "Schemas - RoleUpdate")
            @RequestBody
            @Validated(value = { OnRoleUpdate.class })
            Role.RoleUpdate request
    ) {
        return roleService.update(request);
    }

    @Operation(summary = "Role 삭제", description = "Role을 삭제 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegeRole.PrivilegeRoleBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/delete")
    public ResponseEntity<?> delete(
            @Parameter(description = "Schemas - RoleDelete")
            @RequestBody
            @Validated(value = { OnRoleDelete.class })
            Role.RoleDelete request
    ) {
        return roleService.delete(request);
    }

    @Operation(summary = "Role 복사", description = "Role을 복사 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegeRole.PrivilegeRoleBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/copy")
    public ResponseEntity copy(
            @Parameter(description = "Schemas - RoleCopy")
            @RequestBody
            @Validated(value = { OnRoleCopy.class })
            Role.RoleCopy request
    ) {
        return roleService.copy(request);
    }



    @Operation(summary = "Role을 이동한다.", description = "Role을 이동한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegeRole.PrivilegeRoleBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/move")
    public ResponseEntity<?> move(
            @Parameter(description = "Schemas - RoleMove")
            @RequestBody
            @Validated(value = { OnRoleMove.class })
            Role.RoleMove request
    ) {
        return roleService.move(request);
    }


}
