package com.hdel.miri.api.domain.terms;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Terms {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TermsDefaultCreate {
        @Schema(type = "String", example = "사용자 아이디", description = "사용자 아이디")
        private String userId;

        private String serviceUseAg;
        private String plInfoUsingAg;
        private String plInfoStoreTimeAg;
        private String adRecvAg;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TermsUpdate {
        @Schema(type = "String", example = "사용자 아이디", description = "사용자 아이디")
        private String userId;
        private String plInfoStoreTimeAg;
        private String adRecvAg;
    }
}
