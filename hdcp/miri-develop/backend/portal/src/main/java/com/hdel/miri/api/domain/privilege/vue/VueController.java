package com.hdel.miri.api.domain.privilege.vue;

import com.hdel.miri.api.domain.privilege.vue.valid.OnVueCreate;
import com.hdel.miri.api.util.response.ResponseFactory;
import com.hdel.miri.api.domain.privilege.vue.valid.OnVueDelete;
import com.hdel.miri.api.domain.privilege.vue.valid.OnVueUpdate;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import com.hdel.miri.api.util.response.swagger.privilege.vue.SwaggerPrivilegeVue;
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
@Tag(name = "VUE FILE 관리", description = "VUE FILE 관리 API")
@RestController
@RequestMapping("/v2/privilege/vue")
@RequiredArgsConstructor
public class VueController {

    private final VueService vueService;

    @Operation(summary = "VUE FILE 리스트 조회", description = "VUE FILE 리스트를 조회하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegeVue.GetPrivilegeVueFileListResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @GetMapping(value = "/list")
    public ResponseEntity<?> getList(
            @Parameter(description = "Schemas - VueSearch")
            Vue.VueSearch request
    ) {
        return vueService.getList(request);
    }

    @Operation(summary = "VUE FILE 등록", description = "조건에 맞는 VUE FILE을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegeVue.PrivilegeVueBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/create")
    public ResponseEntity<?> create(
            @Parameter(description = "Schemas - VueCreate")
            @RequestBody
            @Validated(value = { OnVueCreate.class })
            Vue.VueCreate request
    ) {
        return vueService.insert(request);
    }

    @Operation(summary = "VUE FILE 수정", description = "조건에 맞는 VUE FILE을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegeVue.PrivilegeVueBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/update")
    public ResponseEntity<?> update(
            @Parameter(description = "Schemas - VueUpdate")
            @RequestBody
            @Validated(value = { OnVueUpdate.class })
            Vue.VueUpdate request
    ) {
        return vueService.update(request);
    }

    @Operation(summary = "VUE FILE 삭제", description = "조건에 맞는 VUE FILE을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPrivilegeVue.PrivilegeVueBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/delete")
    public ResponseEntity<?> delete(
            @Parameter(description = "Schemas - VueDelete")
            @RequestBody
            @Validated(value = { OnVueDelete.class })
            Vue.VueDelete request
    ) {
        return vueService.delete(request);
    }
}
