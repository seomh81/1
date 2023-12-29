package com.hdel.miri.concurrent.domain.dgk.vo;

import com.hdel.miri.concurrent.util.request.AbstractRequest;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

public class CcLogVO {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper=false)
    public static final class CcLogCreate extends AbstractRequest {
        @Schema(type = "Integer", example = "1", description = "정주기 로그 ID")
        @Hidden
        private Integer concurrentErrId;
        @Schema(type = "String", example = "정주기 일감 명", description = "정주기 일감 명")
        private String jobNm;
        @Schema(type = "String", example = "국가승강비 번호", description = "국가 승강기 번호")
        private String elNo;
        @Schema(type = "String", example = "입력파라메타", description = "입력파라메타")
        private String inParams;
        @Schema(type = "String", example = "에러유무", description = "에러유무")
        private String errorYn;
        @Schema(type = "String", example = "에러내역", description = "에러내역")
        private String errors;

        @Schema(type = "String", example = "등록자", description = "등록자")
        @Hidden
        private String creationUser;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper=false)
    public static final class CcLogUpdate extends AbstractRequest {
        @Schema(type = "Integer", example = "1", description = "정주기 로그 ID")
        private Integer concurrentErrId;
        @Schema(type = "String", example = "에러유무", description = "에러유무")
        private String errorYn;
        @Schema(type = "String", example = "에러내역", description = "에러내역")
        private String errors;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper=false)
    public static final class VO {
        private Integer concurrentErrId;
        private String jobNm;
        private String elevatorNo;
        private String errorYn;
        private String errors;
        private String inParams;
        private String creationDt;
        private String creationUser;
        private String lastupdateDt;
        private String lastupdateUser;
    }
}
