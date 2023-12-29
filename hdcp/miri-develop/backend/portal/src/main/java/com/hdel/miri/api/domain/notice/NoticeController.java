package com.hdel.miri.api.domain.notice;

import com.hdel.miri.api.domain.notice.valid.OnNoticeCreate;
import com.hdel.miri.api.domain.notice.valid.OnNoticeRemove;
import com.hdel.miri.api.domain.notice.valid.OnNoticeUpdate;
import com.hdel.miri.api.util.request.RequestWrapper;
import com.hdel.miri.api.util.response.ResponseFactory;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import com.hdel.miri.api.util.response.swagger.notice.SwaggerNotice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Request;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Tag(name = "공지사항 API", description = "공지사항 관련 API")
@RestController
@RequestMapping("/v2/board/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "Notice 조회", description = "Notice 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerNotice.GetNoticeResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @GetMapping(value = "/list")
    public ResponseEntity getList(
            @Parameter(description = "Schemas - NoticeSearch")
            Notice.NoticeSearch request
            , HttpServletRequest sRequest
    ) throws IOException {
        request.setMobile(DeviceUtils.getCurrentDevice(new RequestWrapper(sRequest)).isMobile());
        return noticeService.getList(request);
    }

    @Operation(summary = "Notice 등록", description = "Notice 등록 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerNotice.NoticeBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/create")
    public ResponseEntity<?> createNotice(
            @Parameter(description = "Schemas - NoticeCreate")
            @RequestBody
            @Validated(value = { OnNoticeCreate.class })
            Notice.NoticeCreate request
    ) {
        return noticeService.create(request);
    }

    @Operation(summary = "Notice 수정", description = "Notice 수정 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerNotice.NoticeBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/update")
    public ResponseEntity<?> updateNotice(
            @Parameter(description = "Schemas - NoticeUpdate")
            @RequestBody
            @Validated(value = { OnNoticeUpdate.class })
            Notice.NoticeUpdate request
    ) {
        return noticeService.update(request);
    }

    @Operation(summary = "Notice 삭제", description = "Notice 삭제 API.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerNotice.NoticeBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/remove")
    public ResponseEntity<?> removeNotice(
            @Parameter(description = "Schemas - NoticeRemove")
            @RequestBody
            @Validated(value = { OnNoticeRemove.class })
            Notice.NoticeRemove request

    ) {
        return noticeService.remove(request);
    }
}
