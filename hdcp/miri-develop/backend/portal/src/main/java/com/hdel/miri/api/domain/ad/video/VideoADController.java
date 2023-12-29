package com.hdel.miri.api.domain.ad.video;

import com.hdel.miri.api.domain.ad.video.valid.OnVideoADCreate;
import com.hdel.miri.api.domain.ad.video.valid.OnVideoADRemove;
import com.hdel.miri.api.domain.ad.video.valid.OnVideoADUpdate;
import com.hdel.miri.api.util.response.ResponseFactory;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
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
@Tag(name = "비디오 광고 관리", description = "비디오 광고 관리 API")
@RestController
@RequestMapping("/v2/ad/video")
@RequiredArgsConstructor
public class VideoADController {

    private final VideoADService videoADService;

    @Operation(summary = "비디오 광고 조회", description = "조건에 맞는 비디오 광고를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerVideoAD.GetVideoADListResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @GetMapping(value = "/list")
    public ResponseEntity<?> list(
            @Parameter(description = "Schemas - VideoADSearch 참조")
            VideoAD.VideoADSearch request
            ) {
        return videoADService.getList(request);
    }

    @Operation(summary = "비디오 광고 등록", description = "조건에 맞는 비디오 광고를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerVideoAD.VideoADBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/create")
    public ResponseEntity<?> create(
            @Parameter(description = "Schemas - VideoADCreate 참조")
            @RequestBody
            @Validated(value = { OnVideoADCreate.class })
            VideoAD.VideoADCreate request) {
        return videoADService.insert(request);
    }

    @Operation(summary = "비디오 광고 수정", description = "조건에 맞는 비디오 광고를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerVideoAD.VideoADBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/update")
    public ResponseEntity<?> update(
            @Parameter(description = "Schemas - VideoADUpdate 참조")
            @RequestBody
            @Validated(value = { OnVideoADUpdate.class })
            VideoAD.VideoADUpdate request) {
        return videoADService.update(request);
    }

    @Operation(summary = "인증 광고 활성화", description = "인증 광고 활성화 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerVideoAD.VideoADBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/active")
    public ResponseEntity<?> active(
            @Parameter(description = "Schemas - SignInADActivation 참조")
            @RequestBody
            @Validated(value = { OnVideoADUpdate.class })
            VideoAD.VideoADActivation request
    ) {
        return videoADService.active(request);
    }
    @Operation(summary = "인증 광고 비활성화", description = "인증 광고 비활성화 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerVideoAD.VideoADBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/inactive")
    public ResponseEntity<?> inactive(
            @Parameter(description = "Schemas - SignInADActivation 참조")
            @RequestBody
            @Validated(value = { OnVideoADUpdate.class })
            VideoAD.VideoADActivation request
    ) {
        return videoADService.inactive(request);
    }

    @Operation(summary = "비디오 광고 삭제", description = "조건에 맞는 비디오 광고를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerVideoAD.VideoADBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/remove")
    public ResponseEntity<?> remove(
            @Parameter(description = "Schemas - VideoADRemove 참조")
            @RequestBody
            @Validated(value = { OnVideoADRemove.class })
            VideoAD.VideoADRemove request) {
        return videoADService.remove(request);
    }
}
