package com.hdel.miri.api.domain.user;

import com.hdel.miri.api.domain.contract.Contract;
import com.hdel.miri.api.domain.user.vo.valid.*;
import com.hdel.miri.api.util.request.AbstractRequest;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;
import java.util.List;


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
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MultipleUserAlreadyExt extends AbstractRequest{
        @Schema(type = "List<UserAlreadyExt>", example = "[{\"userId\":\"test@aa.bb\"]", description = "사용자 아이디 리스트")
        @NotNull(groups = { OnFindAccount.class }, message = "사용자 아이디  입력이 필요합니다.")
        @Size(min=1,groups = { OnFindAccount.class }, message = "사용자 아이디  입력이 필요합니다.")
        private List<UserAlreadyExt> list;
    }


    /**
     * ChangePassword Request.
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangePassword extends AbstractRequest {
        @Schema(type = "string", example = "test@aa.bb", description = "사용자 아이디")
        @NotEmpty(groups = { OnChangePassword.class }, message = "사용자 아이디 입력이 필요합니다.")
        @NotNull(groups = { OnChangePassword.class }, message = "사용자 아이디  입력이 필요합니다.")
        @NotBlank(groups = {OnChangePassword.class}, message = "사용자 아이디 입력이 필요합니다.")
        @Email(groups = {OnChangePassword.class}, message = "아이디가 이메일 형식이 아닙니다.")
        private String userId;

        @Schema(type = "string", example = "1234", description = "새로운 변경할 패스워드")
        @NotEmpty(groups = { OnChangePassword.class }, message = "새로 변경할 패스워드가 필요합니다.")
        @NotNull(groups = { OnChangePassword.class }, message = "새로 변경할 패스워드가 필요합니다.")
        @NotBlank(groups = {OnChangePassword.class}, message = "새로 변경할 패스워드가 필요합니다.")
        private String newPassword;
    }

    /**
     * ResetPassword Request.
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RestPassword extends AbstractRequest {
        @Schema(type = "string", example = "test@aa.bb", description = "사용자 아이디")
        @NotEmpty(groups = { OnResetPassword.class }, message = "사용자 아이디 입력이 필요합니다.")
        @NotNull(groups = { OnResetPassword.class }, message = "사용자 아이디  입력이 필요합니다.")
        @NotBlank(groups = {OnResetPassword.class}, message = "사용자 아이디 입력이 필요합니다.")
        @Email(groups = {OnResetPassword.class}, message = "아이디가 이메일 형식이 아닙니다.")
        private String userId;
    }

    /**
     * Find Account Request.
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FindAccount extends AbstractRequest {
        @Schema(type = "string", example = "01000000000", description = "사용자 전화번호 입니다.")
        @NotEmpty(groups = { OnFindAccount.class }, message = "사용자 전화번호 입력이 필요합니다.")
        @NotNull(groups = { OnFindAccount.class }, message = "사용자 전화번호  입력이 필요합니다.")
        @NotBlank(groups = {OnFindAccount.class}, message = "사용자 전화번호 입력이 필요합니다.")
        private String phonenumber;

        @Schema(type = "string", example = "홍길동", description = "사용자명 입니다.")
        @NotEmpty(groups = { OnFindAccount.class }, message = "사용자명 입력이 필요합니다.")
        @NotNull(groups = { OnFindAccount.class }, message = "사용자명  입력이 필요합니다.")
        @NotBlank(groups = {OnFindAccount.class}, message = "사용자명 입력이 필요합니다.")
        private String userName;
    }

    /**
     * Find Password Request.
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FindPassword extends AbstractRequest {
        @Schema(type = "string", example = "test@aa.bb", description = "사용자 아이디")
        @NotEmpty(groups = { OnFindPassword.class }, message = "사용자 아이디 입력이 필요합니다.")
        @NotNull(groups = { OnFindPassword.class }, message = "사용자 아이디  입력이 필요합니다.")
        @NotBlank(groups = {OnFindPassword.class}, message = "사용자 아이디 입력이 필요합니다.")
        @Email(groups = { OnFindPassword.class }, message = "아이디가 이메일 형식이 아닙니다.")
        private String userId;

        @Schema(type = "string", example = "01000000000", description = "사용자 전화번호 입니다.")
        @NotEmpty(groups = { OnFindPassword.class }, message = "사용자 전화번호 입력이 필요합니다.")
        @NotNull(groups = { OnFindPassword.class }, message = "사용자 전화번호  입력이 필요합니다.")
        @NotBlank(groups = {OnFindPassword.class}, message = "사용자 전화번호 입력이 필요합니다.")
        private String phonenumber;

        @Hidden
        private String newPassword;
    }
    /**
    * Create
    * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DefaultUserCreate extends AbstractRequest {
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
        @NotEmpty(groups = { OnUserCreate.class }, message = "서비스 이용 동의 입력이 필요합니다.")
        @NotNull(groups = { OnUserCreate.class }, message = "서비스 이용 동의  입력이 필요합니다.")
        @NotBlank(groups = {OnUserCreate.class}, message = "서비스 이용 동의 입력이 필요합니다.")
        @Pattern(regexp = "^[yn]$" ,message = "유효한 데이터가 아닙니다.", groups = {OnUserCreate.class})
        private String termsServiceUseAg;
        @Schema(type = "String", example = "y | n", description = "정보 이용 동의")
        @NotEmpty(groups = { OnUserCreate.class }, message = "정보 이용 동의 입력이 필요합니다.")
        @NotNull(groups = { OnUserCreate.class }, message = "정보 이용 동의  입력이 필요합니다.")
        @NotBlank(groups = {OnUserCreate.class}, message = "정보 이용 동의 입력이 필요합니다.")
        @Pattern(regexp = "^[yn]$" ,message = "유효한 데이터가 아닙니다.", groups = {OnUserCreate.class})
        private String termsPlInfoUsingAg;
        @Schema(type = "String", example = "y | n", description = "개인정보 유효 5년 동의")
        @NotEmpty(groups = { OnUserCreate.class }, message = "개인정보 유효 5년 동의 입력이 필요합니다.")
        @NotNull(groups = { OnUserCreate.class }, message = "개인정보 유효 5년 동의  입력이 필요합니다.")
        @NotBlank(groups = {OnUserCreate.class}, message = "개인정보 유효 5년 동의 입력이 필요합니다.")
        @Pattern(regexp = "^[yn]$" ,message = "유효한 데이터가 아닙니다.", groups = {OnUserCreate.class})
        private String termsPlInfoStoreTimeAg;
        @Schema(type = "String", example = "y | n", description = "광고 수신 동의")
        @NotEmpty(groups = { OnUserCreate.class }, message = "광고 수신 동의 입력이 필요합니다.")
        @NotNull(groups = { OnUserCreate.class }, message = "광고 수신 동의  입력이 필요합니다.")
        @NotBlank(groups = {OnUserCreate.class}, message = "광고 수신 동의 입력이 필요합니다.")
        @Pattern(regexp = "^[yn]$" ,message = "유효한 데이터가 아닙니다.", groups = {OnUserCreate.class})
        private String termsAdRecvAg;

        @Schema(type = "List<ContractSummary>", example = "[{\n" +
                "        \"intgPrjNo\":\"a123455\"\n" +
                "        , \"trlineCd\":\"A01\"\n" +
                "        , \"model\":\"WBSS\"\n" +
                "        , \"siteName\" : \"파랑주간보호센터\"\n" +
                "        , \"addr\":\"전라북도 전주시..\"\n" +
                "    }]", description = "Default Portfolio.")
        @NotNull(groups = { OnUserCreate.class }, message = "계약 정보가 필요합니다.")
        @Size(min = 1, message = "계약 정보가 없습니다.",groups = {OnUserCreate.class})
        private List<Contract.ContractAPI> lobby;

        @Schema(type = "String", example = "xdjfoiwejfoweifw", description = "구글 파이어베이스 사용자 아이디")
        private String firebaseUserId;

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
    public static class DefaultOtherUserCreate extends AbstractRequest {
        @Schema(type = "String", example = "korea@korea.com", description = "사용자 아이디",required = true)
        @NotEmpty(groups = { OnUserCreate.class }, message = "사용자 아이디 입력이 필요합니다.")
        @NotNull(groups = { OnUserCreate.class }, message = "사용자 아이디  입력이 필요합니다.")
        @NotBlank(groups = {OnUserCreate.class}, message = "사용자 아이디 입력이 필요합니다.")
        @Email(groups = {OnUserCreate.class}, message = "아이디가 이메일 형식이 아닙니다.")
        private String userId;

        @Schema(type = "String", example = "홍길동", description = "사용자 이름")
        private String userName;

        @Hidden
        private String originPassword;
        @Hidden
        private String password;

        @Schema(type = "String", example = "010-0000-0000", description = "사용자 전화정보")
        @NotBlank(message = "사용자 전화 정보가 필요합니다.", groups = {OnUserCreate.class})
        private String phonenumber;
        
        private String termsServiceUseAg = "y";
        private String termsPlInfoUsingAg = "y";
        private String termsPlInfoStoreTimeAg = "y";
        private String termsAdRecvAg = "n";
        private List<Contract.ContractAPI> lobby;

        @Hidden
        private String roleType;

        @Hidden
        private String iUserId;

        @Hidden
        private String empId;

        @Hidden
        private String note;
    }
    /**
     * Create
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DefaultOtherUserCreateAuto extends AbstractRequest {
        @Schema(type = "String", example = "korea@korea.com", description = "사용자 아이디")
        private String userId;

        @Schema(type = "String", example = "홍길동", description = "사용자 이름")
        private String userName;

        @Hidden
        private String originPassword;
        @Hidden
        private String password;

        @Schema(type = "String", example = "010-0000-0000", description = "사용자 전화정보")
        private String phonenumber;
        
        private String termsServiceUseAg = "y";
        private String termsPlInfoUsingAg = "y";
        private String termsPlInfoStoreTimeAg = "y";
        private String termsAdRecvAg = "n";
        private List<Contract.ContractAPI> lobby;

        @Hidden
        private String roleType;

        @Hidden
        private String iUserId;

        @Hidden
        private String empId;

        @Hidden
        private String note;
    }

    /**
     * Create
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DefaultOtherUserCreateList extends AbstractRequest {
        private List<DefaultOtherUserCreate> createUserList;
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
     * Create
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class returnResultAuto extends AbstractRequest {
        private String userId;
        private String resultValue;
    }

    /**
     * Update
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserUpdate extends AbstractRequest {
        @Schema(type = "String", example = "홍길동", description = "사용자 이름")
        private String userName;

        @Schema(type = "String", example = "123-123", description = "우편 번호")
        private String postnumber;

        @Schema(type = "String", example = "대한민국 어딘가", description = "사용자 주소정보")
        private String address;

        @Schema(type = "String", example = "대한민국 어딘가", description = "사용자 주소정보")
        private String detailaddress;

        @Schema(type = "String", example = "010-0000-0000", description = "사용자 전화정보")
        private String phonenumber;

        @Schema(type = "String", example = "y | n", description = "개인정보 유효 5년 동의",required = true)
        @Pattern(regexp = "^[yn]$" ,message = "유효한 데이터가 아닙니다.", groups = {OnUserUpdate.class})
        private String plInfoStoreTimeAg;

        @Schema(type = "String", example = "y | n", description = "광고 수신 동의",required = true)
        @Pattern(regexp = "^[yn]$" ,message = "유효한 데이터가 아닙니다.", groups = {OnUserUpdate.class})
        private String adRecvAg;

        @Schema(type = "String", example = "password", description = "변경 비밀번호")
        private String newPassword;
    }

    /**
     * Update
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserUpdateAdminPage extends AbstractRequest {
        @Schema(type = "String", example = "test@aa.bb", description = "사용자 아이디",required = true)
        @NotEmpty(groups = { OnUserUpdate.class }, message = "사용자 아이디 입력이 필요합니다.")
        @NotNull(groups = { OnUserUpdate.class }, message = "사용자 아이디  입력이 필요합니다.")
        @NotBlank(groups = {OnUserUpdate.class}, message = "사용자 아이디 입력이 필요합니다.")
        @Email(groups = {OnUserUpdate.class}, message = "아이디가 이메일 형식이 아닙니다.")
        private String userId;

        @Schema(type = "String", example = "홍길동", description = "사용자 이름",required = true)
        @NotEmpty(groups = { OnUserUpdate.class }, message = "사용자이름 입력이 필요합니다.")
        @NotNull(groups = { OnUserUpdate.class }, message = "사용자이름  입력이 필요합니다.")
        @NotBlank(groups = { OnUserUpdate.class }, message = "사용자이름 입력이 필요합니다.")
        private String userName;

        @Schema(type = "String", example = "010-0000-0000", description = "사용자 전화정보",required = true)
        @NotEmpty(groups = { OnUserUpdate.class }, message = "휴대폰 번호 입력이 누락 되었습니다.")
        @NotNull(groups = { OnUserUpdate.class }, message = "휴대폰 번호 입력이 누락 되었습니다.")
        @NotBlank(groups = { OnUserUpdate.class }, message = "휴대폰 번호 입력이 누락 되었습니다.")
        private String phonenumber;

    }
    /**
     * Update
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserAccountStatus extends AbstractRequest {
        @Schema(type = "String", example = "test@aa.bb", description = "사용자 아이디",required = true)
        @NotEmpty(groups = { OnUserUpdate.class }, message = "사용자 아이디 값이 필요합니다.")
        @NotNull(groups = { OnUserUpdate.class }, message = "사용자 아이디 값이 필요합니다.")
        @NotBlank(groups = {OnUserUpdate.class}, message = "사용자 아이디 값이 필요합니다.")
        private String userId;

        @Schema(type = "String", example = "0001,0010,0020,0040,9999", description = "사용자 상태 변경",required = true)
        @NotEmpty(groups = { OnUserUpdate.class }, message = "사용자 상태 값이 필요합니다.")
        @NotNull(groups = { OnUserUpdate.class }, message = "사용자 상태 값이 필요합니다.")
        @NotBlank(groups = {OnUserUpdate.class}, message = "사용자 상태 값이 필요합니다.")
        private String accountStatus;
    }
    /**
     * Update
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserAccountStatusRequest extends AbstractRequest {
        @Schema(type = "List<UserAccountStatus>", example = "[{ \"userId\":\"test@aa.bb\",\"accountStatus\":\"0010\"}]", description = "사용자 상태 변경",required = true)
        @NotNull(groups = { OnUserUpdate.class }, message = "사용자 상태 값이 필요합니다.")
        @Size(min=1,groups = { OnUserUpdate.class }, message = "하나 이상의 상태 값이 필요합니다.")
        private List<UserAccountStatus> targetList;
    }
    /**
     * Remove
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRemove extends AbstractRequest {
        @Schema(type = "string", example = "['test@aa.bb']", description = "사용자 아이디",required = true)
        @NotNull(groups = { OnUserRemove.class }, message = "사용자 아이디 입력이 필요합니다.")
        @Size(min = 1,message = "하나 이상의 사용자 아이디 입력이 필요합니다.")
        private List<String> userIds;

    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRecovery extends AbstractRequest {
        @Schema(type = "string", example = "['test@aa.bb']", description = "사용자 아이디",required = true)
        @NotNull(groups = { OnUserRecovery.class }, message = "사용자 아이디 입력이 필요합니다.")
        @Size(min = 1,message = "하나 이상의 사용자 아이디 입력이 필요합니다.")
        private List<String> userIds;

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
        @Schema(type = "string", example = "ALL", description = "검색 대상 사용자 권한.",required = true)
        private String targetRoleType;
        @Schema(type = "string", example = "ALL", description = "사용자 상태 값 입니다.",required = true)
        private String accountStatus;
        @Schema(type = "string", example = "ALL", description = "삭제 유무",required = true)
        private String delYn;
        @Schema(type = "string", example = "SYSTEM", description = "사용자 권한",required = true)
        private String roleType;
        @Schema(type = "string", example = "Y/N", description = "계약 여부",required = true)
        private String contractYn;
        @Schema(type = "string", example = "from@user.com", description = "복사 필터용 from ID")
        private String selectedUserId;
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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class userAcceptMailInfo {
        @Schema(example = "Y/N")
        private String creationFg;
        @Schema(example = "박고객")
        private String userName;
        @Schema(example = "123456U2305B01")
        private String compsCntrNo;
        @Schema(example = "salesUser@hdel.co.kr")
        private String salesUserId;
        @Schema(example = "김영업")
        private String salesUserName;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class nonContractData {
        @Schema(example = "a@a.com")
        private String userId;
        @Schema(example = "박고객")
        private String userName;
        @Schema(example = "010-1234-5678")
        private String phonenumber;
        @Schema(example = "건물관리인")
        private String roleType;
        @Schema(example = "정상")
        private String status;
        @Schema(example = "삭제")
        private String delYn;
        @Schema(example = "123456B01")
        private String intgPrjTrlineCdCode;
    }

    /**
     * Create
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class setNonContractUserList extends AbstractRequest {
        private List<setNonContractUser> setUserList;
    }

    /**
     * Create
     * */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class setNonContractUser extends AbstractRequest {
        @Schema(type = "String", example = "mapping@a.com", description = "맵핑할 id")
        private String userId;
        private List<Contract.ContractAPI> lobby;
    }
}
