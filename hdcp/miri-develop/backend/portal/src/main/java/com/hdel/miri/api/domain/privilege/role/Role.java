package com.hdel.miri.api.domain.privilege.role;

import com.hdel.miri.api.domain.privilege.role.valid.*;
import com.hdel.miri.api.util.request.AbstractRequest;
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
import java.math.BigDecimal;
import java.util.List;

public class Role {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoleCreate extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "3", description = "부모 Role를 구별하는 Unique key입니다.")
        private BigDecimal parentRoleId;

        @Schema(type = "String", example = "Role Name", description = "Role 이름")
        @NotEmpty(groups = { OnRoleCreate.class }, message = "Role 이름 입력이 필요합니다.")
        @NotNull(groups = { OnRoleCreate.class }, message = "Role 이름 입력이 필요합니다.")
        @NotBlank(groups = { OnRoleCreate.class }, message = "Role 이름 입력이 필요합니다.")
        private String roleName;

        @Schema(type = "String", example = "Type Name", description = "유형 이름")
        @NotEmpty(groups = { OnRoleCreate.class }, message = "Role 유형 입력이 필요합니다.")
        @NotNull(groups = { OnRoleCreate.class }, message = "Role 유형 입력이 필요합니다.")
        @NotBlank(groups = { OnVueUpdate.class }, message = "Role 유형 입력이 필요합니다.")
        private String type;

        @Schema(type = "String", example = "", description = "Payload")
        private String payload;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoleUpdate extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "3", description = "Role를 구별하는 Unique key입니다.")
        @NotNull(groups = { OnRoleUpdate.class }, message = "Role 키 입력이 필요합니다.")
        private BigDecimal roleId;

        @Schema(type = "String", example = "Role Name", description = "Role 이름")
        @NotEmpty(groups = { OnRoleUpdate.class }, message = "Role 이름 입력이 필요합니다.")
        @NotNull(groups = { OnRoleUpdate.class }, message = "Role 이름 입력이 필요합니다.")
        @NotBlank(groups = { OnRoleUpdate.class }, message = "Role 이름 입력이 필요합니다.")
        private String roleName;

        @Schema(type = "String", example = "Type Name", description = "유형 이름")
        @NotEmpty(groups = { OnRoleUpdate.class }, message = "Role 유형 입력이 필요합니다.")
        @NotNull(groups = { OnRoleUpdate.class }, message = "Role 유형 입력이 필요합니다.")
        @NotBlank(groups = { OnRoleUpdate.class }, message = "Role 유형 입력이 필요합니다.")
        private String type;

        @Schema(type = "String", example = "", description = "Payload")
        private String payload;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoleDelete extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "1", description = "Role를 구별하는 Unique key입니다.")
        @NotNull(groups = { OnRoleDelete.class }, message = "Role 키 입력이 필요합니다.")
        private BigDecimal roleId;
    }
    /**
     * RoleMove 프로시저 확인 필요
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoleMove extends AbstractRequest {
        @Schema(type = "List<BigDecimal>", example = "[1,2]", description = "소스 Role를 구별하는 Unique key입니다.")
        @NotNull(groups = { OnRoleMove.class }, message = "Role 키 입력이 필요합니다.")
        @Size(min = 1,groups = { OnRoleMove.class })
        private List<BigDecimal> srcRoleIds;

        @Schema(type = "BigDecimal", example = "1", description = "타겟 Role를 구별하는 Unique key입니다.")
        @NotNull(groups = { OnRoleMove.class }, message = "Role 키 입력이 필요합니다.")
        private BigDecimal tgtRoleId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoleCopy extends AbstractRequest {
        @Schema(type = "List<BigDecimal>", example = "[1,2]", description = "소스 Role를 구별하는 Unique key입니다.")
        @NotNull(groups = { OnRoleCopy.class }, message = "Role 키 입력이 필요합니다.")
        @Size(min = 1,groups = { OnRoleCopy.class },message = "하나 이상의 Role 키 입력이 필요합니다.")
        private List<BigDecimal> srcRoleIds;

        @Schema(type = "BigDecimal", example = "1", description = "타겟 Role를 구별하는 Unique key입니다.")
        @NotNull(groups = { OnRoleCopy.class }, message = "하나 이상의 Role 키 입력이 필요합니다.")
        private BigDecimal tgtRoleId;
    }
}
