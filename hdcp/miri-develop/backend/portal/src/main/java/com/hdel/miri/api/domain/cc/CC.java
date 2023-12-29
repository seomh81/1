package com.hdel.miri.api.domain.cc;

import com.hdel.miri.api.domain.cc.xml.InspectFail;
import com.hdel.miri.api.util.request.AbstractRequest;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class CC {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CCCurrentRequest extends AbstractRequest {
        @Hidden
        private String userId;
        private String userPortfolioMappingId;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CCCurrentElevatorChangeDateRequest extends AbstractRequest {
        @Schema(example = "126453")
        private String prjNo;
        @Schema(example = "L09")
        private String carNo;
        @Schema(example = "2023-07-24")
        private String changeDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CCCurrentPortfolioRequest extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "21", description = "사용자 포트폴리오 매핑 번호")
        private BigDecimal userPortfolioMappingId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CCCurrentPortfolioRequestExtSearch extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "21", description = "사용자 포트폴리오 매핑 번호")
        private BigDecimal userPortfolioMappingId;
        @Schema(type = "String", example = "126453L01", description = "호기 번호")
        private String keyword;
        @Schema(type = "String", example = "20230101", description = "검색 시작일")
        private String startDate;
        @Schema(type = "String", example = "20230131", description = "검색 종료일")
        private String endDate;
        @Schema(type = "String", example = "all", description = "정기검사/정기점검")
        private String inspType;

        @Hidden
        private List<String> projnos;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CCCurrentElevatorSearchByPortfolio extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "21", description = "사용자 포트폴리오 매핑 번호")
        private BigDecimal userPortfolioMappingId;
        @Schema(type = "String", example = "엘리베이터", description = "장비 종류")
        private String type;
        @Schema(type = "String", example = "원프로젝트", description = "123456")
        private String prjNo;
        @Schema(type = "String", example = "호기번호", description = "L01")
        private String carNo;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CCCurrentElevatorRequest extends AbstractRequest {
        @Schema(type = "String", example = "123456", description = "원 프로젝트 번호")
        private String prjNo;
        @Schema(type = "String", example = "L01", description = "호기 번호")
        private String carNo;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CCCurrentBillHistoryRequest extends AbstractRequest {
        // @Schema(type = "String", example = "182609U2212B01", description = "유상계약번호")
        // private String compsContNo;

        @Schema(type = "String", example = "141199", description = "통합프로젝트 코드")
        @NotNull
        @NotBlank
        private String intgPrjNo;

        @Schema(type = "String", example = "B02", description = "거래선 코드")
        @NotNull
        @NotBlank
        private String trlineCd;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CCCurrentCalenderRequest extends AbstractRequest {
        @Hidden
        private String userId;
        @Schema(type = "String", example = "20230201", description = "캘린더 조회 타입정보로 조회 시작일자 입니다.",required = true)
        @NotNull
        @NotBlank
        private String startDate;
        @Schema(type = "String", example = "20230223", description = "캘린더 조회 타입정보로 조회 마지막일자 입니다.",required = true)
        @NotNull
        @NotBlank
        private String endDate;

        @Schema(type = "String", example = "21", description = "사용자 포트폴리오 매핑 번호", required = true)
        private String userPortfolioMappingId;

        @Schema(type = "List<String>", example = "[\"21\"]", description = "통합프로젝트번호||거래선코드")
        @Hidden
        private List<String> intgPrjNoAndTrlineCds;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CCServiceRequest extends AbstractRequest {
        @Hidden
        private String userId;

        @Schema(type = "String", example = "20230201", description = "서비스 등록일 기준 조회 시작일자 입니다.",required = true)
        @NotNull
        @NotBlank
        private String startDate;

        @Schema(type = "String", example = "20230223", description = "서비스 등록일 기준 조회 마지막일자 입니다.",required = true)
        @NotNull
        @NotBlank
        private String endDate;

        @Schema(type = "String", example = "21", description = "사용자 포트폴리오 매핑 번호", required = true)
        @NotNull
        @NotBlank
        private String userPortfolioMappingId;

        @Schema(type = "String", example = "0010", description = "SERVICE_TYPE FROM MASTERDATA")
        @NotNull
        @NotBlank
        private String serviceType;
        
        @Schema(type = "String", example = "신고1", description = "신고 내역")
        private String searchKeyword;

        @Schema(type = "List<String>", example = "[\"21\"]", description = "통합프로젝트번호||거래선코드")
        @Hidden
        private List<String> intgPrjNoAndTrlineCds;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CCServiceREQ extends AbstractRequest {
        @Schema(type = "String", example = "123456", description = "프로젝터번호", required = true)
        @NotNull
        @NotBlank
        private String prjNo;

        @Schema(type = "String", example = "L01", description = "호기번호", required = true)
        @NotNull
        @NotBlank
        private String hoNo;

        @Schema(type = "String", example = "0010", description = "SERVICE_TYPE FROM MASTERDATA", required = true)
        @NotNull
        @NotBlank
        private String serviceType;

        @Schema(type = "String", example = "세금계산서 재발행", description = "SERVICE_TYPE 한글명칭", required = true)
        @NotNull
        @NotBlank
        private String serviceTypeNm;

        @Schema(type = "String", example = "10102", description = "운행중 정지")
        private String serviceCd;

        @Schema(type = "String", example = "2층 엘리베이터 고장", description = "신고 내역", required = true)
        private String contents;

        @Schema(type = "String", example = "박고객", description = "고객 명", required = true)
        private String userName;

        @Schema(type = "String", example = "010-9999-8888", description = "고객 연락처", required = true)
        private String phonenumber;

         @Schema(type = "String", example = "user@user.com", description = "사용자 id", required = true)
        private String userId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HCCCServiceREQ {
        @Schema(type = "String", example = "123456", description = "승강기번호", required = true)
        @NotNull
        @NotBlank
        private String sid; // elevatorNo

        @Schema(type = "String", example = "123456", description = "통합프로젝트번호", required = true)
        @NotNull
        @NotBlank
        private String intgPrjNo; // INTG_PRJ_NO

        @Schema(type = "String", example = "A01", description = "거래선코드", required = true)
        @NotNull
        @NotBlank
        private String dealLineDiv; // TRLINE_CD

        @Schema(type = "String", example = "123456", description = "원프로젝트번호", required = true)
        @NotNull
        @NotBlank
        private String prjNo; // PRJ_NO

        @Schema(type = "String", example = "L01", description = "호기번호", required = true)
        @NotNull
        @NotBlank
        private String hoNo;

        @Schema(type = "String", example = "00102", description = "서비스유형(소)", required = true)
        @NotNull
        @NotBlank
        private String conslScd;

        @Schema(type = "String", example = "고장층", description = "고장층")
        @NotNull
        @NotBlank
        private String bkdnFlor;

        @Schema(type = "String", example = "1층에서 멈췄습니다.", description = "접수 내용")
        private String acptCntn;

        @Schema(type = "String", example = "W", description = "WEB:W, MOBILE:M", required = true)
        private String acptGubun;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InspectSearch extends AbstractRequest{
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
    public static class InspectSearchByPortfolio extends AbstractRequest{
        @Schema(type = "BigDecimal", example = "21", description = "포트폴리오 키 값")
        private BigDecimal userPortfolioMappingId;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InspectSearchByPortfolioExtMonth extends AbstractRequest{
        @Schema(type = "String", example = "202301", description = "시작 월 정보")
        private String startMonth;
        @Schema(type = "String", example = "202301", description = "마지막 월 정보")
        private String endMonth;
        @Schema(type = "BigDecimal", example = "21", description = "포트폴리오 키 값")
        private BigDecimal userPortfolioMappingId;
        @Hidden
        private List<String> projnos;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DashboardWidgetREQ extends AbstractRequest{
        @Schema(type = "String", example = "gimapei@gmail.com", description = "사용자 이메일")
        private String userId;
        @Schema(type = "String", example = "{}", description = "위젯 설정")
        private String widgets;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InspectSearchBySelfInspectHead extends AbstractRequest{
        @Schema(type = "String", example = "2039448", description = "엘리베이터 코드",required = true)
        private String elevatorNo;
        @Schema(type = "String", example = "20230302", description = "점검일자",required = true)
        private String selChkBeginDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InspectSearchByInspectHead extends AbstractRequest{
        @Schema(type = "String", example = "40432022092900001339019341", description = "실패내역 코드",required = true)
        private String failCd;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ElvSearch extends AbstractRequest{
        @Schema(type = "String", example = "2039448", description = "엘리베이터 코드")
        private String elevatorNo;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class  InspectMasterVO{
        private String elevatorNo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SelfInspectHeadVO{
        @Schema(example = "2221141")
        private String elevatorNo;
        @Schema(example = "시흥센트럴돔그랑트리캐슬")
        private String buldNm;
        @Schema(example = "경기도 시흥시 은계호수로 49")
        private String address1;
        @Schema(example = "1-1")
        private String installationPlace;
        @Schema(example = "20230224")
        private String selChkBeginDate;
        @Schema(example = "합격")
        private String checkRst;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SelfInspectVO{
        @Schema(example = "2221141")
        private String elevatorNo;
        @Schema(example = "시흥센트럴돔그랑트리캐슬")
        private String buldNm;
        @Schema(example = "경기도 시흥시 은계호수로 49")
        private String address1;
        @Schema(example = "1-1")
        private String installationPlace;
        @Schema(example = "20230224")
        private String selChkBeginDate;
        private List<SelfInspectHistoryVO> details;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SelfInspectHistoryVO {
        @Schema(example = "20230224")
        private String elevatorNo;
        @Schema(example = "시흥센트럴돔그랑트리캐슬")
        private String buldNm;
        @Schema(example = "경기도 시흥시 은계호수로 49")
        private String address1;
        @Schema(example = "1-1")
        private String installationPlace;
        @Schema(example = "20230224")
        private String selChkBeginDate;
        @Schema(example = "현대엘리베이터서비스 주식회사")
        private String companyNm;
        @Schema(example = "20230227")
        private String registDt;
        @Schema(example = "1000")
        private String selChkStDt;
        @Schema(example = "1030")
        private String selChkEnDt;
        @Schema(example = "설치 및 작동 상태")
        private String selChkItemDtNm;
        @Schema(example = "송**")
        private String selChkUsnm;
        @Schema(example = "주개폐기")
        private String selChkItemNm;
        @Schema(example = "D")
        private String selChkResult;
        @Schema(example = "양호")
        private String selchkResultNm;
        @Schema(example = "null")
        private String subSelchkUsnm;
        @Schema(example = "1.1.1.1")
        private String titNo;
        @Schema(example = "null")
        private String cnfirmDt;
        @Schema(example = "양호")
        private String beforeResultNm;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InspectVO{
        @Schema(example = "2221141")
        private String elevatorNo;
        @Schema(example = "시흥센트럴돔그랑트리캐슬")
        private String buldNm;
        @Schema(example = "경기도 시흥시 은계호수로 49")
        private String address1;
        @Schema(example = "1-1")
        private String installationPlace;
        @Schema(example = "20221021")
        private String inspctDe;
        @Schema(example = "조건후합격")
        private String dispWords;
        @Schema(example = "91022022071200015429077262")
        private String failCd;
        @Schema(example = "20220827")
        private String applcStDt;
        @Schema(example = "20230826")
        private String applcEnDt;
        private List<InspectFail.Item> fails;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InspectHeadVO{
        @Schema(example = "2221141")
        private String elevatorNo;
        @Schema(example = "시흥센트럴돔그랑트리캐슬")
        private String buldNm;
        @Schema(example = "경기도 시흥시 은계호수로 49")
        private String address1;
        @Schema(example = "1-1")
        private String installationPlace;
        @Schema(example = "20221021")
        private String inspctDe;
        @Schema(example = "조건후합격")
        private String dispWords;
        @Schema(example = "91022022071200015429077262")
        private String failCd;
        @Schema(example = "20220827")
        private String applcStDt;
        @Schema(example = "20230826")
        private String applcEnDt;
        @Schema(example = "합격")
        private String checkRst;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ELCountStatusVO {
        private InspectTotalVO inspect;
        private CompMisuTotalVO contract;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InspectTotalVO{
        @Schema(example = "21")
        private Long total;
        @Schema(example = "16")
        private Long success;
        @Schema(example = "0")
        private Long fail;
        @Schema(example = "5")
        private Long not;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CCServiceVO{
        @Schema(example = "2023-03-01 14:22:22")
        private String registDt;

        @Schema(example = "0010")
        private String serviceType;

        @Schema(example = "고장신고")
        private String serviceNm;

        @Schema(example = "117149U2203B01")
        private String paidContractNumber;

        @Schema(example = "117149")
        private String prjNo;
        
        @Schema(example = "L01")
        private String hoNo;

        @Schema(example = "신고1")
        private String serviceContents;

        @Schema(example = "김영업")
        private String receiverNm;

        @Schema(example = "2025426")
        private String receiverEmpl;

        @Schema(example = "korea@korea.com")
        private String receiverEmail;

        @Schema(example = "2025426")
        private String elevatorNo;

        @Schema(example = "20200301")
        private String frstInstallationDe;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CCProjNoExtTRLineVO{
        private String intgPrjTrlineCdCode;
        private String projNo;
        private String buldNm;
        private String elevatorNo;
        private String frstInstallationDe;
        private String installationDe;
        private String installationPlace;
        private String applcEnDt;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DashBoradeSelfInspectCountInfoVO{
        @Schema(example = "206")
        private Long total = Long.valueOf(0);
        @Schema(example = "129")
        private Long success = Long.valueOf(0);
        @Schema(example = "79")
        private Long remote = Long.valueOf(0);
        @Schema(example = "50")
        private Long field = Long.valueOf(0);
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompMisuTotalVO{
        @Schema(example = "21")
        private Long total;
        @Schema(example = "21")
        private Long success;
        @Schema(example = "0")
        private Long misu;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ELInfoVO{
        @Schema(example = "5044796")
        private String elevatorNo;
        @Schema(example = "126453")
        private String intgPrjNo;
        @Schema(example = "A03")
        private String trlineCd;
        @Schema(example = "126453")
        private String prjNo;
        @Schema(example = "L09")
        private String hoNo;
        @Schema(example = "5901065")
        private String salesEmpl;
        @Schema(example = "이석형")
        private String salesEmplNm;
        @Schema(example = "126453U2205-A03")
        private String compsCntrNo;
        @Schema(example = "세종특별자치시  달빛1로 39")
        private String address1;
        @Schema(example = "(종촌동)")
        private String address2;
        @Schema(example = "2022-10-01")
        private String applcBeDt;
        @Schema(example = "2023-09-30")
        private String applcEnDt;
        @Schema(example = "세종특별자치시")
        private String sigungunm;
        @Schema(example = "339")
        private String buldMgtNo1;
        @Schema(example = "552")
        private String buldMgtNo2;
        @Schema(example = "가재마을2단지")
        private String buldNm;
        @Schema(example = "엘리베이터")
        private String elvtrDivNm;
        @Schema(example = "VVVF")
        private String elvtrDetailForm;
        @Schema(example = "소방구조/장애인용")
        private String elvtrKindNm;
        @Schema(example = "운행중")
        private String elvtrSttsNm;
        @Schema(example = "2014-04-09")
        private String frstInstallationDe;
        @Schema(example = "2014-04-09")
        private String installationDe;
        @Schema(example = "110동 80호")
        private String installationPlace;
        @Schema(example = "1000")
        private String liveLoad;
        @Schema(example = "15")
        private String ratedCap;
        @Schema(example = "B1-15")
        private String shuttleSection;
        @Schema(example = "16")
        private String shuttleFloorCnt;
        @Schema(example = "18")
        private String groundFloorCnt;
        @Schema(example = "1")
        private String undgrndFloorCnt;
        @Schema(example = "36.5027375")
        private String wgslat;
        @Schema(example = "127.2436664")
        private String wgslon;
        @Schema(example = "서울 세종(대전)영업소")
        private String mgtComNm;
        @Schema(example = "-")
        private String mgtComHp;
        @Schema(example = "-")
        private String mgtComTel;
        @Schema(example = "20220501")
        private String contractStartDt;
        @Schema(example = "20230430")
        private String contractEndDt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ELInfoJoinToHCCC extends ELInfoVO{
        @Schema(example = "Y")
        private String elStatus;
        @Schema(example = "완료")
        private String activityStatus;
        @Schema(example = "20230718")
        private String acptDate;
        @Schema(example = "L")
        private String elType;
        @Schema(example = "19")
        private String elLEa;
        @Schema(example = "19")
        private String elTEa;
        @Schema(example = "110동 801호")
        private String installationPlace;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CalenderVO {
        // private List<SelfInspectVO> selfInspect;
        // private List<InspectVO> inspect;
        // private List<ELInfoJoinToHCCC> failMgmt;
        @Schema(example = "2018111112345")
        private String acptNo;
        @Schema(example = "123456")
        private String intgPrjNo;
        @Schema(example = "A01")
        private String trlineCd;
        @Schema(example = "123456")
        private String prjNo;
        @Schema(example = "L01")
        private String hoNo;
        @Schema(example = "1234567")
        private String elNo;
        @Schema(example = "서초상풍APT")
        private String prjNm;
        @Schema(example = "103동1호기")
        private String hoNm;
        @Schema(example = "20230320>")
        private String targetDt;
        @Schema(example = "도착")
        private String status;
        @Schema(example = "FAULT_HIS")
        private String dType;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CCElevatorInfoVO {
        @Schema(example = "1234567")
        private String elevatorNo;
        @Schema(example = "183376")
        private String prjNo;
        @Schema(example = "엘리베이터")
        private String elvtrDiv;
        @Schema(example = "L01")
        private String hoNo;
        @Schema(example = "운행중")
        private String elvtrSttsNm;
        @Schema(example = "2023-11-16")
        private String applcEnDt;
        @Schema(example = "2020-10-27")
        private String installationDe;
        @Schema(example = "현대엘리베이터(주)")
        private String installationPlace;
        @Schema(example = "해성빌딩")
        private String buldNm;
        @Schema(example = "110동 801호")
        private String inspCompanyNm;
        @Schema(example = "-")
        private String inspCompanyTel;
        @Schema(example = "현대해상")
        private String issueCompanyNm;
        @Schema(example = "20240213")
        private String issueContEnDe;
        @Schema(example = "이*곤")
        private String shuttleMngrNm;
        @Schema(example = "2023-11-23")
        private String mgrValdEndDt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CCElevatorDetailInfoVO {
        @Schema(example = "1234567")
        private String elevatorNo;
        @Schema(example = "183376")
        private String prjNo;
        @Schema(example = "엘리베이터")
        private String elvtrDiv;
        @Schema(example = "건물명")
        private String buildNm;
        @Schema(example = "L01")
        private String hoNo;
        @Schema(example = "운행중")
        private String elvtrSttsNm;
        @Schema(example = "2022-11-16")
        private String inspctDe;
        @Schema(example = "2022-11-16")
        private String inspctDays;
        @Schema(example = "2022-11-16")
        private String inspctCycle;
        @Schema(example = "2022-11-16")
        private String applcBeDt;
        @Schema(example = "2023-11-16")
        private String applcEnDt;
        @Schema(example = "2020-10-27")
        private String installationDe;
        @Schema(example = "110동 801호")
        private String installationPlace;
        @Schema(example = "현대엘리베이터(주)")
        private String inspCompanyNm;
        @Schema(example = "-")
        private String inspCompanyTel;
        @Schema(example = "현대해상")
        private String issueCompanyNm;
        @Schema(example = "20240213")
        private String issueContStDe;
        @Schema(example = "20240213")
        private String issueContEnDe;
        @Schema(example = "이*곤")
        private String shuttleMngrNm;
        @Schema(example = "2023-11-23")
        private String mgrValdStrDt;
        @Schema(example = "2023-11-23")
        private String mgrValdEndDt;
        @Schema(example = "ST7")
        private String model;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CCBillHistoryVO {
        @Schema(example = "2023-01-25")
        private String invoiceDt;
        @Schema(example = "구분")
        private String billType;
        @Schema(example = "182609U2212B01")
        private String compsCntrNo;
        @Schema(example = "56215699535280")
        private String virtualAccNoSh;
        @Schema(example = "79019219759518")
        private String virtualAccNoNh;
        @Schema(example = "999.00")
        private String unbillAmt;
        @Schema(example = "999.00")
        private String invoiceAmt;
        @Schema(example = "KRW")
        private String currency;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CCUnitsTreeVO {
        @Schema(example = "P2")
        private String id;
        @Schema(example = "0")
        private String parentId;
        @Schema(example = "빌딩명")
        private String buldNm;
        @Schema(example = "엘레베이터명")
        private String elNm;

        @Schema(example = "LOBBY")
        private String name;

        @Schema(example = "110동 801호")
        private String installationPlace;
        
        @Schema(example = "el")
        private String type;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IntegratedInspectVO{
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
        @Schema(example = "20230101")
        private String endDate;
        @Schema(example = "양호")
        private String chkResult;
        @Schema(example = "40432022092900001339018441")
        private String failCd;
        @Schema(example = "202305")
        private String yyyymm;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IntegratedMasterInfoVO{
        @Schema(example = "2221141")
        private String elevatorNo;
        @Schema(example = "시흥센트럴돔그랑트리캐슬")
        private String buldNm;
        @Schema(example = "현대엘리베이터")
        private String crtCompany;
        @Schema(example = "엘리베이터")
        private String elvtrDivNm;
        @Schema(example = "123456L01")
        private String prjNo;
        @Schema(example = "경기도 시흥시...")
        private String address1;
        @Schema(example = "운동장")
        private String installationPlace;
        @Schema(example = "20230101")
        private String installationDe;
        @Schema(example = "현대엘리베이터")
        private String selfCompany;
        @Schema(example = "B1-4")
        private String shuttleSection;
        @Schema(example = "22")
        private String shuttleFloorCnt;
        @Schema(example = "검사기관")
        private String inspctInsttNm;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CCCurrentElevatorChangeDateResponse {
        @Schema(example = "0061859")
        private String elevatorNo;
        @Schema(example = "126453L01")
        private String projNo;
        @Schema(example = "2012-07-24")
        private String installationDe;
        @Schema(example = "2023-07-24")
        private String changeDate;
        @Schema(example = "2023-07-24")
        private String applcEnDt;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlarmSearchREQ extends AbstractRequest {
        @Schema(type = "String", example = "gimapei@gmail.com", description = "사용자 아이디(이메일 주소)")
        private String userId;

        @Schema(type = "String", example = "0010", description = "정기점검 마스터데이터 코드")
        private String alarmType;

        @Schema(type = "String", example = "20230101", description = "검색 시작일")
        private String startDate;
        @Schema(type = "String", example = "20230131", description = "검색 종료일")
        private String endDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlarmHisVO{
        @Schema(example = "1")
        private BigDecimal alarmSendId;

        @Schema(example = "0010")
        private BigDecimal msgTemplateId;

        @Schema(example = "MMS")
        private String alarmMethod;

        @Schema(example = "101101")
        private String alarmType;

        @Schema(example = "0011")
        private String alarmEvent;
        
        @Schema(example = "고장신고 A123456")
        private String contents;

        @Schema(example = "gimapei@gmail.com")
        private String receiverId;

        @Schema(example = "01044449999")
        private String receiverPhoneNo;

        @Schema(example = "2023-04-03 17:00:00")
        private String registDt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MsgTemplateSearchREQ extends AbstractRequest {
        @Schema(type = "String", example = "접수번호", description = "템플릿 제목을 like검색")
        private String searchKeyword;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MsgTemplateVO {
        @Schema(example = "1")
        private BigDecimal msgTemplateId;

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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PrjNoVO {
        @Schema(example = "123456")
        private String prjNo;

        @Schema(example = "L01")
        private String hoNo;

        @Schema(example = "123456L01")
        private String prjNos;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MsgTemplateUpdateREQ extends AbstractRequest {
        @Schema(example = "1")
        @NotNull
        @NotBlank
        private BigDecimal msgTemplateId;    

        @Schema(example = "템플릿 소개")
        private String msgTemplateNote;

        @Schema(example = "템플릿 내용입니다. ")
        private String msgTemplateContents;

    }   


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BuldNmVO {
        private String prjhno;    
        private String buld_nm;

    }   


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServiceReqInfoVO{
        @Hidden
        private BigDecimal serviceHisId;
        @Schema(example = "0020")
        private String serviceType;
        @Schema(example = "138083")
        private String elevatorNo;
        @Schema(example = "123456U123111")
        private String compsCntrNo;
        @Schema(example = "138083A01")
        private String intgPrjNoTrlineCd;
        @Schema(example = "138083")
        private String intgPrjNo;
        @Schema(example = "A01")
        private String trlineCd;        
        @Schema(example = "138083")
        private String prjNo;       
         @Schema(example = "L01")
        private String hoNo;
        @Schema(example = "세금계산서")
        private String contents;
        @Schema(example = "나영업")
        private String receiverNm;
        @Schema(example = "20230224")
        private String receiverEmpl;
        @Schema(example = "korea@korea.com")
        private String receiverEmail;
        @Schema(example = "user@korea.com")
        private String userId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class mailLogVO{
        @Hidden
        private BigDecimal emailSeq;
        @Schema(example = "from@user.com")
        private String fromAddress;
        @Schema(example = "to@user.com")
        private List<String> toAddress;
        @Schema(example = "to@user.com")
        private List<String> ccAddress;
        @Schema(example = "to@user.com")
        private List<String> bccAddress;
        @Schema(example = "메일 제목")
        private String subject;
        @Schema(example = "메일 본문")
        private String content;
        @Schema(example = "사용자 ID")
        private String userId;
    }
}
