package com.hdel.miri.api.domain.privilege.view;

import com.hdel.miri.api.domain.privilege.view.valid.OnViewCreate;
import com.hdel.miri.api.domain.privilege.view.valid.OnViewDelete;
import com.hdel.miri.api.domain.privilege.view.valid.OnViewUpdate;
import com.hdel.miri.api.util.request.AbstractRequest;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

public class View {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewSearch extends AbstractRequest implements Serializable {
        @Schema(type = "string", example = "VIEW FILE 검색 키워드", description = "VIEW FILE 검색 키워드")
        private String searchKeyword;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewCreate extends AbstractRequest {
        @Hidden
        private BigDecimal viewId;

        @Schema(type = "String", example = "Notice", description = "View 이름")
        @NotEmpty(groups = { OnViewCreate.class }, message = "View 이름 입력이 필요합니다.")
        @NotNull(groups = { OnViewCreate.class }, message = "View 이름 입력이 필요합니다.")
        @NotBlank(groups = { OnViewCreate.class }, message = "View 이름 입력이 필요합니다.")
        private String viewName;

        @Schema(type = "String", example = "Notice", description = "View 타입")
        @NotEmpty(groups = { OnViewCreate.class }, message = "View 이름 입력이 필요합니다.")
        @NotNull(groups = { OnViewCreate.class }, message = "View 이름 입력이 필요합니다.")
        @NotBlank(groups = { OnViewCreate.class }, message = "View 이름 입력이 필요합니다.")
        private String type;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewUpdate extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "1", description = "View를 구별하는 Unique key입니다.",required = true)
        @NotNull(groups = { OnViewUpdate.class }, message = "View 키 입력이 필요합니다.")
        private BigDecimal viewId;

        @Schema(type = "String", example = "Notice", description = "View 이름")
        @NotEmpty(groups = { OnViewUpdate.class }, message = "View 이름 입력이 필요합니다.")
        @NotNull(groups = { OnViewUpdate.class }, message = "View 이름 입력이 필요합니다.")
        @NotBlank(groups = { OnViewUpdate.class }, message = "View 이름 입력이 필요합니다.")
        private String viewName;

        @Schema(type = "String", example = "Notice", description = "View 타입")
        @NotEmpty(groups = { OnViewUpdate.class }, message = "View 타입 입력이 필요합니다.")
        @NotNull(groups = { OnViewUpdate.class }, message = "View 타입 입력이 필요합니다.")
        @NotBlank(groups = { OnViewUpdate.class }, message = "View 타입 입력이 필요합니다.")
        private String type;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewDelete extends AbstractRequest {

        @Schema(type = "BigDecimal", example = "1", description = "View 를 구별하는 Unique key입니다.",required = true)
        @NotNull(groups = { OnViewDelete.class }, message = "View 키 입력이 필요합니다.")
        private BigDecimal viewId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewVO {
        @Schema(example = "1")
        private BigDecimal viewId;
        @Schema(example = "Notice")
        private String viewName;
        @Schema(example = "\"/admin/SetNotice.vue\"")
        private String vueFileUrl;
    }
}
