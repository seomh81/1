package com.hdel.miri.concurrent.domain.dgk.xmlschema;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "response")
public class ElevatorInfo {

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
        @XmlElement(name = "address1")
        private String address1;
        @XmlElement(name = "address2")
        private String address2;
        @XmlElement(name = "applcBeDt")
        private String applcBeDt;
        @XmlElement(name = "applcEnDt")
        private String applcEnDt;
        @XmlElement(name = "areaNm")
        private String areaNm;
        @XmlElement(name = "buldMgtNo1")
        private String buldMgtNo1;
        @XmlElement(name = "buldMgtNo2")
        private String buldMgtNo2;
        @XmlElement(name = "buldNm")
        private String buldNm;
        @XmlElement(name = "elevatorNo")
        private String elevatorNo;
        @XmlElement(name = "elvtrAsignNo")
        private String elvtrAsignNo;
        @XmlElement(name = "elvtrDetailForm")
        private String elvtrDetailForm;
        @XmlElement(name = "elvtrDivNm")
        private String elvtrDivNm;
        @XmlElement(name = "elvtrForm")
        private String elvtrForm;
        @XmlElement(name = "elvtrKindNm")
        private String elvtrKindNm;
        @XmlElement(name = "elvtrSttsNm")
        private String elvtrSttsNm;
        @XmlElement(name = "frstInstallationDe")
        private String frstInstallationDe;
        @XmlElement(name = "groundFloorCnt")
        private String groundFloorCnt;
        @XmlElement(name = "installationDe")
        private String installationDe;
        @XmlElement(name = "installationPlace")
        private String installationPlace;
        @XmlElement(name = "liveLoad")
        private String liveLoad;
        @XmlElement(name = "ratedCap")
        private String ratedCap;
        @XmlElement(name = "resultNm")
        private String resultNm;
        @XmlElement(name = "shuttleFloorCnt")
        private String shuttleFloorCnt;
        @XmlElement(name = "shuttleSection")
        private String shuttleSection;
        @XmlElement(name = "sigunguNm")
        private String sigunguNm;
        @XmlElement(name = "undgrndFloorCnt")
        private String undgrndFloorCnt;
    }
}
