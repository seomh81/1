package com.hdel.miri.concurrent.domain.dgk.xmlschema;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "response")
public class InspectHis {

    @XmlElement(name = "header")
    private Header header;

    @XmlElement(name = "body")
    private Body body;

    @Getter
    @ToString
    @XmlRootElement(name = "header")
    public static class Header {
        @XmlElement(name = "resultCode")
        private String resultCode;

        @XmlElement(name = "resultMsg")
        private String resultMsg;
    }

    @Getter
    @ToString
    @XmlRootElement(name = "body")
    public static class Body {
        @XmlElement(name = "items")
        private Items items;
        @XmlElement(name = "numOfRows")
        private String numOfRows;
        @XmlElement(name = "pageNo")
        private String pageNo;
        @XmlElement(name = "totalCount")
        private String totalCount;
    }

    @Getter
    @ToString
    @XmlRootElement(name = "items")
    public static class Items {
        @XmlElement(name = "item")
        private List<Item> item;
    }

    @Getter
    @ToString
    @XmlRootElement(name = "item")
    public static class Item {
        @XmlElement(name = "elevatorNo")
        private String elevatorNo;
        @XmlElement(name = "buldNm")
        private String buldNm;
        @XmlElement(name = "address1")
        private String address1;
        @XmlElement(name = "address2")
        private String address2;
        @XmlElement(name = "sido")
        private String sido;
        @XmlElement(name = "sigungu")
        private String sigungu;
        @XmlElement(name = "elvtrAsignNo")
        private String elvtrAsignNo;
        @XmlElement(name = "elvtrDiv")
        private String elvtrDiv;
        @XmlElement(name = "elvtrForm")
        private String elvtrForm;
        @XmlElement(name = "elvtrDetailForm")
        private String elvtrDetailForm;
        @XmlElement(name = "elvtrKindNm")
        private String elvtrKindNm;
        @XmlElement(name = "installationPlace")
        private String installationPlace;
        @XmlElement(name = "shuttleFloorCnt")
        private String shuttleFloorCnt;
        @XmlElement(name = "ratedSpeed")
        private String ratedSpeed;
        @XmlElement(name = "liveLoad")
        private String liveLoad;
        @XmlElement(name = "ratedCap")
        private String ratedCap;
        @XmlElement(name = "companyNm")
        private String companyNm;
        @XmlElement(name = "frstInstallationDe")
        private String frstInstallationDe;
        @XmlElement(name = "installationDe")
        private String installationDe;
        @XmlElement(name = "inspctDe")
        private String inspctDe;
        @XmlElement(name = "inspctKindNm")
        private String inspctKindNm;
        @XmlElement(name = "dispWords")
        private String dispWords;
        @XmlElement(name = "failCd")
        private String failCd;
        @XmlElement(name = "applcBeDt")
        private String applcBeDt;
        @XmlElement(name = "applcEnDt")
        private String applcEnDt;
        @XmlElement(name = "inspctInsttNm")
        private String inspctInsttNm;
        @XmlElement(name = "lastChiefApprDt")
        private String lastChiefApprDt;
    }
}
