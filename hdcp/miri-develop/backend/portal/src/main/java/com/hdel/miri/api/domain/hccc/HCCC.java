package com.hdel.miri.api.domain.hccc;

import com.hdel.miri.api.domain.cc.CC;
import com.hdel.miri.api.domain.contract.Contract;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import com.hdel.miri.api.util.request.AbstractRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class HCCC {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractJoin {
        private String prevMonth;
        private List<Contract.ContractJoin> list;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContractPeriod {
        private List<Contract.ContractJoin> list;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HCCCJoinWithMobile {
        private Boolean mobile;
        private List<CC.ELInfoJoinToHCCC> list;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HCCCHoStatusVO {
        private String elStatus;
        private String activityStatus;
        private String acptDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HCCCFailStatusVO {
        @Schema(example = "[ 가재마을2단지 ] - L18 호기")
        private String text;
        @Schema(example = "L18 호기")
        private String hoNo;
        @Schema(example = "Y")
        private String elStatus;
        @Schema(example = "수리완료")
        private String activityStatus;
        @Schema(example = "2023-03-17")
        private String updDt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HCCCRunAvgVO {
        @Schema(example = "99.85")
        private Double elAvg;
        @Schema(example = "0.0")
        private Double esAvg;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HCCCCountVO {
        @Schema(example = "22")
        private Long allCount;
        @Schema(example = "0")
        private Long failCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HCCCELESCountVO {
        @Schema(example = "22")
        private Long run;
        @Schema(example = "0")
        private Long notRun;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HCCCELESCountVOResponse {
        private HCCCELESCountVO el;
        private HCCCELESCountVO es;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HCCCFailCountInfoVO {
        @Schema(example = "119")
        private String failComplete;
        @Schema(example = "5")
        private String failDetect;
        @Schema(example = "124")
        private String remoteRepair;
        @Schema(example = "86")
        private String fieldRepair;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HCCCRunAvgInPeriod {
        @Schema(example = "99.8")
        private String month;
        @Schema(example = "99.9")
        private String quarter;
        @Schema(example = "99.9")
        private String halfyear;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HCCCFailRemakrSearchVO extends AbstractRequest {
        @Schema(example = "136785L01")
        private String prjhno;

        @Schema(example = "[\"교체\", \"점검\"]")
        @Hidden
        private List<String> orStrs;

        @Schema(example = "[\"교체완료\", \"점검완료\"]")
        @Hidden
        private List<String> notStrs;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HCCCReceptionDetailVO {
        @Schema(example = "2023123111111")
        private String acptNo;
        @Schema(example = "남초공대APT")
        private String prjNm;
        @Schema(example = "123456L01")
        private String carNo;
        @Schema(example = "010999")
        private String conslMcd;
        @Schema(example = "운행이상")
        private String conslMcdNm;
        @Schema(example = "020999")
        private String conslScd;
        @Schema(example = "도어 동작 이상")
        private String conslScdNm;
        @Schema(example = "030999")
        private String bkdnLocLcd;
        @Schema(example = "CAR(Cage)")
        private String bkdnLocLcdNm;
        @Schema(example = "040999")
        private String bkdnLocMcd;
        @Schema(example = "Car Door Oper")
        private String bkdnLocMcdNm;
        @Schema(example = "050999")
        private String bkdnLocScd;
        @Schema(example = "Clutch Assy")
        private String bkdnLocScdNm;
        @Schema(example = "060999")
        private String bkdnCauCd;
        @Schema(example = "접촉불량")
        private String bkdnCauCdNm;
        @Schema(example = "070999")
        private String procRsltCd;
        @Schema(example = "조정")
        private String procRsltCdNm;
        @Schema(example = "완료")
        private String status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HCCCtargetReceptionVO extends AbstractRequest {
        @Schema(example = "2023123111111", description = "고장 접수 상세 내역 조회를 위한 접수 번호")
        private String targetAcptNo;
    }
}
