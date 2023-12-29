package com.hdel.miri.api.domain.scrm;

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

public class SCRM {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SCRMCurrentPortfolioRequest extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "21", description = "사용자 포트폴리오 매핑 번호")
        private BigDecimal userPortfolioMappingId;
        @Schema(type = "String", example = "현장명", description = "현장명")
        private String keyword;
        @Schema(type = "String", example = "20230101", description = "사용자 포트폴리오 매핑 번호")
        private String startDate;
        @Schema(type = "String", example = "20230131", description = "사용자 포트폴리오 매핑 번호")
        private String endDate;

        @Hidden
        private List<CC.CCProjNoExtTRLineVO> join;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SCRMCurrentWBSNoRequest extends AbstractRequest {
        @Schema(type = "String", example = "N10870L01", description = "호기 번호")
        private String wbsNo;
        
        @Schema(type = "String", example = "20160607", description = "호기 번호")
        private String chngDt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SCRMUnitPurchaseMSTResponse {
        @Schema(example = "123456B01")
        private String intgPrjTrlineCdCode;
        @Schema(example = "123456L01")
        private String wbsNo;
        @Schema(example = "PC BOARD,CCB-3 ASSY,")
        private String materCont;
        @Schema(example = "20160607")
        private String chngDt;
        @Schema(example = "1234567")
        private String elevatorNo;
        @Schema(example = "20120101")
        private String frstInstallationDe;
        @Schema(example = "20130301")
        private String installationDe;
        @Schema(example = "110동 801호")
        private String installationPlace;
        @Schema(example = "20230331")
        private String applcEnDt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SCRMCurrentWBSNoResponse {
        @Schema(type = "String", example = "205003148", description = "부품 번호")
        private String materNo;
        @Schema(type = "String", example = "PC BOARD,CCB-3 ASSY,", description = "부품명")
        private String materCont;
        @Schema(type = "String", example = "0", description = "크기")
        private String materVol;
        @Schema(type = "String", example = "0", description = "치수")
        private String materSize;
        @Schema(type = "String", example = "0", description = "단위")
        private String materUnit;
        @Schema(type = "String", example = "0", description = "자재 유형")
        private String materType;
    }
}
