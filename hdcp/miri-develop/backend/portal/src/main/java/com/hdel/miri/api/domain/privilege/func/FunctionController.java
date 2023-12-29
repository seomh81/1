package com.hdel.miri.api.domain.privilege.func;

import com.hdel.miri.api.domain.privilege.func.valid.OnFunctionCreate;
import com.hdel.miri.api.domain.privilege.func.valid.OnFunctionDelete;
import com.hdel.miri.api.domain.privilege.func.valid.OnFunctionUpdate;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import com.hdel.miri.api.util.response.swagger.privilege.func.SwaggerPrivilegeFunc;
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
@Tag(name = "Function 관리", description = "Function 관리 API")
@RestController
@RequestMapping("/v2/privilege/func")
@RequiredArgsConstructor
public class FunctionController {

    private final FunctionService functionService;

    @Operation(summary = "기능 조회", description = "조건에 맞는 기능을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegeFunc.GetFunctionsResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })

    @GetMapping(value = "/list")
    public ResponseEntity getList(
            @Parameter(description = "Schemas - FunctionSearch")
            FunctionVO.FunctionSearch request) {
        return functionService.getList(request);
    }

    @Operation(summary = "기능 등록", description = "조건에 맞는 기능을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegeFunc.FunctionsBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/create")
    public ResponseEntity create(
            @Parameter(description = "Schemas - FunctionCreate")
            @RequestBody
            @Validated(value = { OnFunctionCreate.class })
            FunctionVO.FunctionCreate request
    ) {
        return functionService.insert(request);
    }

    @Operation(summary = "기능 수정", description = "조건에 맞는 기능을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegeFunc.FunctionsBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/update")
    public ResponseEntity update(
            @Parameter(description = "Schemas - FunctionUpdate")
            @RequestBody
            @Validated(value = { OnFunctionUpdate.class })
            FunctionVO.FunctionUpdate request
    ) {
        try {return functionService.update(request); }
        catch (Exception e){ throw new RuntimeException(e.getMessage(),e);}

    }

    @Operation(summary = "기능 삭제", description = "조건에 맞는 기능을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegeFunc.FunctionsBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/delete")
    public ResponseEntity delete(
            @Parameter(description = "Schemas - FunctionDelete")
            @RequestBody
            @Validated(value = { OnFunctionDelete.class })
            FunctionVO.FunctionDelete request
    ) {
        return functionService.delete(request);
    }
}
