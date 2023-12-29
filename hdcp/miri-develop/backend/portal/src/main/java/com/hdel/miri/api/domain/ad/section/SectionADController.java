package com.hdel.miri.api.domain.ad.section;

import com.hdel.miri.api.domain.ad.section.valid.OnSectionADCreate;
import com.hdel.miri.api.domain.ad.section.valid.OnSectionADRemove;
import com.hdel.miri.api.domain.ad.section.valid.OnSectionADUpdate;
import com.hdel.miri.api.domain.ad.section.valid.OnSectionDetailSearch;
import com.hdel.miri.api.util.file.FileService;
import com.hdel.miri.api.util.request.AbstractRequest;
import com.hdel.miri.api.util.response.ResponseFactory;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import com.hdel.miri.api.util.response.swagger.ad.section.SwaggerSectionAD;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Tag(name = "부분 광고 관리", description = "부분 광고 관리 API")
@RestController
@RequestMapping("/v2/ad/section")
@RequiredArgsConstructor
public class SectionADController {
    private final FileService fileService;
    private final SectionADService sectionADService;

    @Operation(summary = "인증화면 광고 상세(이미지) 조회", description = "인증화면 광고 상세(이미지) 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerSectionAD.SwaggerSectionADStringsResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @GetMapping(value = "/images")
    public ResponseEntity getImages(
            @Parameter(description = "Schemas - AbstractRequest 참조")
            AbstractRequest request) {
        return sectionADService.getImages(request);
    }
    @Operation(summary = "중간광고 리스트 조회", description = "중간광고 리스트 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerSectionAD.SwaggerSectionADListResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @GetMapping(value = "/list")
    public ResponseEntity getList(
            @Parameter(description = "Schemas - SectionADCreate 참조")
            SectionAD.SectionADSearch request
    ) {
        return sectionADService.getList(request);
    }

    @Operation(summary = "중간광고 상세(이미지) 조회", description = "중간광고 상세(이미지) 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerSectionAD.SwaggerSectionADDetailListResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @GetMapping(value = "/details")
    public ResponseEntity details(
            @Parameter(description = "Schemas - SectionADDetailSearch 참조")
            @Validated(value = { OnSectionDetailSearch.class })
            SectionAD.SectionADDetailSearch request) {
        return sectionADService.getDetails(request);
    }

    @Operation(summary = "중간광고 등록", description = "중간광고 등록 API ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerSectionAD.SwaggerSectionADBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/create")
    public ResponseEntity create(
            @Parameter(description = "Schemas - SectionADCreate 참조")
            @RequestBody
            @Validated(value = { OnSectionADCreate.class })
            SectionAD.SectionADCreate request
    ) {

        return sectionADService.insert(request);
    }

    @Operation(summary = "중간광고 상세(이미지) 등록", description = "중간광고 상세(이미지) 등록 API ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerSectionAD.SwaggerSectionADBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/details/create", consumes = { })
    public ResponseEntity<?> insertDetails(
            @Parameter(description = "Schemas - SectionADDetailCreate 참조")
            @Validated(value = { OnSectionADCreate.class })
            @RequestPart("request") SectionAD.SectionADDetailCreate request
            ,@RequestPart("files") List<MultipartFile> files
    ) {
        return sectionADService.insertDetails(request,files);
    }

    @Operation(summary = "중간광고 수정", description = "중간광고 수정 API ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerSectionAD.SwaggerSectionADBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/update")
    public ResponseEntity update(
            @Parameter(description = "Schemas - SectionADUpdate 참조")
            @RequestBody
            @Validated(value = { OnSectionADUpdate.class })
            SectionAD.SectionADUpdate request
    ) {
        return sectionADService.update(request);
    }

    @Operation(summary = "중간 광고 활성화", description = "중간 광고 활성화 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerSectionAD.SwaggerSectionADBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/active")
    public ResponseEntity<?> active(
            @Parameter(description = "Schemas - SectionADActivation 참조")
            @RequestBody
            @Validated(value = { OnSectionADUpdate.class })
            SectionAD.SectionADActivation request
    ) {
        return sectionADService.active(request);
    }
    @Operation(summary = "중간 광고 비활성화", description = "중간 광고 비활성화 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerSectionAD.SwaggerSectionADBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/inactive")
    public ResponseEntity<?> inactive(
            @Parameter(description = "Schemas - SectionADActivation 참조")
            @RequestBody
            @Validated(value = { OnSectionADUpdate.class })
            SectionAD.SectionADActivation request
    ) {
        return sectionADService.inactive(request);
    }
    
    
    @Operation(summary = "중간광고 삭제", description = "중간광고 삭제 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerSectionAD.SwaggerSectionADBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/remove")
    public ResponseEntity remove(
            @Parameter(description = "Schemas - SectionADRemove 참조")
            @RequestBody
            @Validated(value = { OnSectionADRemove.class })
            SectionAD.SectionADRemove request
    ) {
        return sectionADService.remove(request);
    }

    @Operation(summary = "중간광고 상세(이미지) 삭제", description = "중간광고 상세(이미지) 삭제 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerSectionAD.SwaggerSectionADBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/detail/delete")
    public ResponseEntity<?> deleteDetail(
            @Parameter(description = "Schemas - SectionADRemove 참조")
            @RequestBody
            @Validated(value = { OnSectionADRemove.class })
            SectionAD.SectionADDetailDelete request
    ) {
        return sectionADService.deleteDetail(request);
    }
}
