package com.hdel.miri.api.domain.miri;

import com.hdel.miri.api.domain.cc.CC;
import com.hdel.miri.api.util.request.AbstractRequest;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MIRI {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MIRIServiceSearch extends AbstractRequest{
        @Schema(type = "String", example = "202301", description = "조회 시작일",required = true)
        @NotBlank
        @NotNull
        private String startDate;

        @Schema(type = "String", example = "202304", description = "조회 종료일",required = true)
        @NotBlank
        @NotNull
        private String endDate;

        private String miriType;

        @Schema(type = "String", example = "3", description = "사용자 포트폴리오 매핑 아이디",required = true)
        @NotNull
        @NotBlank
        private String userPortfolioMappingId;
    }


    // call = requestCount, status
    // robot = callCount
    // view = not found

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MIRIServiceResultVO {
        private String callCount;
        private String carNo;
        private String status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MIRIPortalLoginSearch extends AbstractRequest{
        @Schema(type = "String", example = "20230101", description = "조회 시작일",required = true)
        @NotBlank
        @NotNull
        private String startDate;
        @Schema(type = "String", example = "20230431", description = "조회 종료일",required = true)
        @NotBlank
        @NotNull
        private String endDate;
        @Schema(type = "String", example = "ALL", description = "계정 상태")
        private String accountStatus;
        @Schema(type = "String", example = "account", description = "권한")
        private String roleType;
        @Schema(type = "String", example = "y", description = "삭제 여부")
        private String delYn;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MIRIPortalLoginData extends AbstractRequest{
        @Schema(type = "String", example = "aaa@aa.com", description = "유저 ID")
        private String userId;
        @Schema(type = "String", example = "김유저", description = "유저 명")
        private String userName;
        @Schema(type = "String", example = "입주민", description = "유저 권한")
        private String roleType;
        @Schema(type = "String", example = "010-1111-2222", description = "연락처")
        private String phonenumber;
        @Schema(type = "String", example = "가입요청", description = "상태")
        private String accountStatus;
        @Schema(type = "String", example = "n", description = "삭제 여부")
        private String delYn;
        @Schema(type = "String", example = "2023-09-12 00:00:00", description = "계정 생성 일자")
        private String creationDt;
        @Schema(type = "String", example = "2023-09-12 00:00:00", description = "최종 앱 로그인 시간")
        private String appLoginTime;
        @Schema(type = "String", example = "1", description = "앱 로그인 시도 횟수")
        private String appLoginCnt;
        @Schema(type = "String", example = "2023-09-12 00:00:00", description = "최종 PC 로그인 시간")
        private String pcLoginTime;
        @Schema(type = "String", example = "1", description = "PC 로그인 시도 횟수")
        private String pcLoginCnt;
    }
}
