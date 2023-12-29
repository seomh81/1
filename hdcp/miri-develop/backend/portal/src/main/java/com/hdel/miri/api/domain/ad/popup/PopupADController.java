package com.hdel.miri.api.domain.ad.popup;

import com.hdel.miri.api.domain.ad.popup.valid.OnPopupADCreate;
import com.hdel.miri.api.domain.ad.popup.valid.OnPopupADRemove;
import com.hdel.miri.api.domain.ad.popup.valid.OnPopupADUpdate;
import com.hdel.miri.api.util.response.ResponseFactory;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import com.hdel.miri.api.util.response.swagger.ad.popup.SwaggerPopupAD;
import com.hdel.miri.api.util.response.swagger.ad.video.SwaggerVideoAD;
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
@Tag(name = "팝업 광고 관리", description = "팝업 광고 관리 API")
@RestController
@RequestMapping("/v2/ad/popup")
@RequiredArgsConstructor
public class PopupADController {
    
    private final PopupADService popupADService;
    
    @Operation(summary = "팝업 광고 조회", description = "조건에 맞는 팝업 광고를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPopupAD.GetPopupADListResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @GetMapping(value = "/list")
    public ResponseEntity<?> list(
            @Parameter(description = "Schemas - PopupADSearch 참조")
            PopupAD.PopupADSearch request
    ) {
        return popupADService.getList(request);
    }

    @Operation(summary = "팝업 광고 등록", description = "조건에 맞는 팝업 광고를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPopupAD.PopupADBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/create")
    public ResponseEntity<?> create(
            @Parameter(description = "Schemas - PopupADCreate 참조")
            @RequestBody
            @Validated(value = { OnPopupADCreate.class })
            PopupAD.PopupADCreate request) {
        return popupADService.insert(request);
    }

    @Operation(summary = "팝업 광고 수정", description = "조건에 맞는 팝업 광고를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPopupAD.PopupADBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/update")
    public ResponseEntity<?> update(
            @Parameter(description = "Schemas - PopupADUpdate 참조")
            @RequestBody
            @Validated(value = { OnPopupADUpdate.class })
            PopupAD.PopupADUpdate request) {
        return popupADService.update(request);
    }

    @Operation(summary = "팝업 광고 활성화", description = "팝업 광고 활성화 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPopupAD.PopupADBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/active")
    public ResponseEntity<?> active(
            @Parameter(description = "Schemas - PopupADActivation 참조")
            @RequestBody
            @Validated(value = { OnPopupADUpdate.class })
            PopupAD.PopupADActivation request) {
        return popupADService.active(request);
    }
    @Operation(summary = "팝업 광고 비활성화", description = "팝업 광고 비활성화 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPopupAD.PopupADBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/inactive")
    public ResponseEntity<?> unactive(
            @Parameter(description = "Schemas - PopupADActivation 참조")
            @RequestBody
            @Validated(value = { OnPopupADUpdate.class })
            PopupAD.PopupADActivation request) {
        return popupADService.inactive(request);
    }

    @Operation(summary = "팝업 광고 삭제", description = "조건에 맞는 팝업 광고를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPopupAD.PopupADBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/remove")
    public ResponseEntity<?> remove(
            @Parameter(description = "Schemas - PopupADRemove 참조")
            @RequestBody
            @Validated(value = { OnPopupADRemove.class })
            PopupAD.PopupADRemove request) {
        return popupADService.remove(request);
    }
}
