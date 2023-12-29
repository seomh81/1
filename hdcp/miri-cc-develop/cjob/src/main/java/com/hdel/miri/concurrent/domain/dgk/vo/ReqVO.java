package com.hdel.miri.concurrent.domain.dgk.vo;
import com.hdel.miri.concurrent.util.request.DefaultAbstract;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

public class ReqVO {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper=false)
    public static final class ElevatorTargetVO extends DefaultAbstract {
        @Schema(type = "List<ElVO>"
                , example = "[{\"elevator_no\":\"0064207\", \"db_type\":\"SCRM\"},{\"elevator_no\":\"0066108\", \"db_type\":\"SCRM\"}]"
                , description = "국가 승강기 번호 List, 이 리스트가 Nlll 또는 공백이 아니면 lastUpdateDt는 무시")
        private List<ElVO> elList;

        @Schema(type = "String"
                , example = "20230301"
                , description = "SCRM/SRM의 마지막 업데이트 날짜, Null 또는 공백의 경우 Default 어제로, ALL or all은 조건전체를 대상으로")
        private String lastUpdateDt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper=false)
    public static final class ElevatorTargetSyncVO extends DefaultAbstract {
        @Schema(type = "List<String>"
                , example = "[\"0064207\",\"0064207\"]"
                , description = "국가 승강기 번호 List in SCRM")
        private List<String> scrmList;

        @Schema(type = "List<String>"
        , example = "[\"0064207\",\"0064207\"]"
        , description = "국가 승강기 번호 List in SRM")
        private List<String> srmList;

        @Schema(type = "String"
                , example = "20230301"
                , description = "SCRM/SRM의 마지막 업데이트 날짜, Null 또는 공백의 경우 Default 어제로, ALL or all은 조건전체를 대상으로")
        private String lastUpdateDt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper=false)
    public static final class SyncElevatorTargetVO extends DefaultAbstract {
        @Schema(type = "List"
                , example = "[\"1234567\",\"2345678\"]"
                , description = "국가 승강기 번호 List, 이 리스트가 Nlll이면 CT_EL_SYNC_TARGET 에 들어있는 번호를 대상으로 한다.")
        private List<String> elList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper=false)
    public static final class SyncElevatorTargetVO2 extends DefaultAbstract {
        @Schema(type = "List"
                , example = "[\"1234567\",\"2345678\"]"
                , description = "국가 승강기 번호 List, 이 리스트가 Nlll이면 CT_EL_SYNC_TARGET 에 들어있는 번호를 대상으로 한다.")
        private List<String> elList;

        @Schema(type = "String"
        , example = "202303"
        , description = "Sync할 년월")
        private String syncYYYMM;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper=false)
    public static final class SyncInspElevatorTargetVO extends DefaultAbstract {
        @Schema(type = "List"
                , example = "[\"1234567\",\"2345678\"]"
                , description = "국가 승강기 번호 List, 이 리스트가 Nlll이면 CT_EL_SYNC_TARGET 에 들어있는 번호를 대상으로 한다.")
        private List<String> elList;

        @Schema(type = "String"
        , example = "2022"
        , description = "Sync할 년도")
        private String syncYear;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper=false)
    public static final class UnBilledElevatorTargetVO extends DefaultAbstract {
        @Schema(type = "List<String>"
                , example = "[\"0064207\",\"0064207\"]"
                , description = "국가 승강기 번호 List in SCRM")
        private List<String> scrmList;

        @Schema(type = "List<String>"
        , example = "[\"0064207\",\"0064207\"]"
        , description = "국가 승강기 번호 List in SRM")
        private List<String> srmList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class ElVO {
        private String elevator_no;
        private String del_yn;
        private String db_type;
        private String change_dt;
        private String job_nm;
        private BigDecimal concurrent_err_id;
        private String intgPrjNo;
        private String trlineCd;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class NoElVO {
        private String db_type;
        private String elevator_no;
        private String pjt;
        private String hno;
        private String upn;
        private String cst;
        private String gno;
        private String cont_start;
        private String cont_end;
        private String change_dt; 
        private String miricall_yn;
        private String miriview_yn;
        private String mirirobot_yn;
        private String miri_yn;
        private String site_nm;
        private String cust_nm;
        private String sales_empl;
        private String sales_empl_nm;
        private String sales_gcdnm;
        private String org_cd;
        private String org_nm; 
        private String hrtsYn; 
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class TargetMailVO {
        @Schema(example = "accId@hyundaielevator.com")
        private String accId;
        @Schema(example = "영업사원명")
        private String accNm;
        @Schema(example = "manager@user.com")
        private String mngId;
        @Schema(example = "건물관리자명")
        private String mngNm;
        @Schema(example = "연락처")
        private String mngPhone;
        @Schema(example = "유사계약번호")
        private String compsCntrNo;
        @Schema(example = "등록일자")
        private String regDt;
        @Schema(example = "조회기간")
        private String terms;
        @Schema(example = "메일양식")
        private String msgContent;
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

    //2023-10-04 add 
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class ElInfoVO {
        private String chkitem;
        private String elevator_no;
        private String dbtype;
    }

    //2023-10-13 add 
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class UserVO {
        private String userId;
        private String userName;
    }
}
