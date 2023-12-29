package com.hdel.miri.api.domain.portfolio;

import com.hdel.miri.api.domain.portfolio.valid.OnPortfolioCreate;
import com.hdel.miri.api.domain.portfolio.valid.OnPortfolioDelete;
import com.hdel.miri.api.domain.portfolio.valid.OnPortfolioUpdate;
import com.hdel.miri.api.util.response.ResponseFactory;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import com.hdel.miri.api.util.response.swagger.portfolio.SwaggerPortfolio;
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

import javax.management.RuntimeMBeanException;

/**
 * Portfolio API.
 * */
@Slf4j
@Tag(name = "포트폴리오 API", description = "포트폴리오 API")
@RestController
@RequestMapping("/v2/portfolio")
@RequiredArgsConstructor
public class PortfolioController {
    private final PortfolioService portfolioService;


    @Operation(summary = "사용자의 Portfolio 조회", description = "사용자의 Portfolio 조회 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPortfolio.GetPortfolioResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @GetMapping(value = "/current/list")
    public ResponseEntity<?> getList(
            @Parameter(description = "Schemas - PortfolioSearch")
            Portfolio.PortfolioSearch request
    ) {
        return portfolioService.getListByUser(request);
    }

    @Operation(summary = "Portfolio 등록", description = "Portfolio 등록 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPortfolio.PortfolioBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/current/create")
    public ResponseEntity<?> createPortfolio(
            @Parameter(description = "Schemas - PortfolioCreate")
            @RequestBody
            @Validated(value = { OnPortfolioCreate.class })
            Portfolio.PortfolioCreate request
    ) {
        return portfolioService.insert(request);
    }

    @Operation(summary = "Portfolio 수정", description = "Portfolio 수정 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPortfolio.PortfolioBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/current/update")
    public ResponseEntity<?> updatePortfolio(
            @Parameter(description = "Schemas - PortfolioUpdate")
            @RequestBody
            @Validated(value = { OnPortfolioUpdate.class })
            Portfolio.PortfolioUpdate request
    ) {
        return portfolioService.update(request);
    }

    @Operation(summary = "Portfolio 삭제", description = "Portfolio 삭제 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPortfolio.PortfolioBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/current/delete")
    public ResponseEntity<?> removePortfolio(
            @Parameter(description = "Schemas - PortfolioDelete")
            @RequestBody
            @Validated(value = { OnPortfolioDelete.class })
            Portfolio.PortfolioDelete request
    ) {
        return portfolioService.delete(request);
    }

    @Operation(summary = "Portfolio 기본 설정", description = "Portfolio 기본 설정 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "응답 완료", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerPortfolio.PortfolioBoolResponse.class))),
            @ApiResponse(responseCode = "400", description =  "잘못된 요청" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerBadRequestResponse.class))),
            @ApiResponse(responseCode = "500", description =  "서버 내부 에러" , content = @Content(mediaType = "application/json", schema = @Schema(implementation = SwaggerResponse.SwaggerInternalErrorResponse.class)))
    })
    @PostMapping(value = "/current/default")
    public ResponseEntity<?> defaultPortfolio(
            @Parameter(description = "Schemas - PortfolioSelection")
            @RequestBody
            @Validated(value = { OnPortfolioUpdate.class })
            Portfolio.PortfolioSelection request
    ) {
        return portfolioService.updateDefault(request);
    }
}
