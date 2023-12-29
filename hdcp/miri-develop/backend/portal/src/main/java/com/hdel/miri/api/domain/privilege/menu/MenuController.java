package com.hdel.miri.api.domain.privilege.menu;

import com.hdel.miri.api.domain.privilege.menu.valid.*;
import com.hdel.miri.api.util.response.ResponseFactory;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import com.hdel.miri.api.util.response.swagger.privilege.menu.SwaggerPrivilegeMenu;
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
@Tag(name = "메뉴 관리", description = "메뉴 관리 API")
@RestController
@RequestMapping("/v2/privilege/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @Operation(summary = "메뉴 추가", description = "메뉴를 추가 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegeMenu.SwaggerMenuBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/create")
    public ResponseEntity create(
            @Parameter(description = "Schemas - MenuCreate")
            @RequestBody
            @Validated(value = { OnMenuCreate.class })
            Menu.MenuCreate request
    ) {
        return menuService.insert(request);
    }

    @Operation(summary = "메뉴 수정", description = "메뉴를 수정 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegeMenu.SwaggerMenuBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/update")
    public ResponseEntity update(
            @Parameter(description = "Schemas - MenuUpdate")
            @RequestBody
            @Validated(value = { OnMenuUpdate.class })
            Menu.MenuUpdate request
    ) {
        return menuService.update(request);
    }

    @Operation(summary = "메뉴 삭제", description = "메뉴 삭제 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegeMenu.SwaggerMenuBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/delete")
    public ResponseEntity RemoveMenu(
            @Parameter(description = "Schemas - MenuDelete")
            @RequestBody
            @Validated(value = { OnMenuDelete.class })
            Menu.MenuDelete request
    ) {
        return menuService.delete(request);
    }

    @Operation(summary = "Menu 복사", description = "조건에 맞는 Menu를 복사합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegeMenu.SwaggerMenuBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/copy")
    public ResponseEntity<?> CopyMenu(
            @Parameter(description = "Schemas - MenuCopy")
            @RequestBody
            @Validated(value = { OnMenuCopy.class })
            Menu.MenuCopy request
    ) {
        return menuService.copy(request);
    }



    @Operation(summary = "Menu 이동", description = "조건에 맞는 Menu를 이동합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegeMenu.SwaggerMenuBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/move")
    public ResponseEntity<?> MoveMenu(
            @Parameter(description = "Schemas - MenuMove")
            @RequestBody
            @Validated(value = { OnMenuMove.class })
            Menu.MenuMove request
    ) {
        return menuService.move(request);
    }
}
