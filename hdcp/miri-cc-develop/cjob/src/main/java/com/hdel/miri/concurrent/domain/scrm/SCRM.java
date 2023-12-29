package com.hdel.miri.concurrent.domain.scrm;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

public class SCRM {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VO {
        private String prjNo;
        private String hoNo;
        private String intgPrjNo;
        private String trlineCd;
        private String salesEmpl;
        private String salesEmplNm;

        private String mgtComTel;
        private String mgtComHp;
        private String mgtComCd;
        private String mgtComNm;
        private String contractStartDt;
        private String contractEndDt;
        
        private String compsCntrNo;
        private String elevatorNo;
        private String address1;
        private String address2;
        private String applcBeDt;
        private String applcEnDt;
        private String resultNm;
        private String areaNm;
        private String sigunguNm;
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
        private String wgsLon;
        private String wgsLat;
        private String miricallYn;
        private String miriviewYn;
        private String mirirobotYn;
        private String miriYn;
        private String creationUser;
        private String changeDt;
        private String siteNm;
        private String custNm; 
        private String model; 
        private String hrtsYn;
        //2023-10-04 add
        private String chkitem;
    }
}
