package com.hdel.miri.api.domain.mail;

import com.hdel.miri.api.util.request.AbstractRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class MailVO {


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request extends AbstractRequest {

        @Schema(type = "String", example = "gimapei@gmail.com", description = "보내는 사람 이메일 주소입니다. ")
        private String fromAddress;

        @Schema(type = "List<String>", example = "gimapei@gmail.com,gimapei@kakao.com", description = "받는 사람 이메일 주소입니다. ")
        private List<String> toAddress;

        @Schema(type = "List<String>", example = "gimapei@gmail.com,gimapei@kakao.com", description = "참조 이메일 주소입니다. ")
        private List<String> ccAddress;

        @Schema(type = "List<String>", example = "gimapei@gmail.com,gimapei@kakao.com", description = "숨은참조 사람 이메일 주소입니다. ")
        private List<String> bccAddress;

        @Schema(type = "String", example = "현대엘리베이터 테스트 이메일입니다. ", description = "메일 제목 입니다. ")
        private String subject;

        @Schema(type = "String", example = "반갑습니다. <br> 메일 내역입니다.", description = "메일 내용입니다.")
        private String content;

        @Schema(type = "List<MultipartFile>", example = "첨부파일입니다.", description = "첨부파일입니다.")
        List<MultipartFile> attachFiles;

        @Schema(type = "Boolean", example = "true", description = "메일내용이 HTML여부인지 확인입니다.")
        private boolean isHTML;

        public int getAttachSize(){
            return (this.attachFiles==null ? 0 : this.attachFiles.size());
        }
    }
}
