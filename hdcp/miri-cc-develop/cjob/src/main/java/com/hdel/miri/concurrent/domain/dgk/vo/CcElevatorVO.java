package com.hdel.miri.concurrent.domain.dgk.vo;

import com.hdel.miri.concurrent.util.request.AbstractRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


public class CcElevatorVO {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper=false)
    public static final class CcElevatorInfoForSAP extends AbstractRequest {
        @Schema(type = "String", example = "통합 프로젝트 코드", description = "통합 프로젝트 코드")
        private String intgPrjNo;
        @Schema(type = "String", example = "거래선", description = "거래선")
        private String trlineCd;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper=false)
    public static final class VO {
        private String elevatorNo;
        private String prjNo;
        private String ho_no;
        private String intgPrjNo;
        private String trlineCd;
        private String resultCode;
        private String resultMsg;
        private String address1;
        private String address2;
        private String applcBeDt;
        private String applcEnDt;
        private String resultNm;
        private String areanm;
        private String sigungunm;
        private String buldMgtNo1;
        private String buldMgtNo2;
        private String buldNm;
        private String elvtrDivNm;
        private String elvtrForm;
        private String elvtrDetailForm;
        private String elvtrKindNm;
        private String elvtrSttsNm;
        private String frstInstallationDe;
        private String installationDe;
        private String installationPlace;
        private String liveLoad;
        private String ratedCap;
        private String shuttleSection;
        private String shuttleFloorCnt;
        private String groundFloorCnt;
        private String undgrndFloorCnt;
        private String wgslat;
        private String wgslon;
        private String creationDt;
        private String creationUser;
        private String lastupdateDt;
        private String lastupdateUser;
        private String miricallYn;
        private String miriviewYn;
        private String mirirobotYn;
    }
}
