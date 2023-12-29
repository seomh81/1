package com.hdel.miri.api.domain.hrts;

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

public class HRTS {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HRTSDistanceAvgSearch extends AbstractRequest{
        @Schema(type = "BigDecimal", example = "21", description = "포트폴리오 키 값")
        private BigDecimal userPortfolioMappingId;
        @Hidden
        private List<HRTSDistance> targets;
        @Hidden
        private String start;
        @Hidden
        private String end;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HRTSDistance {
        private String prjNo;
        private String carNo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HRTSRunAvgSearch extends AbstractRequest {
        @Schema(type = "String", example = "123456", description = "원 프로젝트 번호")
        private String prjNo;
        @Schema(type = "String", example = "B01", description = "호기 번호")
        private String carNo;
        @Schema(type = "String", example = "20220103", description = "20220101")
        private String requestDate;

        @Hidden
        private String firstYear;
        @Hidden
        private String firstMonth;
        @Hidden
        private String firstStart;
        @Hidden
        private String firstEnd;
        @Hidden
        private String secondYear;
        @Hidden
        private String secondMonth;
        @Hidden
        private String secondStart;
        @Hidden
        private String secondEnd;
        @Hidden
        private String thirdYear;
        @Hidden
        private String thirdMonth;
        @Hidden
        private String thirdStart;
        @Hidden
        private String thirdEnd;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HRTSMasterSearch extends AbstractRequest {
        @Schema(type = "String", example = "123456", description = "통합 프로젝트 코드")
        private String intgPrjNo;
        @Schema(type = "String", example = "B01", description = "거래선 코드")
        private String trlineCd;
        @Schema(type = "String", example = "123456", description = "원 프로젝트 번호")
        private String prjNo;
        @Schema(type = "String", example = "L01", description = "호기 번호")
        private String carNo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HRTSMasterSearchWeb extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "21", description = "사용자 포트폴리오 매핑 번호")
        private BigDecimal userPortfolioMappingId;
        @Schema(type = "String", example = "126453L01", description = "호기 번호")
        private String keyword;
        @Schema(type = "String", example = "20230101", description = "검색 시작일")
        private String startDate;
        @Schema(type = "String", example = "20230131", description = "검색 종료일")
        private String endDate;

        @Hidden
        private List<CC.IntegratedInspectVO> joinList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HRTSMasterJoin {
        @Schema(type = "String", example = "123456", description = "통합 프로젝트 코드")
        private String intgPrjNo;
        @Schema(type = "String", example = "B01", description = "거래선 코드")
        private String trlineCd;
        @Schema(type = "String", example = "123456", description = "원 프로젝트 번호")
        private String prjNo;
        @Schema(type = "String", example = "L01", description = "호기 번호")
        private String carNo;
        @Schema(type = "String", example = "2023년 - 2월", description = "진단월")
        private String diagMonth;
        @Schema(type = "String", example = "가재마을2단지", description = "현장명")
        private String siteNm;
        @Schema(type = "String", example = "209-1", description = "동호기명")
        private String dongcarNm;
        @Schema(type = "String", example = "126453L17", description = "승강기번호")
        private String elProjNo;
        @Schema(type = "String", example = "세종특별자치시달빛1로 39", description = "주소")
        private String address;
        @Schema(type = "String", example = "20140424", description = "설치일자")
        private String setupDate;
        @Schema(type = "String", example = "20230430", description = "계약만료일")
        private String expirationDate;
        @Schema(type = "String", example = "20230430", description = "점검일")
        private String chkDate;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HRTSMasterJoinWeb {
        @Schema(type = "String", example = "가재마을2단지", description = "현장명")
        private String siteNm;
        @Schema(type = "String", example = "209-1", description = "동호기명")
        private String dongcarNm;
        @Schema(type = "String", example = "126453L17", description = "승강기번호")
        private String elProjNo;
        @Schema(type = "String", example = "세종특별자치시달빛1로 39", description = "주소")
        private String address;
        @Schema(type = "String", example = "모델", description = "모델")
        private String model;
        @Schema(type = "String", example = "서비스 유형", description = "서비스 유형")
        private String service_type;
        @Schema(type = "String", example = "20140424", description = "설치일자")
        private String setupDate;
        @Schema(type = "String", example = "20230430", description = "계약만료일")
        private String expirationDate;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResultRunAvgVO {
        private List<RunAvgTargetVO> target;
        private List<RunAvgTotVO> type;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RunAvgTotVO {
        @Schema(example = "2021")
        private String yyyy;
        @Schema(example = "12")
        private String mm;
        @Schema(example = "5.04")
        private String totAvgRunTime;
        @Schema(example = "22.58")
        private String totAvgRunDistance;
        @Schema(example = "766.65")
        private String totAvgRunNum;
        @Schema(example = "766.65")
        private String totAvgDocc;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RunAvgTargetVO {
        @Schema(example = "2021")
        private String yyyy;
        @Schema(example = "12")
        private String mm;
        @Schema(example = "5.04")
        private String elAvgRunTime;
        @Schema(example = "22.58")
        private String elAvgRunDistance;
        @Schema(example = "766.65")
        private String elAvgRunNum;
        @Schema(example = "766.65")
        private String elAvgDocc;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CurrentMonthRunTimeTargetVO {
        @Schema(example = "2021")
        private String yyyy;
        @Schema(example = "12")
        private String mm;
        @Schema(example = "5.04")
        private String elAvgRunTime;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResultDOCCAvgVO {
        private List<DOCCTypeVO> type;
        private List<DOCCTargetVO> target;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DOCCTypeVO {
        @Schema(example = "2022")
        private String yyyy;
        @Schema(example = "01")
        private String mm;
        @Schema(example = "766.06")
        private String totAvgDocc;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DOCCTargetVO {
        @Schema(example = "2022")
        private String yyyy;
        @Schema(example = "01")
        private String mm;
        @Schema(example = "766.06")
        private String elAvgDocc;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HRTSPerformanceCheckResultVO {
        @Schema(example = "0.0")
        private String fTotalExe;

        @Schema(example = "0.0")
        private String fLevelNr;

        @Schema(example = "0.0")
        private String fOpenCv;

        @Schema(example = "0.0")
        private String fCloseCv;

        @Schema(example = "점검필요")
        private String fRst;

        @Schema(example = "0.0")
        private String sTotalExe;

        @Schema(example = "0.0")
        private String sLevelNr;

        @Schema(example = "0.0")
        private String sOpenCv;

        @Schema(example = "0.0")
        private String sCloseCv;

        @Schema(example = "점검필요")
        private String sRst;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HRTSConnectionVO extends AbstractRequest {
        private String projNo;
        private String hoNo;
        private String command;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HRTSMonitVO extends AbstractRequest {
        @Schema(example = "123456")
        private String projNo;
        @Schema(example = "L01")
        private String hoNo;
        @Schema(example = "PT")
        private String elType;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HRTSReportReqVO extends AbstractRequest {
        @Schema(example = "202211")
        @NotNull
        @NotBlank
        private String yyyymm;
        
        @Schema(example = "123456")
        @NotNull
        @NotBlank
        private String projNo;

        @Schema(example = "L01")
        @NotNull
        @NotBlank
        private String hoNo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HRTSConnectionResultVO {
        private int result;
        @Schema(example = "141346")
        private String projNo;
        @Schema(example = "L01")
        private String hoNo;
        @Schema(example = "PT")
        private String elType;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HRTSMonitResultVO {
        private int result;
        private String projNo;
        private String hoNo;
        private String timestamp;
        private String currentFloor;
        private String currentdispfloor;
        private int runStatus;
        private int moveDirection;
        private int runMode;
        private int faultStatus;
        private int doorStatus;
        private int fdoorStatus;
        private int rdoorStatus;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HRTSRuleVO {
        private int result;
        private String projNo;
        private String hoNo;
        private String command;
        private String value;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HRTSReportVO {
        private String s3Key;
        private String fileNm;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HRTSRemoteInspectVO{
        @Schema(example = "2221141")
        private String elevatorNo;
        @Schema(example = "엘리베이터")
        private String elvtrDivNm;
        @Schema(example = "시흥센트럴돔그랑트리캐슬")
        private String buldNm;
        @Schema(example = "123456L01")
        private String projNo;
        @Schema(example = "정기점검")
        private String reportType;
        @Schema(example = "20230101")
        private String chkDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HRTSAvgRunDistanceResultVO {
        private double elAvgRunDistance;
        @Schema(example = "23-09-30")
        private String startDate;
        @Schema(example = "23-10-30")
        private String endDate;
    }
}
