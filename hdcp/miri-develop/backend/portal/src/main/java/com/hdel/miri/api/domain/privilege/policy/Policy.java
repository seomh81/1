package com.hdel.miri.api.domain.privilege.policy;

import com.hdel.miri.api.domain.privilege.policy.valid.*;
import com.hdel.miri.api.util.request.AbstractRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

public class Policy {

    /**
     * View - Functions.
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FunctionsMappedByView extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "1", description = "View 를 구별하는 Unique key입니다.",required = true)
        @NotNull(groups = { OnFunctionsMappedByView.class }, message = "View 키 입력이 필요합니다.")
        private BigDecimal viewId;

        @Schema(type = "List<BigDecimal>", example = "[3,4,5]", description = "Function 을 구별하는 Unique key List입니다.",required = true)
        @NotNull(groups = { OnFunctionsMappedByView.class }, message = "Function 키 입력이 필요합니다.")
        @Size(min = 1,groups = { OnFunctionsMappedByView.class },message = "Function 키가 하나 이상 입력이 필요합니다.")
        private List<BigDecimal> functionIds;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FunctionsByView {
        @Schema(example = "1")
        private String step;
        @Schema(example = "2-4")
        private String treeId;
        @Schema(example = "2")
        private String parentId;
        @Schema(example = "null")
        private String viewId;
        @Schema(example = "4")
        private String functionId;
        @Schema(example = "null")
        private String viewName;
        @Schema(example = "null")
        private String vueFileUrl;
        @Schema(example = "function")
        private String type;
        @Schema(example = "삭제")
        private String functionName;
        @Schema(example = "BTN_REMOVE")
        private String methodName;
    }

    /**
     * View - VueFile.
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VueFileLink extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "1", description = "View를 구별하는 Unique key입니다.",required = true)
        @NotNull(groups = { OnVueFileLink.class }, message = "Vue File 키 입력이 필요합니다.")
        private BigDecimal viewId;

        @Schema(type = "String", example = "/system/userManagement.vue", description = "Vue 파일명",required = true)
        @NotEmpty(groups = { OnVueFileLink.class }, message = "Vue File URL 입력이 필요합니다.")
        @NotNull(groups = { OnVueFileLink.class }, message = "Vue File URL 입력이 필요합니다.")
        @NotBlank(groups = { OnVueFileLink.class }, message = "Vue File URL 입력이 필요합니다.")
        private String vueFileUrl;
    }

    /**
     * Menu - Views.
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewsMappedByMenu extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "1", description = "Menu 를 구별하는 Unique key입니다.",required = true)
        @NotNull(groups = { OnViewsMappedByMenu.class }, message = "Menu 키 입력이 필요합니다.")
        private BigDecimal menuId;

        @Schema(type = "List<BigDecimal>", example = "[3,4,5]", description = "View 를 구별하는 Unique key List입니다.",required = true)
        @NotNull(groups = { OnViewsMappedByMenu.class }, message = "View 키 입력이 필요합니다.")
        @Size(min = 1,groups = { OnViewsMappedByMenu.class },message = "View 키가 하나 이상 입력이 필요합니다.")
        private List<BigDecimal> viewIds;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewsByMenu {
        @Schema(example = "1")
        private String step;
        @Schema(example = "1")
        private String treeId;
        @Schema(example = "null")
        private String parentId;
        @Schema(example = "1")
        private String menuId;
        @Schema(example = "null")
        private String parentMenuId;
        @Schema(example = "System")
        private String menuName;
        @Schema(example = "null")
        private String payload;
        @Schema(example = "menu")
        private String type;
        @Schema(example = "null")
        private String viewId;
        @Schema(example = "null")
        private String viewName;
        @Schema(example = "null")
        private String vueFileUrl;
    }

    /**
     * Role - Users.
     * */

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsersMappedByRole extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "1", description = "Role 를 구별하는 Unique key입니다.",required = true)
        @NotNull(groups = { OnUsersMappedByRole.class }, message = "Role 키 입력이 필요합니다.")
        private BigDecimal roleId;

        @Schema(type = "List<BigDecimal>", example = "[test@aa.bb]", description = "User 를 구별하는 Unique key List 입니다.",required = true)
        @NotNull(groups = { OnUsersMappedByRole.class }, message = "User ID 키 입력이 필요합니다.")
        @Size(min = 1,groups = { OnUsersMappedByRole.class },message = "User ID가 하나 이상 입력이 필요합니다.")
        private List<String> userIds;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsersByRole extends AbstractRequest {
        @Schema(example = "1")
        private String step;
        @Schema(example = "1")
        private String treeId;
        @Schema(example = "null")
        private String parentId;
        @Schema(example = "1")
        private String roleId;
        @Schema(example = "null")
        private String parentRoleId;
        @Schema(example = "SYSTEM")
        private String roleName;
        @Schema(example = "null")
        private String payload;
        @Schema(example = "test@test.co.kr")
        private String userId;
        @Schema(example = "user")
        private String type;
    }

    /**
     * Role - Menus.
     * */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenusMappedByRole extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "1", description = "Role 를 구별하는 Unique key입니다.",required = true)
        @NotNull(groups = { OnMenusMappedByRole.class }, message = "Role 키 입력이 필요합니다.")
        private BigDecimal roleId;

        @Schema(type = "List<BigDecimal>", example = "[1,2,3,4]", description = "Menu 를 구별하는 Unique key List 입니다.",required = true)
        @NotNull(groups = { OnMenusMappedByRole.class }, message = "Menu ID 키 입력이 필요합니다.")
        @Size(min = 1,groups = { OnMenusMappedByRole.class },message = "Menu ID가 하나 이상 입력이 필요합니다.")
        private List<BigDecimal> menuIds;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenusByRole extends AbstractRequest {
        @Schema(example = "1")
        private String treeId;
        @Schema(example = "1")
        private String parentId;
        @Schema(example = "Menu1")
        private String name;
        @Schema(example = "menu")
        private String type;
        @Schema(example = "1")
        private String roleId;
        @Schema(example = "1")
        private String menuId;
        @Schema(example = "0")
        private String parentMenuId;
        @Schema(example = "null")
        private String payload;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Test {
        private String userId;
    }
}
