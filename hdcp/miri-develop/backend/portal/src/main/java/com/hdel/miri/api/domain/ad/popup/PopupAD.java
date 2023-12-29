package com.hdel.miri.api.domain.ad.popup;

import com.hdel.miri.api.domain.ad.popup.valid.OnPopupADCreate;
import com.hdel.miri.api.domain.ad.popup.valid.OnPopupADRemove;
import com.hdel.miri.api.domain.ad.popup.valid.OnPopupADUpdate;
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

public class PopupAD {
    /**
     * SearchOption
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PopupADSearch extends AbstractRequest {
        @Schema(type="String",example = "Video1",description = "PopupAD 이름 검색 키워드")
        private String searchKeyword;
        @Schema(type="String",example = "ALL|y|n",description = "PopupAD 활성화 상태")
        private String activationYn;
    }
    /**
     * Create
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PopupADCreate extends AbstractRequest {
        @Schema(type = "String", example = "광고명", description = "광고명 입니다. ")
        @NotEmpty(groups = { OnPopupADCreate.class }, message = "광고명 입력이 필요합니다.")
        @NotNull(groups = { OnPopupADCreate.class }, message = "광고명 입력이 필요합니다.")
        @NotBlank(groups = { OnPopupADCreate.class }, message = "광고명 입력이 필요합니다.")
        private String popupName;
        @Schema(type = "String", example = "내용", description = "광고 내용 입니다. ")
        @NotEmpty(groups = { OnPopupADCreate.class }, message = "광고 내용 입력이 필요합니다.")
        @NotNull(groups = { OnPopupADCreate.class }, message = "광고 내용 입력이 필요합니다.")
        @NotBlank(groups = { OnPopupADCreate.class }, message = "광고 내용 입력이 필요합니다.")
        private String contents;
    }
    /**
     * Update
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PopupADUpdate extends AbstractRequest {

        @Schema(type = "BigDecimal", example = "1", description = "광고 키 값 입니다. ")
        @NotNull(groups = { OnPopupADUpdate.class }, message = "광고 키 입력이 필요합니다.")
        private BigDecimal popupAdId;

        @Schema(type = "String", example = "광고명", description = "광고명 입니다. ")
        @NotEmpty(groups = { OnPopupADUpdate.class }, message = "광고명 입력이 필요합니다.")
        @NotNull(groups = { OnPopupADUpdate.class }, message = "광고명 입력이 필요합니다.")
        @NotBlank(groups = { OnPopupADUpdate.class }, message = "광고명 입력이 필요합니다.")
        private String popupName;
        @Schema(type = "String", example = "내용", description = "광고 내용 입니다. ")
        @NotEmpty(groups = { OnPopupADUpdate.class }, message = "광고 내용 입력이 필요합니다.")
        @NotNull(groups = { OnPopupADUpdate.class }, message = "광고 내용 입력이 필요합니다.")
        @NotBlank(groups = { OnPopupADUpdate.class }, message = "광고 내용 입력이 필요합니다.")
        private String contents;
    }

    /**
     * Activation
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PopupADActivation extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "[1,2,3,4]", description = "광고 아이디입니다. ")
        @NotNull(groups = { OnPopupADUpdate.class }, message = "광고 키 입력이 필요합니다.")
        @Size(min = 1,groups = { OnPopupADUpdate.class }, message = "광고 키 입력이 하나 이상 필요 합니다.")
        private List<BigDecimal> popupAdIds;
    }
    /**
     * Remove
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PopupADRemove extends AbstractRequest {
        @Schema(type = "List<BigDecimal>", example = "[1,2,3]", description = "영상 광고 아이디입니다. ")
        @NotNull(groups = { OnPopupADRemove.class }, message = "광고 키 입력이 필요합니다.")
        @Size(min = 1,groups = { OnPopupADRemove.class }, message = "광고 키 입력이 하나 이상 필요 합니다.")
        private List<BigDecimal> popupAdIds;
    }
    /**
     * Value Object
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PopupADVO {
        @Schema(example = "1")
        private String popupAdId;
        @Schema(example = "팝업이름")
        private String popupName;
        @Schema(example = "팝업 내용")
        private String contents;
        @Schema(example = "y")
        private String activationYn;
        @Schema(example = "n")
        private String delYn;
        @Schema(example = "2023-03-14 13:22:58")
        private String creationDt;
    }
}
