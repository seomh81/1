package com.hdel.miri.api.domain.notice;

import com.hdel.miri.api.domain.notice.valid.OnNoticeCreate;
import com.hdel.miri.api.domain.notice.valid.OnNoticeRemove;
import com.hdel.miri.api.domain.notice.valid.OnNoticeUpdate;
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
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class Notice {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoticeSearch extends AbstractRequest implements Serializable {
        @Schema(type = "String", example = "공지사항 타이틀 키워드", description = "공지사항 타이틀 키워드")
        private String searchKeyword;
    }

    @Data
    @Builder
    public static class NoticeSearchMobile extends AbstractRequest implements Serializable {
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoticeCreate extends AbstractRequest {
        @Schema(type = "String", example = "공지사항 제목", description = "공지사항 타이틀",required = true)
        @NotEmpty(groups = { OnNoticeCreate.class }, message = "공지사항 타이틀 입력이 필요합니다.")
        @NotNull(groups = { OnNoticeCreate.class }, message = "공지사항 타이틀 입력이 필요합니다.")
        @NotBlank(groups = { OnNoticeCreate.class }, message = "공지사항 타이틀 입력이 필요합니다.")
        private String title;
        @Schema(type = "String", example = "공지사항 입니다.", description = "공지사항 내용",required = true)
        @NotEmpty(groups = { OnNoticeCreate.class }, message = "공지사항 내용 입력이 필요합니다.")
        @NotNull(groups = { OnNoticeCreate.class }, message = "공지사항 내용 입력이 필요합니다.")
        @NotBlank(groups = { OnNoticeCreate.class }, message = "공지사항 내용 입력이 필요합니다.")
        private String contents;
        @Schema(type = "String", example = "181734B01", description = "공지사항 자산코드",required = true)
        private String intgProjectTrlineCdCode;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoticeUpdate extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "1", description = "공지사항 키 값",required = true)
        @NotNull(groups = { OnNoticeUpdate.class }, message = "공지사항 키 값 입력이 필요합니다.")
        private BigDecimal noticeId;

        @Schema(type = "String", example = "공지사항 제목", description = "공지사항 타이틀",required = true)
        @NotEmpty(groups = { OnNoticeUpdate.class }, message = "공지사항 타이틀 입력이 필요합니다.")
        @NotNull(groups = { OnNoticeUpdate.class }, message = "공지사항 타이틀 입력이 필요합니다.")
        @NotBlank(groups = { OnNoticeUpdate.class }, message = "공지사항 타이틀 입력이 필요합니다.")
        private String title;
        @Schema(type = "String", example = "공지사항 입니다.", description = "공지사항 내용",required = true)
        @NotEmpty(groups = { OnNoticeUpdate.class }, message = "공지사항 내용 입력이 필요합니다.")
        @NotNull(groups = { OnNoticeUpdate.class }, message = "공지사항 내용 입력이 필요합니다.")
        @NotBlank(groups = { OnNoticeUpdate.class }, message = "공지사항 내용 입력이 필요합니다.")
        private String contents;
        @Schema(type = "String", example = "181734B01", description = "공지사항 자산코드",required = true)
        private String intgProjectTrlineCdCode;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoticeRemove extends AbstractRequest {
        @Schema(type = "List<BigDecimal>", example = "[1,2,3]", description = "공지사항 키 값",required = true)
        @NotNull(groups = { OnNoticeRemove.class }, message = "공지사항 키 값 입력이 필요합니다.")
        @Size(min = 1,groups = { OnNoticeRemove.class }, message = "하나 이상의 공지사항 키 값 입력이 필요합니다.")
        private List<BigDecimal> noticeIds;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoticeVO {
        @Schema(example = "2")
        private BigDecimal noticeId;
        @Schema(example = "제목")
        private String title;
        @Schema(example = "내용 입니다.")
        private String contents;
        @Schema(example = "136694A01")
        private String intgProjectTrlineCdCode;
        @Schema(example = "n")
        private String delYn;
        @Schema(example = "2023-03-14 11:52:50")
        private String creationDt;
        @Schema(example = "2023-03-14 11:53:15")
        private String lastupdateDt;
        @Schema(example = "test@test.co.kr")
        private String lastupdateUser;
    }
}
