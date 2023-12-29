package com.hdel.miri.api.domain.privilege.vue;

import com.hdel.miri.api.domain.privilege.vue.valid.OnVueCreate;
import com.hdel.miri.api.util.request.AbstractRequest;
import com.hdel.miri.api.domain.privilege.vue.valid.OnVueDelete;
import com.hdel.miri.api.domain.privilege.vue.valid.OnVueUpdate;
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

public class Vue {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VueSearch extends AbstractRequest implements Serializable {
        @Schema(type = "String", example = "VUE FILE 검색 키워드", description = "VUE FILE 검색 키워드")
        private String searchKeyword;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VueCreate extends AbstractRequest implements Serializable {

        @Schema(type = "String", example = "vue", description = "VUE FILE 이름",required = true)
        @NotEmpty(groups = { OnVueCreate.class }, message = "Vue File 이름 입력이 필요합니다.")
        @NotNull(groups = { OnVueUpdate.class }, message = "Vue File 이름 입력이 필요합니다.")
        @NotBlank(groups = { OnVueUpdate.class }, message = "Vue File 이름 입력이 필요합니다.")
        private String vueName;

        @Schema(type = "String", example = "/system/userManagement.vue", description = "VUE FILE URL",required = true)
        @NotEmpty(groups = { OnVueUpdate.class }, message = "Vue File URL 입력이 필요합니다.")
        @NotNull(groups = { OnVueUpdate.class }, message = "Vue File URL 입력이 필요합니다.")
        @NotBlank(groups = { OnVueUpdate.class }, message = "Vue File URL 입력이 필요합니다.")
        private String vueFileUrl;
        @Schema(type = "String", example = "Type", description = "VUE FILE 유형",required = true)
        @NotEmpty(groups = { OnVueUpdate.class }, message = "Vue 타입 입력이 필요합니다.")
        @NotNull(groups = { OnVueUpdate.class }, message = "Vue 타입 입력이 필요합니다.")
        @NotBlank(groups = { OnVueUpdate.class }, message = "Vue 타입 입력이 필요합니다.")
        private String type;
        private String note;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VueUpdate extends AbstractRequest implements Serializable {

        @Schema(type = "BigDecimal", example = "1", description = "VUE FILE 키",required = true)
        @NotNull(groups = { OnVueUpdate.class }, message = "Vue File 키 입력이 필요합니다.")
        private BigDecimal vueFileId;

        @Schema(type = "String", example = "vue", description = "VUE FILE 이름",required = true)
        @NotEmpty(groups = { OnVueUpdate.class }, message = "Vue File 이름 입력이 필요합니다.")
        @NotNull(groups = { OnVueUpdate.class }, message = "Vue File 이름 입력이 필요합니다.")
        @NotBlank(groups = { OnVueUpdate.class }, message = "Vue File 이름 입력이 필요합니다.")
        private String vueName;

        @Schema(type = "String", example = "/system/userManagement.vue", description = "VUE FILE URL",required = true)
        @NotEmpty(groups = { OnVueUpdate.class }, message = "Vue File URL 입력이 필요합니다.")
        @NotNull(groups = { OnVueUpdate.class }, message = "Vue File URL 입력이 필요합니다.")
        @NotBlank(groups = { OnVueUpdate.class }, message = "Vue File URL 입력이 필요합니다.")
        private String vueFileUrl;

        @Schema(type = "String", example = "Type", description = "VUE FILE 유형",required = true)
        @NotEmpty(groups = { OnVueUpdate.class }, message = "Vue 타입 입력이 필요합니다.")
        @NotNull(groups = { OnVueUpdate.class }, message = "Vue 타입 입력이 필요합니다.")
        @NotBlank(groups = { OnVueUpdate.class }, message = "Vue 타입 입력이 필요합니다.")
        private String type;
        private String note;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VueDelete extends AbstractRequest implements Serializable {
        @Schema(type = "BigDecimal", example = "[1]", description = "VUE FILE 키",required = true)
        @NotNull(groups = { OnVueDelete.class }, message = "Vue File 키 입력이 필요합니다.")
        @Size(min = 1,groups = { OnVueDelete.class }, message = "Vue File 키가 하나 이상 필요합니다.")
        private List<BigDecimal> vueFileIds;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VueFile {
        @Schema(example = "1")
        private BigDecimal vueFileId;
        @Schema(example = "SetNotice")
        private String vueName;
        @Schema(example = "/admin/SetNotice.vue")
        private String vueFileUrl;
        @Schema(example = "file")
        private String type;
        @Schema(example = "null")
        private String note;
        @Schema(example = "2023-03-16 10:16:44")
        private String creationDt;
    }
}
