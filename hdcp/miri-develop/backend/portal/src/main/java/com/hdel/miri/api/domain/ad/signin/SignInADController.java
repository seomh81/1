package com.hdel.miri.api.domain.ad.signin;

import com.hdel.miri.api.domain.ad.signin.valid.OnSignInADCreate;
import com.hdel.miri.api.domain.ad.signin.valid.OnSignInADDetailSearch;
import com.hdel.miri.api.domain.ad.signin.valid.OnSignInADRemove;
import com.hdel.miri.api.domain.ad.signin.valid.OnSignInADUpdate;
import com.hdel.miri.api.util.request.AbstractRequest;
import com.hdel.miri.api.util.response.ResponseFactory;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import com.hdel.miri.api.util.response.swagger.ad.signin.SwaggerSignInAD;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Tag(name = "인증화면 광고 관리", description = "인증화면 광고 관리 API")
@RestController
@RequestMapping("/v2/ad/signin")
@RequiredArgsConstructor
public class SignInADController {

    private final SignInADService signInADService;

    @Operation(summary = "인증화면 광고 상세(이미지) 조회", description = "인증화면 광고 상세(이미지) 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerSignInAD.GetSignInADImagePathResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @GetMapping(value = "/images")
    public ResponseEntity getImages(
            @Parameter(description = "Schemas - AbstractRequest 참조")
            AbstractRequest request) {
        return signInADService.getImages(request);
    }

    @Operation(summary = "인증화면 광고 리스트 조회", description = "인증화면 광고 리스트 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerSignInAD.GetSignInADVOResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @GetMapping(value = "/list")
    public ResponseEntity getList(
            @Parameter(description = "Schemas - SignInADCreate 참조")
            SignInAD.SignInADSearch request
    ) {
        return signInADService.getList(request);
    }

    @Operation(summary = "인증화면 광고 상세(이미지) 조회", description = "인증화면 광고 상세(이미지) 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerSignInAD.GetSignInADDetailVOResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @GetMapping(value = "/details")
    public ResponseEntity ListInterimAdImages(
            @Parameter(description = "Schemas - SignInADDetailSearch 참조")
            @Validated(value = {OnSignInADDetailSearch.class})
            SignInAD.SignInADDetailSearch request) {
        return signInADService.getDetails(request);
    }

    @Operation(summary = "인증화면 광고 등록", description = "인증화면 광고 등록 API ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerSignInAD.SignInADBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/create")
    public ResponseEntity create(
            @Parameter(description = "Schemas - SignInADCreate 참조")
            @RequestBody
            @Validated(value = {OnSignInADCreate.class})
            SignInAD.SignInADCreate request
    ) {

        return signInADService.insert(request);
    }

    @Operation(summary = "인증화면 광고 상세(이미지) 등록", description = "인증화면 광고 상세(이미지) 등록 API ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerSignInAD.SignInADBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/details/create", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> insertDetails(
            @Parameter(description = "Schemas - SignInADDetailCreate 참조")
            @Validated(value = {OnSignInADCreate.class})
            @RequestPart("request") SignInAD.SignInADDetailCreate request
            , @RequestPart("files") List<MultipartFile> files
    ) {
        return signInADService.insertDetails(request,files);
    }

    @Operation(summary = "인증화면 광고 수정", description = "인증화면 광고 수정 API ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerSignInAD.SignInADBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/update")
    public ResponseEntity update(
            @Parameter(description = "Schemas - SignInADUpdate 참조")
            @RequestBody
            @Validated(value = {OnSignInADUpdate.class})
            SignInAD.SignInADUpdate request
    ) {
        return signInADService.update(request);
    }

    @Operation(summary = "인증 광고 활성화", description = "인증 광고 활성화 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerSignInAD.SignInADBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/active")
    public ResponseEntity<?> active(
            @Parameter(description = "Schemas - SignInADActivation 참조")
            @RequestBody
            @Validated(value = {OnSignInADUpdate.class})
            SignInAD.SignInADActivation request
    ) {
        return signInADService.active(request);
    }
    @Operation(summary = "인증 광고 비활성화", description = "인증 광고 비활성화 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerSignInAD.SignInADBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/inactive")
    public ResponseEntity<?> inactive(
            @Parameter(description = "Schemas - SignInADActivation 참조")
            @RequestBody
            @Validated(value = {OnSignInADUpdate.class})
            SignInAD.SignInADActivation request
    ) {
        return signInADService.inactive(request);
    }

    @Operation(summary = "인증화면 광고 삭제", description = "인증화면 광고 삭제 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerSignInAD.SignInADBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/remove")
    public ResponseEntity delete(
            @Parameter(description = "Schemas - SignInADRemove 참조")
            @RequestBody
            @Validated(value = {OnSignInADRemove.class})
            SignInAD.SignInADRemove request
    ) {
        return signInADService.remove(request);
    }

    @Operation(summary = "인증화면 광고 상세(이미지) 삭제", description = "인증화면 광고 상세(이미지) 삭제 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerSignInAD.SignInADBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/detail/delete")
    public ResponseEntity<?> deleteDetail(
            @Parameter(description = "Schemas - SignInADRemove 참조")
            @RequestBody
            @Validated(value = {OnSignInADRemove.class})
            SignInAD.SignInADDetailDelete request
    ) {
        return signInADService.deleteDetail(request);
    }
}
