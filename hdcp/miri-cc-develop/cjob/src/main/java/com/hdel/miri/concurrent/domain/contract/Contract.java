package com.hdel.miri.concurrent.domain.contract;

import com.hdel.miri.concurrent.domain.contract.valid.OnAnonymousSearch;
import com.hdel.miri.concurrent.util.request.AbstractRequest;
import io.jsonwebtoken.io.DeserializationException;
import io.jsonwebtoken.io.Deserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

public class Contract {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper=false)
    public static final class ContractSearchAnonymous extends AbstractRequest implements Serializable {
        @Schema(type = "String", example = "승강기번호 | 주민번호 | 사업자번호", description = "승강기번호 | 주민번호 | 사업자번호")
        @NotBlank(message = "검색 데이터를 입력 해주세요.", groups = {OnAnonymousSearch.class })
        private String searchId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper=false)
    public static final class ContractDefaultCreate {
        private String userId;
        private BigDecimal userPortfolioMappingId;
        private String IntgPrjTrlineCdCode;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper=false)
    public static final class ContractAPI implements Serializable {
        @Schema(type = "String", example = "통합 프로젝트 코드", description = "거래선 코드")
        private String intgPrjNo;
        @Schema(type = "String", example = "거래선 코드", description = "거래선 코드")
        private String trlineCd;
        @Schema(type = "String", example = "모델 정보", description = "모델 정보")
        private String model;
        @Schema(type = "String", example = "사이트 이름", description = "사이트 이름")
        private String siteName;
        @Schema(type = "String", example = "주소 정보", description = "주소 정보")
        private String addr;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper=false)
    public static final class ContractSummary {
        private String intgPrjTrlineCdCode;
        private String approvalYn;
    }
}
