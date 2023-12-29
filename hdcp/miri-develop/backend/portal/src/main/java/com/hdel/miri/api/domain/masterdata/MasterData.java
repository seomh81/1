package com.hdel.miri.api.domain.masterdata;

import com.hdel.miri.api.domain.masterdata.valid.*;
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
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class MasterData {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MasterDataSelect extends AbstractRequest implements Serializable {
        @Schema(type = "String", example = "마스터 키", description = "마스터 키 입니다.")
        @NotEmpty(groups = { OnMasterDataSelect.class }, message = "마스터 키 입력이 필요합니다.")
        @NotNull(groups = { OnMasterDataSelect.class }, message = "마스터 키 입력이 필요합니다.")
        @NotBlank(groups = { OnMasterDataSelect.class }, message = "마스터 키 입력이 필요합니다.")
        private String masterdataKey;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MasterDataHeadSearch extends AbstractRequest implements Serializable {
        @Schema(type = "string", example = "마스터명 키워드", description = "검색할 마스터명")
        private String searchKeyword;
        @Schema(type = "string", example = "마스터명 키워드", description = "검색할 마스터명")
        private String locale;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MasterDataDetailSearch extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "198", description = "하위 데이터 검색할 마스터 번호")
        @NotNull(groups = { OnMasterDataDetailSearch.class }, message = "마스터 키 입력이 필요합니다.")
        private BigDecimal masterdataId;
        /*
        @Schema(type = "string", example = "로케일 정보", description = "로케일 정보 입니다.")
        @NotNull(groups = { OnMasterDataDetailSearch.class }, message = "로케일 정보 입력이 필요합니다.")
        private String locale;*/
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MasterDataHeadCreate extends AbstractRequest {
        @Schema(type = "string", example = "ma0001", description = "마스터데이터를 구별하는 Unique 코드 입니다.")
        @NotNull(groups = {OnMasterDataHeadCreate.class}, message = "마스터데이터 코드 값이 필요합니다.")
        @NotBlank(groups = {OnMasterDataHeadCreate.class}, message = "마스터데이터 코드 값이 필요합니다.")
        @NotEmpty(groups = {OnMasterDataHeadCreate.class}, message = "마스터데이터 코드 값이 필요합니다.")
        private String masterdataKey;
        @Schema(type = "string", example = "공장구분", description = "마스터데이터 명 입니다.")
        @NotNull(groups = {OnMasterDataHeadCreate.class}, message = "마스터데이터명 입력이 필요합니다.")
        @NotBlank(groups = {OnMasterDataHeadCreate.class}, message = "마스터데이터명 입력이 필요합니다.")
        @NotEmpty(groups = {OnMasterDataHeadCreate.class}, message = "마스터데이터명 입력이 필요합니다.")
        private String masterdataName;
        @Schema(type = "string", example = "ko_kr", description = "Locale 정보입니다.")
        @NotNull(groups = {OnMasterDataHeadCreate.class}, message = "마스터데이터 로케일 입력이 필요합니다.")
        @NotBlank(groups = {OnMasterDataHeadCreate.class}, message = "마스터데이터 로케일 입력이 필요합니다.")
        @NotEmpty(groups = {OnMasterDataHeadCreate.class}, message = "마스터데이터 로케일 입력이 필요합니다.")
        private String locale;
        @Schema(type = "string", example = "공장구분입니다.", description = "비고란 입니다.")
        private String note;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MasterDataDetailCreate extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "1", description = "마스터데이터를 구별하는 헤드 키 값 입니다.")
        @NotNull(groups = {OnMasterDataDetailCreate.class}, message = "마스터데이터 헤드 키 값이 필요합니다.")
        private BigDecimal masterdataId;

        @Schema(type = "String", example = "XX_TYPE", description = "마스터데이터를 구별하는 코드 입니다.")
        @NotNull(groups = {OnMasterDataDetailCreate.class}, message = "마스터데이터 코드 값이 필요합니다.")
        @NotBlank(groups = {OnMasterDataDetailCreate.class}, message = "마스터데이터 코드 값이 필요합니다.")
        @NotEmpty(groups = {OnMasterDataDetailCreate.class}, message = "마스터데이터 코드 값이 필요합니다.")
        private String code;
        @Schema(type = "string", example = "전체", description = "Value1~5는 화면에 보여지거나 로직에 사용됩니다.")
        @NotNull(groups = {OnMasterDataDetailCreate.class},message = "마스터데이터 Value 1 코드 입력이 필요합니다.")
        @NotBlank(groups = {OnMasterDataDetailCreate.class},message = "마스터데이터 Value 1 코드 입력이 필요합니다.")
        @NotEmpty(groups = {OnMasterDataDetailCreate.class},message = "마스터데이터 Value 1 코드 입력이 필요합니다.")
        private String value1;
        private String value2;
        private String value3;
        private String value4;
        private String value5;
        private BigDecimal sortSeq;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MasterDataDetailImport extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "1", description = "마스터데이터를 구별하는 헤드 키 값 입니다.")
        @NotNull(groups = {OnMasterDataDetailCreate.class}, message = "마스터데이터 헤드 키 값이 필요합니다.")
        private BigDecimal masterdataId;

        List<MasterDataDetailCreate> detailList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MasterDataHeadUpdate extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "198", description = "마스터데이터 헤드 키 번호")
        @NotNull(groups = {OnMasterDataHeadUpdate.class}, message = "마스터데이터 키 값이 필요합니다.")
        private BigDecimal masterdataId;
        @Schema(type = "string", example = "공장구분", description = "마스터데이터 명 입니다.")
        @NotNull(groups = { OnMasterDataHeadUpdate.class }, message = "마스터데이터명 입력이 필요합니다.")
        @NotBlank(groups = { OnMasterDataHeadUpdate.class }, message = "마스터데이터명 입력이 필요합니다.")
        @NotEmpty(groups = { OnMasterDataHeadUpdate.class }, message = "마스터데이터명 입력이 필요합니다.")
        private String masterdataName;

        /**
        @Schema(type = "string", example = "ma0001", description = "마스터데이터를 구별하는 Unique key입니다.")
        private String masterdataKey;
        @Schema(type = "string", example = "ko_kr", description = "Locale 정보입니다.")
        private String locale;
        @Schema(type = "string", example = "공장구분입니다.", description = "비고란 입니다.")**/
        @Schema(type = "string", example = "설명 입니다.", description = "설명 입니다.")
        private String note;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MasterDataDetailUpdate extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "301", description = "마스터데이터를 구별하는 Unique key입니다.",required = true)
        @NotNull(groups = {OnMasterDataDetailUpdate.class},message = "마스터데이터 상세 키 입력이 필요합니다.")
        private BigDecimal masterdataDetailId;

        @Schema(type = "String", example = "0001", description = "마스터데이터 구별하는 코드 입니다.",required = true)
        @NotNull(groups = {OnMasterDataDetailUpdate.class},message = "마스터데이터 상세 코드 입력이 필요합니다.")
        @NotBlank(groups = {OnMasterDataDetailUpdate.class},message = "마스터데이터 상세 코드 입력이 필요합니다.")
        @NotEmpty(groups = {OnMasterDataDetailUpdate.class},message = "마스터데이터 상세 코드 입력이 필요합니다.")
        private String code;

        @Schema(type = "String", example = "OXOX", description = "마스터데이터 Value 1 코드 입니다.",required = true)
        @NotNull(groups = {OnMasterDataDetailUpdate.class},message = "마스터데이터 Value 1 코드 입력이 필요합니다.")
        @NotBlank(groups = {OnMasterDataDetailUpdate.class},message = "마스터데이터 Value 1 코드 입력이 필요합니다.")
        @NotEmpty(groups = {OnMasterDataDetailUpdate.class},message = "마스터데이터 Value 1 코드 입력이 필요합니다.")
        private String value1;
        private String value2;
        private String value3;
        private String value4;
        private String value5;
        private BigDecimal sortSeq;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MasterDataHeadDelete extends AbstractRequest {
        @Schema(type = "List<BigDecimal>", example = "[1,2,3]", description = "마스터데이터 아이디",required = true)
        @NotNull(groups = {OnMasterDataHeadDelete.class}, message = "마스터데이터키 값이 필요합니다.")
        @Size(min = 1,groups = { OnMasterDataHeadDelete.class}, message = "하나 이상의 마스터데이터 키 값이 필요합니다.")
        private List<BigDecimal> masterdataIds;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MasterDataDetailDelete extends AbstractRequest {
        @Schema(type = "List<BigDecimal>", example = "[1,2,3]", description = "마스터데이터 상세 키",required = true)
        @NotNull(groups = {OnMasterDataDetailDelete.class}, message = "마스터데이터 상세 키 값이 필요합니다.")
        @Size(min = 1,groups = { OnMasterDataDetailDelete.class}, message = "하나 이상의 마스터데이터 상세 키 값이 필요합니다.")
        private List<BigDecimal> masterdataDetailIds;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlarmCode {
        private String catCode;
        private String divCode;
        private String typeCode;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MasterDataHeadVO {
        @Schema(example = "16")
        private BigDecimal masterdataId;
        @Schema(example = "ACCOUNT_STATUS")
        private String masterdataKey;
        @Schema(example = "유저 상태")
        private String masterdataName;
        @Schema(example = "ko_kr")
        private String locale;
        @Schema(example = "null")
        private String note;
        @Schema(example = "2023-02-17 04:27:23")
        private String creationDt;
        @Schema(example = "2023-02-17 04:27:23")
        private String lastupdateDt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MasterDataDetailVO {
        @Schema(example = "1")
        private BigDecimal masterdataId;
        @Schema(example = "123")
        private BigDecimal masterdataDetailId;
        @Schema(example = "101103")
        private String code;
        @Schema(example = "MSG Push 알람")
        private String value1;
        @Schema(example = "0010")
        private String value2;
        @Schema(example = "0011")
        private String value3;
        @Schema(example = "null")
        private String value4;
        @Schema(example = "null")
        private String value5;
        @Schema(example = "null")
        private BigDecimal sortSeq;
        @Schema(example = "2023-03-08 16:41:35")
        private String creationDt;
        @Schema(example = "2023-03-08 16:41:35")
        private String lastupdateDt;
    }
}
