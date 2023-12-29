package com.hdel.miri.api.domain.faq;

import com.hdel.miri.api.domain.faq.valid.OnFAQCreate;
import com.hdel.miri.api.domain.faq.valid.OnFAQUpdate;
import com.hdel.miri.api.util.request.RequestWrapper;
import com.hdel.miri.api.util.response.ResponseFactory;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import com.hdel.miri.api.util.response.swagger.faq.SwaggerFAQ;
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
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * FAQ API.
 * */
@Slf4j
@Tag(name = "FAQ API", description = "FAQ 관련 API")
@RestController
@RequestMapping("/v2/board/faq/")
@RequiredArgsConstructor
public class FAQController {

    private final FAQService faqService;

    @Operation(summary = "FAQ 조회", description = "FAQ 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerFAQ.GetFAQVOListResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @GetMapping(value = "/list")
    public ResponseEntity<?> getList(
            @Parameter(description = "Schemas - FAQSearchOption")
            FAQ.FAQSearchOption request
            , HttpServletRequest sRequest
    ) throws IOException {

        request.setMobile(DeviceUtils.getCurrentDevice(new RequestWrapper(sRequest)).isMobile());
        return faqService.getList(request);
    }

    @Operation(summary = "FAQ 조회(모바일)", description = "FAQ 조회 API(모바일)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerFAQ.GetFAQVOListResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @GetMapping(value = "/mobile/list")
    public ResponseEntity<?> getListMobile(
            @Parameter(description = "Schemas - FAQSearchOptionMobile")
            FAQ.FAQSearchOptionMobile request
    ) {
        return faqService.getListMobile(request);
    }

    @Operation(summary = "FAQ 등록", description = "FAQ 등록 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerFAQ.GetFAQBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/create")
    public ResponseEntity<?> create(
            @Parameter(description = "Schemas - FAQCreate")
            @RequestBody
            @Validated(value = { OnFAQCreate.class })
            FAQ.FAQCreate request
    ) {
        return faqService.create(request);
    }

    @Operation(summary = "FAQ 수정", description = "FAQ 수정 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerFAQ.GetFAQBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/update")
    public ResponseEntity<?> update(
            @Parameter(description = "Schemas - FAQUpdate")
            @RequestBody@Validated(value = { OnFAQUpdate.class })
            FAQ.FAQUpdate request
    ) {
        return faqService.update(request);
    }

    @Operation(summary = "FAQ 삭제", description = "FAQ 삭제 API.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerFAQ.GetFAQBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/remove")
    public ResponseEntity<?> remove(
            @Parameter(description = "Schemas - FAQRemove")
            @RequestBody
            FAQ.FAQRemove request

    ) {
        return faqService.remove(request);
    }
}
