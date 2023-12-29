package com.hdel.miri.api.domain.privilege.menu;

import com.hdel.miri.api.domain.privilege.menu.valid.*;
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

public class Menu {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuCreate extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "3", description = "부모 키")
        private BigDecimal parentMenuId;

        @Schema(type = "String", example = "Test", description = "메뉴 이름",required = true)
        @NotEmpty(groups = { OnMenuCreate.class }, message = "메뉴 이름 입력이 필요합니다.")
        @NotNull(groups = { OnMenuCreate.class }, message = "메뉴 이름 입력이 필요합니다.")
        @NotBlank(groups = { OnMenuCreate.class }, message = "메뉴 이름 입력이 필요합니다.")
        private String menuName;

        @Schema(type = "String", example = "Test", description = "메뉴 유형",required = true)
        @NotEmpty(groups = { OnMenuCreate.class }, message = "메뉴 유형 입력이 필요합니다.")
        @NotNull(groups = { OnMenuCreate.class }, message = "메뉴 유형 입력이 필요합니다.")
        @NotBlank(groups = { OnMenuCreate.class }, message = "메뉴 유형 입력이 필요합니다.")
        private String type;

        @Schema(type = "String", example = "", description = "payload")
        private String payload;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuUpdate extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "3", description = "Menu를 구별하는 Unique key입니다.",required = true)
        @NotNull(groups = { OnMenuUpdate.class }, message = "메뉴 키 입력이 필요합니다.")
        private BigDecimal menuId;

        @Schema(type = "String", example = "Test", description = "메뉴 이름",required = true)
        @NotEmpty(groups = { OnMenuUpdate.class }, message = "메뉴 이름 입력이 필요합니다.")
        @NotNull(groups = { OnMenuUpdate.class }, message = "메뉴 이름 입력이 필요합니다.")
        @NotBlank(groups = { OnMenuUpdate.class }, message = "메뉴 이름 입력이 필요합니다.")
        private String menuName;

        @Schema(type = "String", example = "Test", description = "메뉴 유형",required = true)
        @NotEmpty(groups = { OnMenuUpdate.class }, message = "메뉴 유형 입력이 필요합니다.")
        @NotNull(groups = { OnMenuUpdate.class }, message = "메뉴 유형 입력이 필요합니다.")
        @NotBlank(groups = { OnMenuUpdate.class }, message = "메뉴 유형 입력이 필요합니다.")
        private String type;

        @Schema(type = "String", example = "", description = "payload")
        private String payload;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuDelete extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "1", description = "Menu를 구별하는 Unique key입니다.",required = true)
        @NotNull(groups = { OnMenuDelete.class }, message = "메뉴 키 입력이 필요합니다.")
        private BigDecimal menuId;
    }
    /**
    * MenuMove 프로시저 확인 필요
    * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuMove extends AbstractRequest {
        @Schema(type = "List<BigDecimal>", example = "[1,2]", description = "소스 Menu를 구별하는 Unique key입니다.",required = true)
        @NotNull(groups = { OnMenuMove.class }, message = "소스 메뉴 키 입력이 필요합니다.")
        @Size(min = 1,groups = { OnMenuMove.class }, message = "소스 메뉴 키가 하나 이상 필요합니다.")
        private List<BigDecimal> srcMenuIds;

        @Schema(type = "BigDecimal", example = "1", description = "타겟 Menu를 구별하는 Unique key입니다.",required = true)
        @NotNull(groups = { OnMenuMove.class }, message = "타깃 메뉴 키 입력이 필요합니다.")
        private BigDecimal tgtMenuId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuCopy extends AbstractRequest {
        @Schema(type = "List<BigDecimal>", example = "[1,2]", description = "소스 Menu를 구별하는 Unique key입니다.",required = true)
        @NotNull(groups = { OnMenuCopy.class }, message = "소스 메뉴 키 입력이 필요합니다.")
        private List<BigDecimal> srcMenuIds;

        @Schema(type = "BigDecimal", example = "1", description = "타겟 Menu를 구별하는 Unique key입니다.",required = true)
        @NotNull(groups = { OnMenuCopy.class }, message = "타깃 메뉴 키 입력이 필요합니다.")
        private BigDecimal tgtMenuId;
    }

}
