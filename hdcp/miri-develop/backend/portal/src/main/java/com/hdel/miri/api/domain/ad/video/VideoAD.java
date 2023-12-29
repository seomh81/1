package com.hdel.miri.api.domain.ad.video;

import com.hdel.miri.api.domain.ad.video.valid.OnVideoADCreate;
import com.hdel.miri.api.domain.ad.video.valid.OnVideoADRemove;
import com.hdel.miri.api.domain.ad.video.valid.OnVideoADUpdate;
import com.hdel.miri.api.util.request.AbstractRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public class VideoAD {
    /**
     * SearchOption
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VideoADSearch extends AbstractRequest {
        @Schema(type="String",example = "Video1",description = "VideoAD 이름 검색 키워드")
        private String searchKeyword;
        @Schema(type="String",example = "ALL|y|n",description = "VideoAD 활성화 상태")
        private String activationYn;
    }
    /**
     * Create
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VideoADCreate extends AbstractRequest {
        @Schema(type = "String", example = "샘플 광고명", description = "광고명 입니다. ")
        @NotEmpty(groups = { OnVideoADCreate.class }, message = "광고명 입력이 필요합니다.")
        @NotNull(groups = { OnVideoADCreate.class }, message = "광고명 입력이 필요합니다.")
        @NotBlank(groups = { OnVideoADCreate.class }, message = "광고명 입력이 필요합니다.")
        private String title;

        @Schema(type = "String", example = "샘플 내용", description = "광고 내용 입니다. ")
        @NotEmpty(groups = { OnVideoADCreate.class }, message = "광고 내용 입력이 필요합니다.")
        @NotNull(groups = { OnVideoADCreate.class }, message = "광고 내용 입력이 필요합니다.")
        @NotBlank(groups = { OnVideoADCreate.class }, message = "광고 내용 입력이 필요합니다.")
        private String contents;
    }
    /**
     * Update
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VideoADUpdate extends AbstractRequest {

        @Schema(type = "BigDecimal", example = "1", description = "영상 광고 키입니다. ",required = true)
        @NotNull(groups = { OnVideoADUpdate.class }, message = "광고 키 입력이 필요합니다.")
        private BigDecimal videoAdId;

        @Schema(type = "String", example = "샘플 광고명", description = "광고명 입니다. ",required = true)
        @NotEmpty(groups = { OnVideoADUpdate.class }, message = "광고명 입력이 필요합니다.")
        @NotNull(groups = { OnVideoADUpdate.class }, message = "광고명 입력이 필요합니다.")
        @NotBlank(groups = { OnVideoADUpdate.class }, message = "광고명 입력이 필요합니다.")
        private String title;

        @Schema(type = "String", example = "샘플 광고 내용", description = "광고 내용 입니다. ",required = true)
        @NotEmpty(groups = { OnVideoADUpdate.class }, message = "광고 내용 입력이 필요합니다.")
        @NotNull(groups = { OnVideoADUpdate.class }, message = "광고 내용  입력이 필요합니다.")
        @NotBlank(groups = { OnVideoADUpdate.class }, message = "광고 내용 입력이 필요합니다.")
        private String contents;
    }

    /**
     * Activation
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VideoADActivation extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "[1,2,3,4]", description = "광고 키입니다. ")
        @NotNull(groups = { OnVideoADUpdate.class }, message = "광고 키 입력이 필요합니다.")
        @Size(min = 1,groups = { OnVideoADUpdate.class }, message = "광고 키 입력이 하나 이상 필요 합니다.")
        private List<BigDecimal> videoAdIds;
    }

    /**
     * Remove
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VideoADRemove extends AbstractRequest {
        @Schema(type = "List<BigDecimal>", example = "[1,2,3]", description = "영상 광고 키입니다. ",required = true)
        @NotNull(groups = { OnVideoADRemove.class }, message = "광고 키 입력이 필요합니다.")
        @Size(min = 1,groups = { OnVideoADRemove.class }, message = "광고 키 입력이 하나 이상 필요 합니다.")
        private List<BigDecimal> videoAdIds;
    }
    /**
     * Value Object
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VideoADVO {
        @Schema(example = "1")
        private String videoAdId;
        @Schema(example = "테스트")
        private String title;
        @Schema(example = "테스트")
        private String contents;
        @Schema(example = "y")
        private String activationYn;
        @Schema(example = "n")
        private String delYn;
        @Schema(example = "2023-03-14 13:22:58")
        private String creationDt;
    }
}
