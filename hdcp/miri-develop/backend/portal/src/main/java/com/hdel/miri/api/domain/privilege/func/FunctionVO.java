package com.hdel.miri.api.domain.privilege.func;

import com.hdel.miri.api.domain.privilege.func.valid.OnFunctionCreate;
import com.hdel.miri.api.domain.privilege.func.valid.OnFunctionDelete;
import com.hdel.miri.api.domain.privilege.func.valid.OnFunctionUpdate;
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

public class FunctionVO {


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FunctionSearch extends AbstractRequest implements Serializable {
        @Schema(type = "String", example = "Function FILE 검색 키워드", description = "Function FILE 검색 키워드")
        private String searchKeyword;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FunctionCreate extends AbstractRequest {

        @Schema(type = "String", example = "FunctionName", description = "Function 이름",required = true)
        @NotEmpty(groups = { OnFunctionCreate.class }, message = "Function 이름 입력이 필요합니다.")
        @NotNull(groups = { OnFunctionCreate.class }, message = "Function 이름 입력이 필요합니다.")
        @NotBlank(groups = { OnFunctionCreate.class }, message = "Function 이름 입력이 필요합니다.")
        private String functionName;

        @Schema(type = "String", example = "MethodName", description = "Method 이름",required = true)
        @NotEmpty(groups = { OnFunctionCreate.class }, message = "Method 이름 입력이 필요합니다.")
        @NotNull(groups = { OnFunctionCreate.class }, message = "Method 이름 입력이 필요합니다.")
        @NotBlank(groups = { OnFunctionCreate.class }, message = "Method 이름 입력이 필요합니다.")
        private String methodName;

        @Schema(type = "String", example = "FunctionName", description = "Function 유형",required = true)
        @NotEmpty(groups = { OnFunctionCreate.class }, message = "Function 유형 입력이 필요합니다.")
        @NotNull(groups = { OnFunctionCreate.class }, message = "Function 유형 입력이 필요합니다.")
        @NotBlank(groups = { OnFunctionCreate.class }, message = "Function 유형 입력이 필요합니다.")
        private String type;

        @Schema(type = "String", example = "메모", description = "메모 내용")
        private String note;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FunctionUpdate extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "1", description = "Function 키",required = true)
        @NotNull(groups = { OnFunctionUpdate.class }, message = "Function 키 입력이 필요합니다.")
        private BigDecimal functionId;

        @Schema(type = "String", example = "FunctionName", description = "Function 이름",required = true)
        @NotEmpty(groups = { OnFunctionUpdate.class }, message = "Function 이름 입력이 필요합니다.")
        @NotNull(groups = { OnFunctionUpdate.class }, message = "Function 이름 입력이 필요합니다.")
        @NotBlank(groups = { OnFunctionUpdate.class }, message = "Function 이름 입력이 필요합니다.")
        private String functionName;

        @Schema(type = "String", example = "MethodName", description = "Method 이름",required = true)
        @NotEmpty(groups = { OnFunctionUpdate.class }, message = "Method 이름 입력이 필요합니다.")
        @NotNull(groups = { OnFunctionUpdate.class }, message = "Method 이름 입력이 필요합니다.")
        @NotBlank(groups = { OnFunctionUpdate.class }, message = "Method 이름 입력이 필요합니다.")
        private String methodName;

        @Schema(type = "String", example = "FunctionType", description = "Function 유형",required = true)
        @NotEmpty(groups = { OnFunctionUpdate.class }, message = "Function 유형 입력이 필요합니다.")
        @NotNull(groups = { OnFunctionUpdate.class }, message = "Function 유형 입력이 필요합니다.")
        @NotBlank(groups = { OnFunctionUpdate.class }, message = "Function 유형 입력이 필요합니다.")
        private String type;

        @Schema(type = "String", example = "메모", description = "메모 내용")
        private String note;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FunctionDelete extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "{\"functionIds\":[1,2,3]}", description = "Function 키",required = true)
        @NotNull(groups = { OnFunctionDelete.class }, message = "Function 키 입력이 필요합니다.")
        @Size(min = 1,groups = { OnFunctionDelete.class }, message = "Function 키가 하나 이상 필요합니다.")
        private List<BigDecimal> functionIds;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Function {
        @Schema(example = "1")
        private BigDecimal functionId;
        @Schema(example = "조회")
        private String functionName;
        @Schema(example = "BTN_SERACH")
        private String methodName;
        @Schema(example = "function")
        private String type;
        @Schema(example = "어떠한 기능 입니다.")
        private String note;
        @Schema(example = "2023-03-16 10:13:18")
        private String creationDt;
    }
}
