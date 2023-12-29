package com.hdel.miri.concurrent.domain.mms;

import com.hdel.miri.concurrent.util.request.AbstractRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

//2023-10-13 add
public class mmsVO {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class request extends AbstractRequest{
        @Schema(type = "String", example = "01099998888", description = "문자 받을 번호")
        private String receiveNo;
        @Schema(type = "String", example = "제목", description = "문자 제목")
        private String title;
        @Schema(type = "String", example = "내용", description = "문자 내용")
        private String contents;
    }
}
