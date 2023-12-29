package com.hdel.miri.concurrent.util;

import java.io.StringReader;

import javax.xml.XMLConstants;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import javax.xml.XMLConstants;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public class xmltester {

    public static void main(String[] args) {
        String str = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><response><header><resultCode>00</resultCode><resultMsg>NORMAL SERVICE.</resultMsg></header><body><items><item><companyNm>KB손해보험</companyNm><contEnDe>20231227</contEnDe><contStDe>20221227</contStDe><elevatorNo>0001283</elevatorNo></item><item><companyNm>AIG손해보험</companyNm><contEnDe>20231230</contEnDe><contStDe>20221230</contStDe><elevatorNo>0001283</elevatorNo></item></items><numOfRows>100</numOfRows><pageNo>1</pageNo><totalCount>2</totalCount></body></response>";
        Document doc = convertStringToXml(str);

        System.out.println(doc);
    }
    
    private static Document convertStringToXml(String xmlString) {
        SAXBuilder sax = new SAXBuilder();
        sax.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        sax.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        try {

            Document doc = sax.build(new StringReader(xmlString));
            return doc;

        } catch (JDOMException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
