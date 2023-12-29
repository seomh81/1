package com.hdel.miri.api.domain.privilege.view;

import com.hdel.miri.api.domain.privilege.view.valid.OnViewCreate;
import com.hdel.miri.api.domain.privilege.view.valid.OnViewDelete;
import com.hdel.miri.api.domain.privilege.view.valid.OnViewUpdate;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import com.hdel.miri.api.util.response.swagger.privilege.view.SwaggerPrivilegeView;
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
@Tag(name = "View 관리", description = "View 관리 API")
@RestController
@RequestMapping("/v2/privilege/view")
@RequiredArgsConstructor
public class ViewController {
    private final ViewService viewService;

    @Operation(summary = "View FILE 리스트 조회", description = "View FILE 리스트를 조회하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegeView.GetPrivilegeViewsResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @GetMapping(value = "/list")
    public ResponseEntity<?> getList(
            @Parameter(description = "Schemas - ViewSearch")
            View.ViewSearch request
    ) {
        return viewService.getList(request);
    }

    @Operation(summary = "View 생성", description = "조건에 맞는 View FILE을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegeView.PrivilegeViewsBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/create")
    public ResponseEntity<?> create(
            @Parameter(description = "Schemas - ViewCreate")
            @RequestBody
            @Validated(value = {OnViewCreate.class })
            View.ViewCreate request
    ) {
        return viewService.insert(request);
    }

    @Operation(summary = "View FILE 수정", description = "조건에 맞는 View FILE을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegeView.PrivilegeViewsBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/update")
    public ResponseEntity<?> update(
            @Parameter(description = "Schemas - ViewUpdate")
            @RequestBody
            @Validated(value = {OnViewUpdate.class })
            View.ViewUpdate request
    ) {
        return viewService.update(request);
    }

    @Operation(summary = "View FILE 삭제", description = "조건에 맞는 View FILE을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegeView.PrivilegeViewsBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/delete")
    public ResponseEntity<?> delete(
            @Parameter(description = "Schemas - ViewDelete")
            @RequestBody
            @Validated(value = {OnViewDelete.class })
            View.ViewDelete request
    ) {
        return viewService.delete(request);
    }
}
