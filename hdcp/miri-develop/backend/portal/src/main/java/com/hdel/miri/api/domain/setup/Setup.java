package com.hdel.miri.api.domain.setup;

import com.hdel.miri.api.domain.setup.valid.OnChangeLocale;
import com.hdel.miri.api.util.request.AbstractRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Setup {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SetupDefaultCreate extends AbstractRequest {
        @Schema(type = "String", example = "사용자 아이디", description = "사용자 아이디")
        private String userId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SetupLocaleUpdate extends AbstractRequest {
        @Schema(type = "String", example = "ko_kr", description = "변경할 사용자 로케일 정보",required = true)
        @NotEmpty(groups = { OnChangeLocale.class }, message = "변경할 사용자 로케일 정보 입력이 필요합니다.")
        @NotNull(groups = { OnChangeLocale.class }, message = "변경할 사용자 로케일 정보 입력이 필요합니다.")
        @NotBlank(groups = { OnChangeLocale.class }, message = "변경할 사용자 로케일 정보 입력이 필요합니다.")
        private String locale;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SetupThemeUpdate extends AbstractRequest {
        @Schema(type = "String", example = "light", description = "변경할 사용자 테마 정보",required = true)
        @NotEmpty(groups = { OnChangeLocale.class }, message = "변경할 사용자 테마 정보 입력이 필요합니다.")
        @NotNull(groups = { OnChangeLocale.class }, message = "변경할 사용자 테마 정보 입력이 필요합니다.")
        @NotBlank(groups = { OnChangeLocale.class }, message = "변경할 사용자 테마 정보 입력이 필요합니다.")
        private String theme;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SetupLandingPageUpdate extends AbstractRequest {
        @Schema(type = "String", example = "map_page", description = "변경할 사용자 랜딩 페이지 정보",required = true)
        @NotEmpty(groups = { OnChangeLocale.class }, message = "변경할 사용자 랜딩 페이지 정보 입력이 필요합니다.")
        @NotNull(groups = { OnChangeLocale.class }, message = "변경할 사용자 랜딩 페이지 정보 입력이 필요합니다.")
        @NotBlank(groups = { OnChangeLocale.class }, message = "변경할 사용자 랜딩 페이지 정보 입력이 필요합니다.")
        private String landingpageType;
    }
}
