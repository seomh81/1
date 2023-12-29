package com.hdel.miri.api.domain.contract;

import com.hdel.miri.api.domain.contract.valid.OnAnonymousSearch;
import com.hdel.miri.api.domain.contract.valid.OnContractInsert;
import com.hdel.miri.api.domain.contract.valid.OnMiriCallSearch;
import com.hdel.miri.api.domain.portfolio.valid.OnPortfolioUpdate;
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

public class Contract {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractSearchAnonymous extends AbstractRequest implements Serializable {
        @Schema(type = "String", example = "2033771449", description = "승강기번호 | 사업자번호",required = true)
        @NotNull(message = "검색 데이터를 입력 해주세요.", groups = { OnAnonymousSearch.class })
        @NotEmpty(message = "검색 데이터를 입력 해주세요.", groups = { OnAnonymousSearch.class })
        @NotBlank(message = "검색 데이터를 입력 해주세요.", groups = { OnAnonymousSearch.class })
        private String searchId;

        @Hidden
        private String encId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractSearchEmployee extends AbstractRequest implements Serializable {
        @Schema(type = "String", example = "3028372", description = "사원번호",required = true)
        @NotNull(message = "검색 데이터를 입력 해주세요.", groups = { OnAnonymousSearch.class })
        @NotEmpty(message = "검색 데이터를 입력 해주세요.", groups = { OnAnonymousSearch.class })
        @NotBlank(message = "검색 데이터를 입력 해주세요.", groups = { OnAnonymousSearch.class })
        private String empId;

        @Schema(type = "String", example = "ACCOUNT", description = "롤 구분",required = true)
        @NotNull
        @NotEmpty
        @NotBlank
        private String roleType;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractSearchPortfolio extends AbstractRequest implements Serializable {
        @Schema(type = "String", example = "21", description = "포트폴리오 키 값")
        private String userPortfolioMappingId;
        @Schema(type = "String", example = "132750L10", description = "계약번호 또는 호기번호")
        private String keyword;
        @Schema(type = "String", example = "구로동", description = "현장명")
        private String fieldName;
        @Schema(type = "String", example = "박고객", description = "고객명")
        private String clientName;

        @Hidden
        private List<ContractJoin2> joinList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractSearchPortfolioId extends AbstractRequest implements Serializable {
        @Schema(type = "String", example = "21", description = "포트폴리오 키 값")
        private String userPortfolioMappingId;

        @Hidden
        private List<ContractJoin> joinList;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractSearchPortfolioExtendsMonth extends ContractSearchPortfolio implements Serializable {
        @Schema(type = "String", example = "202301", description = "시작 월 정보")
        private String startMonth;
        @Schema(type = "String", example = "202301", description = "마지막 월 정보")
        private String endMonth;
        @Hidden
        private List<Contract.ContractJoin> list;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractSearchCurrentUser extends AbstractRequest implements Serializable {
        @Schema(type = "String", example = "korea@korea.com", description = "사용자 이메일")    
        private String userId;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractSearchCurrentUserWithCondition extends AbstractRequest implements Serializable {
        @Schema(type = "String", example = "korea@korea.com", description = "사용자 이메일")    
        private String userPortfolioMappingId;
        @Schema(type = "String", example = "korea@korea.com", description = "사용자 이메일")    
        private String userId;
        @Schema(type = "String", example = "테스트빌딩", description = "현장 명")    
        private String fieldName;
        @Schema(type = "String", example = "123456U2209B01", description = "계약 번호")    
        private String keyword;
        @Schema(type = "String", example = "이고객", description = "고객 명")    
        private String clientName;
        @Schema(type = "String", example = "김영업", description = "영업사원 명")    
        private String accountName;
        @Schema(type = "String", example = "1234567", description = "승강기 번호")    
        private String elevatorNo;
        @Schema(type = "String", example = "강남지사", description = "지사/영업소명")    
        private String officeName;
        @Schema(type = "String", example = "ALL", description = "미리 여부")    
        private String miriYn;
        @Schema(type = "String", example = "ALL", description = "미리콜 여부")    
        private String miriCallYn;
        @Schema(type = "String", example = "Y", description = "미리뷰 여부")    
        private String miriViewYn;
        @Schema(type = "String", example = "N", description = "미리로봇 여부")    
        private String miriRobotYn;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractSearchCurrentPortfolio extends AbstractRequest implements Serializable {
        @Schema(type = "BigDecimal", example = "21", description = "사용자 포트폴리오 매핑 번호")
        private BigDecimal userPortfolioMappingId;

    }



    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractDefaultCreate {
        private String userId;
        private BigDecimal userPortfolioMappingId;
        private String IntgPrjTrlineCdCode;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractInsert extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "포트폴리오 키", description = "포트폴리오 키",required = true)
        @NotNull(groups = { OnContractInsert.class }, message = "포트폴리오 키 입력이 필요합니다.")
        private BigDecimal userPortfolioMappingId;
        @Schema(type = "List<ContractAPI>", example = "계약 정보", description = "계약 정보",required = true)
        @Size(min = 1,groups = { OnContractInsert.class }, message = "하나 이상의 계약 정보가 필요합니다.")
        private List<ContractAPI> lobby;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NewContractInsert extends AbstractRequest {
        @Schema(type = "String", example = "gimapei@gmail.com", description = "사용자 이메일",required = true)
        @NotNull
        private String userId;
        @Schema(type = "List<ContractAPI>", example = "계약 정보", description = "계약 정보",required = true)
        private List<ContractAPI> lobby;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractDelete extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "계약 키", description = "포트폴리오 - 계약 키",required = true)
        @NotNull(groups = { OnPortfolioUpdate.class }, message = "포트폴리오 키 입력이 필요합니다.")
        private List<BigDecimal> portfolioContractMappingIds;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractSummaryAPI implements Serializable {
        @Schema(type = "String", example = "통합 프로젝트 코드", description = "거래선 코드")
        private String intgPrjNo;
        @Schema(type = "String", example = "거래선 코드", description = "거래선 코드")
        private String trlineCd;
    }

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


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractMiriServiceAPI extends AbstractRequest {
        @Schema(type = "String", example = "3", description = "사용자 포트폴리오 매핑 아이디",required = true)
        @NotNull
        @NotBlank
        private String userPortfolioMappingId;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractMiriServiceAPI2 extends AbstractRequest {
        @Schema(type = "String", example = "2221141", description = "승강기 번호",required = true)
        @NotNull
        @NotBlank
        private String elevatorNo;
    }
    

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractEmployeeAPI implements Serializable {
        @Schema(type = "String", example = "N18796", description = "거래선 코드")
        private String intgPrjNo;
        @Schema(type = "String", example = "B01", description = "거래선 코드")
        private String trlineCd;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractCopyListVO extends AbstractRequest{
        @Schema(type = "String", example = "from@from.com", description = "복사 from 유저 id")
        private String fromUserId;
        @Schema(type = "List<String>", example = "[to1@to.com, to2@to.com]", description = "복사 to 유저 list")
        private List<String> toUserList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractCopyVO{
        private String fromUserId;
        private String toUserId;
        private String currentUserId;

        @Hidden
        private String copyLogNo;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractJoin {
        private String portfolioContractMappingId;
        private String intgPrjTrlineCdCode;
        private String intgPrjNo;
        private String trlineCd;

        @Hidden
        private String keyword;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractJoin2 {
        private String portfolioContractMappingId;
        private String intgPrjTrlineCdCode;
        private String intgPrjNo;
        private String trlineCd;
        private String prjNo;
        private String hoNo;
        private String elevatorNo;
        private String installationPlace;
        private String buildNm;
        private String officeNm;
        private String salesEmplNm;

        @Hidden
        private String keyword;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractJoinDistinct {
        private String intgPrjTrlineCdCode;
        private String intgPrjNo;
        private String trlineCd;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractJoinDetail {
        private String portfolioContractMappingId;
        private String contractCode;
        private String IntgPrjTrlineCdCode;
        private String intgPrjNo;
        private String trlineCd;
        private String prjNo ;
        private List<ContractCarInfo> carList;
        private String model;
        private String bgnDt;
        private String xpirDt;
        private String custNm;
        private String siteNm;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractDetail {
        private String userPortfolioMappingId;
        private List<ContractInfo> contractList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractInfo {
        private String portfolioContractMappingId;
        private String contractCode;
        private String intgPrjTrlineCdCode;
        private String intgPrjNo;
        private String trlineCd;
        private String bgnDt;
        private String xpirDt;
        private String custNm;
        private String siteNm;
        private String buildNm;
        private String officeNm;
        private String salesEmplNm;
        private String elevatorNo;
        private String miriYn;
        private String miriCallYn;
        private String miriViewYn;
        private String miriRobotYn;
        private List<ProjectInfo> projectList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProjectInfo {
        private String portfolioContractMappingId;
        private String contractCode;
        private String intgPrjTrlineCdCode;
        private String intgPrjNo;
        private String trlineCd;
        private String prjNo;
        private String custNm;
        private String siteNm;
        private String buildNm;
        private List<CarInfo> carList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CarInfo {
        private String portfolioContractMappingId;
        private String contractCode;
        private String intgPrjTrlineCdCode;
        private String intgPrjNo;
        private String trlineCd;
        private String prjNo;
        private String hoNo;
        private String carType;
        private String installationPlace;
        private String officeNm;
        private String salesEmplNm;
        private String elevatorNo;
        private String miriYn;
        private String miriCallYn;
        private String miriViewYn;
        private String miriRobotYn;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractJoinDetailPRJNOInfo {
        private String contractCode;
        private String intgPrjTrlineCdCode;
        private String intgPrjNo;
        private String trlineCd;
        private String prjNo ;
        private List<ContractCarPRJNOInfo> carList;
        private String model;
        private String bgnDt;
        private String xpirDt;
        private String custNm;
        private String siteNm;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractJoinSummary {
        private String portfolioContractMappingId;
        private String contractCode;
        private String intgPrjTrlineCdCode;
        private String intgPrjNo;
        private String trlineCd;
        private String bgnDt;
        private String xpirDt;
        private String custNm;
        private String siteNm;
        private String model;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractCarInfo {
        private String portfolioContractMappingId;
        private String carNo;
        private String model;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractCarPRJNOInfo {
        private String intgPrjTrlineCdCode;
        private String carNo;
        private String model;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractContactInfo {
        private String nm;
        private String deptNm;
        private String dispStr;
        private String mob;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractSummary {
        @Schema(example = "N17547B01")
        private String intgPrjTrlineCdCode;
        @Schema(example = "n")
        private String approvalYn;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractMIRIStatus {
        private String isAvailable;
    }
}
