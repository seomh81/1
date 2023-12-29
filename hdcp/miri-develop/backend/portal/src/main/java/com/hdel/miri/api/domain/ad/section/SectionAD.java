package com.hdel.miri.api.domain.ad.section;


import com.hdel.miri.api.domain.ad.section.valid.OnSectionADCreate;
import com.hdel.miri.api.domain.ad.section.valid.OnSectionADRemove;
import com.hdel.miri.api.domain.ad.section.valid.OnSectionADUpdate;
import com.hdel.miri.api.domain.ad.section.valid.OnSectionDetailSearch;
import com.hdel.miri.api.domain.storage.Storage;
import com.hdel.miri.api.util.request.AbstractRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public class SectionAD {
    /**
     * SearchOption
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SectionADSearch extends AbstractRequest {
        @Schema(type="String",example = "Section1",description = "SectionAD 이름 검색 키워드")
        private String searchKeyword;
        @Schema(type="String",example = "ALL|y|n",description = "SectionAD 활성화 상태")
        private String activationYn;
    }

    /**
     * SearchOption - Detail
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SectionADDetailSearch extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "1", description = "광고 키 입니다. ")
        @NotNull(groups = { OnSectionDetailSearch.class }, message = "광고 키 입력이 필요합니다.")
        private BigDecimal adId;
    }

    /**
     * Create
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SectionADCreate extends AbstractRequest {
        @Schema(type = "String", example = "2022-10월 광고", description = "광고명입니다. ")
        @NotEmpty(groups = { OnSectionADCreate.class }, message = "광고명 입력이 필요합니다.")
        @NotNull(groups = { OnSectionADCreate.class }, message = "광고명 입력이 필요합니다.")
        @NotBlank(groups = { OnSectionADCreate.class }, message = "광고명 입력이 필요합니다.")
        private String adName;
    }

    /**
     * Create
     * */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SectionADDetailCreate extends Storage.ImageSave {
        @Schema(type = "BigDecimal", example = "1", description = "광고 키 입니다. ")
        @NotNull(groups = { OnSectionADCreate.class }, message = "광고 키 입력이 필요합니다.")
        private BigDecimal adId;
    }

    /**
     * Update
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SectionADUpdate extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "1", description = "광고 키 입니다. ")
        @NotNull(groups = { OnSectionADUpdate.class }, message = "광고 키 입력이 필요합니다.")
        private BigDecimal adId;

        @Schema(type = "String", example = "2022-10월 광고", description = "광고명입니다. ")
        @NotEmpty(groups = { OnSectionADUpdate.class }, message = "광고명 입력이 필요합니다.")
        @NotNull(groups = { OnSectionADUpdate.class }, message = "광고명 입력이 필요합니다.")
        @NotBlank(groups = { OnSectionADUpdate.class }, message = "광고명 입력이 필요합니다.")
        private String adName;
    }

    /**
     * Activation
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SectionADActivation extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "[1,2,3,4]", description = "광고 키 입니다. ")
        @NotNull(groups = { OnSectionADUpdate.class }, message = "광고 키 입력이 필요합니다.")
        @Size(min = 1,groups = { OnSectionADUpdate.class }, message = "광고 키 입력이 하나 이상 필요 합니다.")
        private List<BigDecimal> adIds;
    }

    /**
     * Delete - SectionAD
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SectionADRemove extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "[1,2,3,4]", description = "광고 키 입니다. ")
        @NotNull(groups = { OnSectionADRemove.class }, message = "광고 키 입력이 필요합니다.")
        @Size(min = 1,groups = { OnSectionADRemove.class }, message = "광고 키 입력이 하나 이상 필요 합니다.")
        private List<BigDecimal> adIds;
    }

    /**
     * Delete - SectionAD
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SectionADDetailDelete extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "1", description = "광고 상세 키입니다. ")
        @NotNull(groups = { OnSectionADRemove.class }, message = "광고 키 입력이 필요합니다.")
        private BigDecimal adDetailId;
    }

    /**
     * Value Object
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SectionADVO {
        @Schema(type = "BigDecimal", example = "1", description = "광고 키 입니다. ")
        private BigDecimal adId;
        @Schema(type = "String", example = "2022-10월 광고", description = "광고명 입니다. ")
        private String adName;
        @Schema(type="String",example = "y|n",description = "SectionAD 활성화 상태 상태")
        private String activationYn;
        @Schema(type="String",example = "y|n",description = "SectionAD 삭제 상태")
        private String delYn;
    }
    /**
     * Value Object
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SectionADDetailVO {
        @Schema(example = "2")
        private BigDecimal adId;
        @Schema(example = "11")
        private BigDecimal adDetailId;
        @Schema(example = "66b790692f68444eb893728faba879fba7b5e92fcd094fccb8714fc870dc758620230313184657")
        private String physicalFileName;
        @Schema(example = "다운로드.PNG")
        private String originalFileName;

    }
}
