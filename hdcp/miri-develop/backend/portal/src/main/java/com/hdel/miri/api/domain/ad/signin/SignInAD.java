package com.hdel.miri.api.domain.ad.signin;

import com.hdel.miri.api.domain.ad.signin.valid.OnSignInADCreate;
import com.hdel.miri.api.domain.ad.signin.valid.OnSignInADRemove;
import com.hdel.miri.api.domain.ad.signin.valid.OnSignInADUpdate;
import com.hdel.miri.api.domain.storage.Storage;
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

public class SignInAD {

    /**
     * SearchOption
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignInADSearch extends AbstractRequest {
        @Schema(type="String",example = "Section1",description = "SignInAD 이름 검색 키워드")
        private String searchKeyword;
        @Schema(type="String",example = "y|n",description = "AD 활성화 상태 상태")
        private String activationYn;
    }

    /**
     * SearchOption - Detail
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignInADDetailSearch extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "1", description = "광고 키 입니다. ")
        private BigDecimal adId;
    }

    /**
     * Create
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignInADCreate extends AbstractRequest {
        @Schema(type = "String", example = "2022-10월 광고", description = "광고명입니다. ")
        @NotEmpty(groups = { OnSignInADCreate.class }, message = "광고명 입력이 필요합니다.")
        @NotNull(groups = { OnSignInADCreate.class }, message = "광고명 입력이 필요합니다.")
        @NotBlank(groups = { OnSignInADCreate.class }, message = "광고명 입력이 필요합니다.")
        private String adName;
    }

    /**
     * Create
     * */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignInADDetailCreate extends Storage.ImageSave {
        @Schema(type = "BigDecimal", example = "1", description = "광고 아이디입니다. ")
        @NotNull(groups = { OnSignInADCreate.class }, message = "광고 키 입력이 필요합니다.")
        private BigDecimal adId;
    }

    /**
     * Update
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignInADUpdate extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "1", description = "광고 아이디입니다. ")
        @NotNull(groups = { OnSignInADUpdate.class }, message = "광고명 입력이 필요합니다.")
        private BigDecimal adId;
        @Schema(type = "String", example = "2022-10월 광고", description = "광고명입니다. ")
        @NotEmpty(groups = { OnSignInADUpdate.class }, message = "광고명 입력이 필요합니다.")
        @NotNull(groups = { OnSignInADUpdate.class }, message = "광고명 입력이 필요합니다.")
        @NotBlank(groups = { OnSignInADUpdate.class }, message = "광고명 입력이 필요합니다.")
        private String adName;
    }
    /**
     * Activation
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignInADActivation extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "[1,2,3,4]", description = "광고 아이디입니다. ")
        @NotNull(groups = { OnSignInADUpdate.class }, message = "광고 아이디 입력이 필요합니다.")
        @Size(min = 1,groups = { OnSignInADUpdate.class },message = "하나 이상의 광고 아이디 입력이 필요합니다.")
        private List<BigDecimal> adIds;
    }
    /**
     * Delete - SignInAD
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignInADRemove extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "[1,2,3,4]", description = "광고 아이디입니다. ")
        @NotNull(groups = { OnSignInADRemove.class }, message = "광고 아이디 입력이 필요합니다.")
        @Size(min = 1,groups = { OnSignInADRemove.class },message = "하나 이상의 광고 아이디 입력이 필요합니다.")
        private List<BigDecimal> adIds;
    }

    /**
     * Delete - SignInAD
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignInADDetailDelete extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "1", description = "광고 상세 아이디입니다. ")
        @NotNull(groups = { OnSignInADRemove.class }, message = "광고 아이디 입력이 필요합니다.")
        private BigDecimal adDetailId;
    }

    /**
     * Value Object
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignInADVO {
        @Schema(type = "BigDecimal", example = "1", description = "광고 아이디입니다. ")
        private BigDecimal adId;
        @Schema(type = "String", example = "2022-10월 광고", description = "광고명 입니다. ")
        private String adName;
        @Schema(type="String",example = "y|n",description = "AD 활성화 상태 상태")
        private String activationYn;
        @Schema(type="String",example = "y|n",description = "AD 삭제 상태")
        private String delYn;
    }
    /**
     * Value Object
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignInADDetailVO {
        @Schema(example = "1")
        private BigDecimal adId;
        @Schema(example = "1")
        private BigDecimal adDetailId;
        @Schema(example = "c8bcdb2462bc47858bf649226e6c40d0fa405057772844129099bd2becd80e2b20230313102122")
        private String physicalFileName;
        @Schema(example = "다운로드.png")
        private String originalFileName;
    }

    /**
     * Value Object
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailInfo {
        @Schema(type = "String", example = "1", description = "원본 파일명 입니다. ")
        private String originalFileName;
        @Schema(type = "String", example = "c8bcdb2462bc47858bf649226e6c40d0fa405057772844129099bd2becd80e2b20230313102122", description = "저장 파일명 입니다.")
        private String physicalFileName;
        @Schema(type = "String", example = "/v2/..", description = "파일 URL 입니다.")
        private String url;
    }
}
