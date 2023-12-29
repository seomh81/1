package com.hdel.miri.api.domain.cc.xml;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "response")
public class InspectFail {

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
        @Schema(example = "자동구출운전 수단 작동상태 불량")
        @XmlElement(name = "failDesc")
        private String failDesc;
        @Schema(example = "1.2.1.4")
        @XmlElement(name = "standardArticle")
        private String standardArticle;
        @Schema(example = "마) 자동구출운전장치가 「엘리베이터 안전기준」 13.2.3.6에 따라 작동하는지 확인한다.")
        @XmlElement(name = "standardTitle1")
        private String standardTitle1;
    }
}
