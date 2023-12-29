package com.hdel.miri.api.domain.faq;


import com.hdel.miri.api.domain.faq.valid.OnFAQCreate;
import com.hdel.miri.api.domain.faq.valid.OnFAQUpdate;
import com.hdel.miri.api.util.request.AbstractRequest;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class FAQ {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FAQSearchOption extends AbstractRequest implements Serializable {
        @Schema(type = "string", example = "검색 FAQ 키워드", description = "검색 FAQ 키워드")
        private String searchKeyword;
    }
    @Data
    @Builder
    public static class FAQSearchOptionMobile extends AbstractRequest implements Serializable {
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class FAQCreate extends AbstractRequest {
        @Schema(type = "String", example = "질문 입니다.", description = "질문")
        @NotEmpty(groups = { OnFAQCreate.class }, message = "질문 입력이 필요합니다.")
        @NotNull(groups = { OnFAQCreate.class }, message = "질문 입력이 필요합니다.")
        @NotBlank(groups = { OnFAQCreate.class }, message = "질문 입력이 필요합니다.")
        private String question;
        @Schema(type = "String", example = "답변 입니다.", description = "답변")
        @NotEmpty(groups = { OnFAQCreate.class }, message = "답변 입력이 필요합니다.")
        @NotNull(groups = { OnFAQCreate.class }, message = "답변 입력이 필요합니다.")
        @NotBlank(groups = { OnFAQCreate.class }, message = "답변 입력이 필요합니다.")
        private String answer;

    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class FAQUpdate extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "FAQ 키", description = "FAQ 키")
        @NotNull(groups = { OnFAQUpdate.class }, message = "키 입력이 필요합니다.")
        private BigDecimal faqId;
        @Schema(type = "String", example = "질문 입니다.", description = "질문")
        private String question;
        @Schema(type = "String", example = "답변 입니다.", description = "답변")
        private String answer;

    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class FAQRemove extends AbstractRequest {
        @NotNull(groups = { OnFAQCreate.class }, message = "삭제할 키 입력이 필요합니다.")
        @Size(min = 1,groups = { OnFAQCreate.class }, message = "삭제할 키가 하나 이상 필요합니다.")
        private List<BigDecimal> faqIds;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class FAQVO {
        @Schema(type = "BigDecimal", example = "1", description = "FAQ 키")
        private BigDecimal faqId;
        @Schema(type = "String", example = "질문 입니다.", description = "질문")
        private String question;
        @Schema(type = "String", example = "답변 입니다.", description = "답변")
        private String answer;
        @Schema(type = "String", example = "y", description = "삭제 여부")
        private String delYn;
        @Schema(type = "String", example = "2023-03-14", description = "생성 일자")
        private String creationDt;
        @Schema(type = "String", example = "2023-03-14", description = "수정 일자")
        private String lastupdateDt;
        @Schema(type = "String", example = "ahsxkx@gmail.com", description = "마지막 수정자")
        private String lastupdateUser;
    }
}
