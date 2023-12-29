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
public class SelfInspectHis {

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
        @XmlElement(name = "companyNm")
        private String companyNm;
        @XmlElement(name = "elevatorNo")
        private String elevatorNo;
        @XmlElement(name = "registDt")
        private String registDt;
        @XmlElement(name = "selChkStDt")
        private String selChkStDt;
        @XmlElement(name = "selChkEnDt")
        private String selChkEnDt;
        @XmlElement(name = "selChkItemDtlNm")
        private String selChkItemDtlNm;
        @XmlElement(name = "selChkItemNm")
        private String selChkItemNm;
        @XmlElement(name = "selChkResult")
        private String selChkResult;
        @XmlElement(name = "selchkBeginDate")
        private String selchkBeginDate;
        @XmlElement(name = "selchkResultNm")
        private String selchkResultNm;
        @XmlElement(name = "selchkUsnm")
        private String selchkUsnm;
        @XmlElement(name = "subSelchkUsnm")
        private String subSelchkUsnm;
        @XmlElement(name = "titNo")
        private String titNo;
        @XmlElement(name = "cnfirmDt")
        private String cnfirmDt;
    }
}
