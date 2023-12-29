package com.hdel.miri.concurrent.domain.user; 

import com.hdel.miri.concurrent.domain.contract.Contract;
import com.hdel.miri.concurrent.domain.user.vo.valid.*;
import com.hdel.miri.concurrent.util.request.AbstractRequest;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

//2023-10-13 add
public class User {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserAlready extends AbstractRequest {
        private String userId;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRenderingRequest extends AbstractRequest {
        @Hidden
        private String userId;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserAlreadyExt {
        @Schema(type = "string", example = "test@aa.bb", description = "사용자 아이디")
        private String userId;
        @Hidden
        private Boolean result;
    }


    /**
    * Create
    * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DefaultUserCreate extends AbstractRequest {
        @Schema(type = "Integer", example = "1233454", description = "요청번호-순번")
        private Integer seqNo;

        @Schema(type = "String", example = "test@aa.bb", description = "사용자 아이디")
        @NotEmpty(groups = { OnUserCreate.class }, message = "사용자 아이디 입력이 필요합니다.")
        @NotNull(groups = { OnUserCreate.class }, message = "사용자 아이디  입력이 필요합니다.")
        @NotBlank(groups = {OnUserCreate.class}, message = "사용자 아이디 입력이 필요합니다.")
        @Email(groups = {OnUserCreate.class}, message = "아이디가 이메일 형식이 아닙니다.")
        private String userId;

        @Schema(type = "String", example = "홍길동", description = "사용자 이름")
        @NotEmpty(groups = { OnUserCreate.class }, message = "사용자이름 입력이 필요합니다.")
        @NotNull(groups = { OnUserCreate.class }, message = "사용자이름  입력이 필요합니다.")
        @NotBlank(groups = { OnUserCreate.class }, message = "사용자이름 입력이 필요합니다.")
        private String userName;

        @Schema(type = "String", example = "1234", description = "사용자 비밀번호")
        @NotEmpty(groups = { OnUserCreate.class }, message = "사용자 비밀번호 입력이 필요합니다.")
        @NotNull(groups = { OnUserCreate.class }, message = "사용자 비밀번호  입력이 필요합니다.")
        @NotBlank(groups = {OnUserCreate.class}, message = "사용자 비밀번호 입력이 필요합니다.")
        private String password;
        
        @Hidden
        private String originPassword;
        @Schema(type = "String", example = "123-123", description = "우편 번호")
        private String postnumber;

        @Schema(type = "String", example = "대한민국 어딘가", description = "사용자 주소정보")
        private String address;

        @Schema(type = "String", example = "대한민국 어딘가", description = "사용자 주소정보")
        private String detailaddress;

        @Schema(type = "String", example = "대한민국 어딘가", description = "특이사항")
        private String note;

        @Schema(type = "String", example = "010-0000-0000", description = "사용자 전화정보")
        @NotBlank(message = "사용자 전화 정보가 필요합니다.", groups = {OnUserCreate.class})
        private String phonenumber;

        @Schema(type = "String", example = "y | n", description = "서비스 이용 동의")
        private String termsServiceUseAg;

        @Schema(type = "String", example = "y | n", description = "정보 이용 동의")
        private String termsPlInfoUsingAg;

        @Schema(type = "String", example = "y | n", description = "개인정보 유효 5년 동의")
        private String termsPlInfoStoreTimeAg;

        @Schema(type = "String", example = "y | n", description = "광고 수신 동의")
        private String termsAdRecvAg;

        @Schema(type = "List<ContractSummary>", example = "[{\n" +
                "        \"intgPrjNo\":\"a123455\"\n" +
                "        , \"trlineCd\":\"A01\"\n" +
                "        , \"model\":\"WBSS\"\n" +
                "        , \"siteName\" : \"파랑주간보호센터\"\n" +
                "        , \"addr\":\"전라북도 전주시..\"\n" +
                "    }]", description = "Default Portfolio.")
        private List<User.ContractAPI> lobby;

        @Schema(type = "String", example = "xdjfoiwejfoweifw", description = "구글 파이어베이스 사용자 아이디")
        private String firebaseUserId;

        @Schema(type = "String", example = "xdjfoiwejfoweifw", description = "유상계약 번호")
        private String compsCntrNo;

        //요청 클라이언트
        @Schema(type = "String", example = "100", description = "클라이언트")
        private String mandt;

        //요청번호
        @Schema(type = "Integer", example = "123445667", description = "요청번호")
        private Integer reqSeq;

        //거래선
        @Schema(type = "String", example = "121212A02", description = "거래선")
        private String intgPrjTrlineCdCode;

        //처리 여부처리
        @Schema(type = "String", example = "121212A02", description = "처리결과")
        private String procYn;

        //처리내용
        @Schema(type = "String", example = "처리되었습니다.", description = "처리내용")
        private String procResult;

        @Schema(type = "String", example = "SCRM or SRM", description = "DB 타입")
        private String dbType;

        @Hidden
        private String roleType;

        @Hidden
        private String iUserId;
    }
    /**
     * Create
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class returnResultMany extends AbstractRequest {
        private String successCnt;
        private String failCnt;
    }

    /**
     * Update
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateAcceptKey extends AbstractRequest {
        private String userId;
        private String sendAcceptKey;
        private String recvAcceptKey;
    }

    /**
     * User List Filter
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserSearch extends AbstractRequest {
        @Schema(type = "string", example = "", description = "검색 키워드 입니다.")
        private String searchKeyword;
        @Schema(type = "string", example = "", description = "검색 키워드2 입니다.")
        private String searchKeyword2;
        @Schema(type = "string", example = "ALL", description = "사용자 상태 값 입니다.",required = true)
        private String accountStatus;
        @Schema(type = "string", example = "ALL", description = "삭제 유무",required = true)
        private String delYn;
        @Schema(type = "string", example = "SYSTEM", description = "사용자 권한",required = true)
        private String roleType;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDetail {
        private String userId;
        private String userName;
        private String avatar;
        private String password;
        private String postnumber;
        private String address;
        private String detailaddress;
        private String phonenumber;
        private String roleType;
        private String note;
        private String delYn;
        private String creationDt;
        private String creationUser;
        private String lastupdateDt;
        private String lastupdateUser;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserSummary {
        @Schema(example = "test@test.co.kr")
        private String userId;
        @Schema(example = "홍길동")
        private String userName;
        @Schema(example = "null")
        private String avatar;
        @Schema(example = "01000000000")
        private String phonenumber;
        @Schema(example = "n")
        private String delYn;
        @Schema(example = "SYSTEM")
        private String roleType;
        List<Contract.ContractSummary> contract;
        @Schema(example = "0040")
        private String accountStatus;
        //private List<String> intgPrjTrlineCdCode;
        //private List<String> approvalYn;
        private String address;
        @Schema(example = "2023-03-14 13:13:11")
        private String creationDt;
        @Schema(example = "2023-03-16 14:02:31")
        private String lastupdateDt;
        @Schema(example = "999999U2208E01")
        private String compsCntrNo;
        @Schema(example = "빌딩이름")
        private String buildNm;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewRenderingByUser {
        @Schema(example = "22")
        private String vueFileUrl;
        @Schema(example = "[BTN_UPDATE]")
        private List<FunctionRenderingByUser> methods;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FunctionRenderingByUser {
        @Schema(example = "BTN_UPDATE")
        private String methodName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class mailReSendByUser {
        @Schema(example = "aa@bb.com")
        private String userId;
        @Schema(example = "박고객")
        private String userName;
        @Schema(example = "01099998888")
        private String phoneNumber;
        @Schema(example = "SYSTEM")
        private String roleType;
        @Schema(example = "er34f2efsg")
        private String sendAcceptKey;
    }
    //2023-10-13 add
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SetupDefaultCreate extends AbstractRequest {
        @Schema(type = "String", example = "사용자 아이디", description = "사용자 아이디")
        private String userId;
    }

    //서비스 약관 동의 2023-10-13 add
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TermsDefaultCreate {
        @Schema(type = "String", example = "사용자 아이디", description = "사용자 아이디")
        private String userId;

        private String serviceUseAg;
        private String plInfoUsingAg;
        private String plInfoStoreTimeAg;
        private String adRecvAg;
    } 

    //포트폴리오 2023-10-13 add
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortfolioDefaultCreate {
        @Hidden
        private BigDecimal userPortfolioMappingId;

        @Schema(type = "String", example = "test@test.co.kr", description = "사용자 아이디",required = true)
        private String userId;
        @Schema(type = "String", example = "y", description = "디폴트 여부",required = true)
        private String defaultYn;
        @Schema(type = "String", example = "lobby", description = "포트폴리오 명",required = true)
        private String portfolioName;
    }

    //유상계약  2023-10-13 add
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractAPI implements Serializable {
        @Schema(type = "String", example = "119394", description = "통합프로젝트 코드")
        private String intgPrjNo;
        @Schema(type = "String", example = "A03", description = "거래선 코드")
        private String trlineCd;
        @Schema(type = "String", example = "WBSS2", description = "모델 정보")
        private String model;
        @Schema(type = "String", example = "꿈에그림", description = "사이트 이름")
        private String siteNm;
        @Schema(type = "String", example = "서울특별시 중구필2가 84", description = "주소 정보")
        private String addr;
    }

    //알람   2023-10-13 add
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlarmDefaultCreate{
        private String userId;
        // private List<MasterData.AlarmCode> codes;
    }
}
