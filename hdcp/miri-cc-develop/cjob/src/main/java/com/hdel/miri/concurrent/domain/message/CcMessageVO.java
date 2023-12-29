package com.hdel.miri.concurrent.domain.message;

import java.util.List;
import com.hdel.miri.concurrent.util.request.AbstractRequest;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

public class CcMessageVO {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper=false)
    public static final class CcSourceVO extends AbstractRequest {
        @Schema(type = "String", example = "123456", description = "통합프로젝트 코드")
        private String intgPrjNo;

        @Schema(type = "String", example = "A01", description = "거래선 코드")
        private String trlineCd;

        @Schema(type = "String", example = "123456L01", description = "원프로젝트코드+호기번호")
        private String prjHoNo;

        @Schema(type = "String", example = "현장명", description = "현장명")
        private String siteNm;

        @Schema(type = "String", example = "접수유형", description = "접수유형")
        private String reqType;

        @Schema(type = "String", example = "접수번호", description = "접수번호")
        private String reqNo;

        @Schema(type = "String", example = "담당기사", description = "담당가시")
        @Hidden
        private String assignedEngineerNm;

        @Schema(type = "String", example = "고객명", description = "고객명")
        @Hidden
        private String customerNm;

        @Schema(type = "String", example = "고객번호", description = "고객번호")
        @Hidden
        private String customerNo;

        @Schema(type = "String", example = "고객주소", description = "고객주소")
        @Hidden
        private String customerAddress;

        @Schema(type = "String", example = "계약만료일", description = "계약만료일")
        @Hidden
        private String contractEndDt;

        @Schema(type = "String", example = "지사명", description = "지사명")
        @Hidden
        private String branchNm;

        @Schema(type = "String", example = "지사전번", description = "지사전번")
        @Hidden
        private String branchTelNo;

        @Schema(type = "String", example = "공지사항 제목", description = "공지사항 제목")
        @Hidden
        private String title;

        @Schema(type = "String", example = "미납 총액", description = "미납 총액")
        @Hidden
        private String unbilledTotal;

        @Schema(type = "String", example = "자체점검일", description = "자체점검일")
        @Hidden
        private String selChkBeginDate;

        @Schema(type = "List<String>", example = "[\"MANAGER\"]", description = "권한대상")
        private List<String> privList;

        @Hidden
        private String divType;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper=false)
    public static final class CcMMSVO {
        @Schema(type = "String", example = "00011111111", description = "수신자 전화번호")
        private String receiverPhoneNo;

        @Schema(type = "String", example = "테스트", description = "메세지 제목")
        private String title;

        @Schema(type = "String", example = "내용", description = "내용")
        private String contents;

        @Schema(type = "String", example = "callback", description = "Callback번호")
        @Builder.Default
        private String callback = "15770603";
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper=false)
    public static final class CcKakaoVO {
        @Schema(type = "String", example = "xfijwoifjweofiwejf", description = "Yellokey")
        @Hidden
        private String yellowidKey;

        @Schema(type = "String", example = "타입", description = "알림톡")
        @Builder.Default
        private String type="KAT";

        @Schema(type = "String", example = "LMS", description = "재전송")
        @Builder.Default
        private String resend="LMS";

        @Schema(type = "String", example = "portal_fail_reserve", description = "템플릿코드")
        private String tmplCd;

        @Schema(type = "String", example = "전송메세지", description = "전송메세지")
        private String msg;

        @Schema(type = "String", example = "재전송 내용", description = "재전송 제목")
        private String lmsSubject;

        @Schema(type = "String", example = "수신번호", description = "수신번호")
        private String phone;

        @Schema(type = "String", example = "callback", description = "Callback번호")
        @Builder.Default
        private String callback = "15770603";
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper=false)
    public static class MailVO {
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

    // response vo
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper=false)
    public static class GetSubscriberListVO {
        @Schema(type = "String", example = "gimapei@gmail.com", description = "사용자 아이디")
        private String userId;

        @Schema(type = "String", example = "사용자명 ", description = "사용자명")
        private String userName;

        @Schema(type = "String", example = "123456A01", description = "통합프로젝트+거래선 코드")
        private String intgPrjTrlineCdCode;

        @Schema(type = "String", example = "fowifjweoifjweoiwjoiwef", description = "Google firebaseId")
        private String firebaseUserId;

        @Schema(type = "String", example = "00011112222", description = "전화번호")
        private String phonenumber;

        @Schema(type = "String", example = "1001", description = "고장접수")
        private String div;

        @Schema(type = "String", example = "고장접수", description = "고장접수")
        private String divNm;

        @Schema(type = "String", example = "01", description = "EMAIL")
        private String alarmType;

        @Schema(type = "String", example = "EMAIL", description = "EMAIL")
        private String alarmNm;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MsgTemplateVO {
        @Schema(example = "1")
        private String msgTemplateId;

        @Schema(example = "테플릿명")
        private String msgTemplateName;

        @Schema(example = "템플릿 소개")
        private String msgTemplateNote;

        @Schema(example = "템플릿 내용입니다. ")
        private String msgTemplateContents;

        @Schema(example = "2023-04-03 17:00:00")
        private String registDt;

        @Schema(example = "2023-04-03 17:00:00")
        private String updateDt;
    }
}
