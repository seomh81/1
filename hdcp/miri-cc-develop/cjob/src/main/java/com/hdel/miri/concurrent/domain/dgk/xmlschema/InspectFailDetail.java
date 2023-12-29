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
public class InspectFailDetail {

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
        @XmlElement(name = "failDesc")
        private String failDesc;
        @XmlElement(name = "standardArticle")
        private String standardArticle;
        @XmlElement(name = "standardTitle1")
        private String standardTitle1;
    }
}
