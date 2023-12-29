package com.hdel.miri.api.domain.portfolio;

import com.hdel.miri.api.domain.portfolio.valid.OnPortfolioCreate;
import com.hdel.miri.api.domain.portfolio.valid.OnPortfolioDelete;
import com.hdel.miri.api.domain.portfolio.valid.OnPortfolioUpdate;
import com.hdel.miri.api.util.request.AbstractRequest;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class Portfolio {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortfolioSearch extends AbstractRequest {
        @Hidden
        private BigDecimal userPortfolioMappingId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortfolioSelection extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "1231", description = "포트폴리오 키", required = true)
        @NotNull(groups = { OnPortfolioUpdate.class }, message = "포트폴리오 키 입력이 필요합니다.")
        private BigDecimal userPortfolioMappingId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortfolioCreate extends AbstractRequest{
        @Schema(type = "String", example = "포트폴리오 이름", description = "포트폴리오 이름", required = true)
        @NotEmpty(groups = { OnPortfolioCreate.class }, message = "포트폴리오 이름 입력이 필요합니다.")
        @NotNull(groups = { OnPortfolioCreate.class }, message = "포트폴리오 이름 입력이 필요합니다.")
        @NotBlank(groups = { OnPortfolioCreate.class }, message = "포트폴리오 이름 입력이 필요합니다.")
        private String portfolioName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortfolioUpdate extends AbstractRequest {

        @Schema(type = "BigDecimal", example = "123", description = "포트폴리오 키", required = true)
        @NotNull(groups = { OnPortfolioUpdate.class }, message = "포트폴리오 키 입력이 필요합니다.")
        private BigDecimal userPortfolioMappingId;

        @Schema(type = "String", example = "포트폴리오 이름", description = "포트폴리오 이름", required = true)
        @NotEmpty(groups = { OnPortfolioUpdate.class }, message = "포트폴리오 이름 입력이 필요합니다.")
        @NotNull(groups = { OnPortfolioUpdate.class }, message = "포트폴리오 이름 입력이 필요합니다.")
        @NotBlank(groups = { OnPortfolioUpdate.class }, message = "포트폴리오 이름 입력이 필요합니다.")
        private String portfolioName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortfolioDefaultCreate {
        @Hidden
        private BigDecimal userPortfolioMappingId;

        @Schema(type = "String", example = "test@test.co.kr", description = "사용자 아이디",required = true)
        private String userId;
        @Schema(type = "String", example = "y", description = "디폴트 여부",required = true)
        private String defaultYn;
        @Schema(type = "String", example = "lobby", description = "포트폴리오 명",required = true)
        private String portfolioName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortfolioDelete extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "123", description = "포트폴리오 키",required = true)
        @NotNull(groups = { OnPortfolioDelete.class }, message = "포트폴리오 키 입력이 필요합니다.")
        private BigDecimal userPortfolioMappingId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailVO {
        private BigDecimal userPortfolioMappingId;
        private String intgPrjTrlineCdCode;
        private String intgPrjNo;
        private String trlineCd;
        private String defaultYn;
        private String sortSeq;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VO {
        @Schema(example = "1")
        private BigDecimal userPortfolioMappingId;
        @Schema(example = "포트폴리오 이름")
        private String portfolioName;
        @Schema(example = "2")
        private BigDecimal portfolioInContractEa;
        @Schema(example = "y")
        private String defaultYn;
        @Schema(example = "1")
        private String sortSeq;
    }
}
