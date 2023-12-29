package com.hdel.miri.api.domain.storage;

import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import com.hdel.miri.api.util.response.swagger.storage.SwaggerStorage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Tag(name = "이미지 저장소 기능", description = "이미지 저장소 기능 API")
@RestController
@RequestMapping("/v2/storage")
@RequiredArgsConstructor
public class StorageController {
    private final StorageService storageService;


    @Operation(summary = "공통 이미지 업로드 기능", description = "공통 이미지 업로드 기능 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerStorage.GetStorageUploadResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/images/upload", consumes = {
            MediaType.APPLICATION_JSON_VALUE
            , MediaType.MULTIPART_FORM_DATA_VALUE
            })
    public ResponseEntity upload(
            @RequestPart("request") Storage.ImageSave request
            , @RequestPart("files") List<MultipartFile> files
    ){
        return storageService.save(request,files);
    }


    @Operation(summary = "공통 이미지 뷰어 기능", description = "공통 이미지 뷰어 기능 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "image", schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @GetMapping(value = "/images/viewer/{key}",produces = {
            "image/bmp",
            "image/gif",
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/svg+xml",
            "image/tiff",
            "image/webp"
    })
    public ResponseEntity<byte[]> getResource(
            @Parameter(description = "Image UUID", in = ParameterIn.PATH)
            @PathVariable String key) {
        return storageService.imageView(key);
    }


    @Operation(summary = "공통 계약 파일 다운로드", description = "공통 계약 파일 다운로드 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/octet-stream", schema = @Schema(implementation = ResponseEntity.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @GetMapping(value = "/contract/{key}")
    public ResponseEntity<Resource> getContractFile(
            @Parameter(description = "유상계약번호", in = ParameterIn.PATH)
            @PathVariable String key, HttpServletRequest request) {
        return storageService.contractDownload(key,request);
    }

}
