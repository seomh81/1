package com.hdel.miri.api.util.ksign;

import com.ksign.spin.apilib.v3.SpinApiData;
import com.ksign.spin.apilib.v3.SpinApiLib;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.util.Properties;

@Slf4j
@Component
public class KsignSPinCrypto {
    private static Properties props = null;

    private static final String strSEED_Jumin     = "P001"; // �ֹι�ȣ ��ȣȭ(8��° ���� �κ� ��ȣȭ)
    private static final String strSEED_Acc       = "P002"; // ���¹�ȣ ��ȣȭ
    private static final String strSEED_Card      = "P003"; // ī���ȣ ��ȣȭ
    private static final String strSHA256_Pwd     = "P004"; // ��й�ȣ

    private static final int nSEED_Jumin     = 0;
    private static final int nSEED_Acc       = 1;
    private static final int nSEED_Card      = 2;
    private static final int nSHA256_Pwd     = 3;


    // SecurePIN Ʈ   IP:PORT;IP:PORT
    private static String strConnInfValue  = "";

    // 1 : Round-Robin
    // 2 : Active Standby
    private static int    nConnInfoSchedule = 1;

    private static String strSenderName     = "";

    private static String strSenderIp       = null;


    private SpinApiLib crypto = null;

    @PostConstruct
    public void postConstruct() {
        if(crypto == null) {
            if(props == null) {

                try {
                    // Sender IP
                    InetAddress local = InetAddress.getLocalHost();
                    strSenderIp = "172.16.90.1";
                    log.info("Sender IP : {}",strSenderIp);
                } catch(Exception e) {
                    e.printStackTrace();
                }

                String strTmp = "";
                strTmp = "10.105.99.100"; //PIP
                String pPort = "6901"; //PPort
                strTmp = strTmp + ":" + pPort + ";";

                strConnInfValue = strTmp;
                strTmp = "";
                strTmp = "10.105.107.100"; //SIP

                String sPort = "6901"; //SPort
                if(strTmp != null && !strTmp.equals("")) strTmp = strTmp + ":" + sPort + ";";

                strConnInfValue += strTmp;

                strTmp = "2";

                if(strTmp != null && !strTmp.equals("")) nConnInfoSchedule = Integer.parseInt(strTmp);

                strSenderName = "SCRM";
                log.debug("##########암호화타겟 strSenderName##########" + strSenderName);
            }
            try {
                crypto = new SpinApiLib();

                crypto.initializeRemote(strConnInfValue, nConnInfoSchedule, strSenderName);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private String encSEED(String plainText, int nType) {
        SpinApiData[] data = new SpinApiData[1];
        data[0] = new SpinApiData(plainText);
        String strRet = plainText;

        if(crypto != null) {
            try {
                switch(nType) {
                    case nSEED_Jumin   : data[0].setPolicyId(strSEED_Jumin); crypto.encryptEx(data, strSenderName, strSenderIp); strRet = data[0].getOutput(); break;
                    case nSEED_Acc     : data[0].setPolicyId(strSEED_Acc  ); crypto.encryptEx(data, strSenderName, strSenderIp); strRet = data[0].getOutput(); break;
                    case nSEED_Card    : data[0].setPolicyId(strSEED_Card ); crypto.encryptEx(data, strSenderName, strSenderIp); strRet = data[0].getOutput(); break;
                    case nSHA256_Pwd   : data[0].setPolicyId(strSHA256_Pwd); crypto.encryptEx(data, strSenderName, strSenderIp); strRet = data[0].getOutput(); break;
                    default            : break;
                }
            } catch(Exception e) {
                switch(nType) {
                    case nSEED_Jumin   : log.debug(" Ksign Encrypt Message : [Jumin    ]"); break;
                    case nSEED_Acc     : log.debug(" Ksign Encrypt Message : [Acc      ]"); break;
                    case nSEED_Card    : log.debug(" Ksign Encrypt Message : [Card     ]"); break;
                    case nSHA256_Pwd   : log.debug(" Ksign Encrypt Message : [Pwd      ]"); break;
                    default            : log.debug(" Ksign Encrypt Message : [default  ]"); break;
                }
                strRet = plainText;
                e.printStackTrace();
            }
        }
        return strRet;
    }

    // ���� ��ȣȭ �Լ�
    private String decSEED(String cipherText, int nType) {
        SpinApiData[] data = new SpinApiData[1];
        data[0] = new SpinApiData(cipherText);
        String strRet = cipherText;
        if(crypto != null) {
            try {
                switch(nType) {
                    case nSEED_Jumin   : data[0].setPolicyId(strSEED_Jumin); crypto.decryptEx(data, strSenderName, strSenderIp); strRet = data[0].getOutput(); break;
                    case nSEED_Acc     : data[0].setPolicyId(strSEED_Acc  ); crypto.decryptEx(data, strSenderName, strSenderIp); strRet = data[0].getOutput(); break;
                    case nSEED_Card    : data[0].setPolicyId(strSEED_Card ); crypto.decryptEx(data, strSenderName, strSenderIp); strRet = data[0].getOutput(); break;
                    default            : break;
                }
            } catch(Exception e) {
                switch(nType) {
                    case nSEED_Jumin     : log.debug(" Ksign Decrypt Message : [Jumin    ]"); break;
                    case nSEED_Acc       : log.debug(" Ksign Decrypt Message : [Acc      ]"); break;
                    case nSEED_Card      : log.debug(" Ksign Decrypt Message : [Card     ]"); break;
                    default              : log.debug(" Ksign Decrypt Message : [default  ]"); break;
                }
                strRet = cipherText;
                e.printStackTrace();
            }
        }
        log.debug(strRet);
        return strRet;
    }

    // Column�� ��ȣȭ �Լ�
    public String encJumin    (String plainText) { return encSEED(plainText, nSEED_Jumin    ); }
    public String encAcc      (String plainText) { return encSEED(plainText, nSEED_Acc      ); }
    public String encCard     (String plainText) { return encSEED(plainText, nSEED_Card     ); }
    public String encPwd      (String plainText) { return encSEED(plainText, nSHA256_Pwd    ); }

    // Column�� ��ȣȭ �Լ�
    public String decJumin    (String cipherText) { return decSEED(cipherText, nSEED_Jumin    ); }
    public String decAcc      (String cipherText) { return decSEED(cipherText, nSEED_Acc      ); }
    public String decCard     (String cipherText) { return decSEED(cipherText, nSEED_Card     ); }

    //	// ���ڿ� �迭 �����ȣȭ �Լ�
    public String[] encStringArray(String[] strArray, int nType) {
        if(crypto != null) {
            for(int i = 0; i < strArray.length; i++) {
                strArray[i] = encSEED(strArray[i], nType);
            }
        }
        return strArray;
    }

    // ���ڿ� �迭 ���뺹ȣȭ �Լ�
    public String[] decStringArray(String[] strArray, int nType) {
        if(crypto != null) {
            for(int i = 0; i < strArray.length; i++) {
                strArray[i] = decSEED(strArray[i], nType);
            }
        }
        return strArray;
    }

    // �迭 Column�� ��ȣȭ �Լ�
    public String[] encJumin    (String[] plainText) { return encStringArray(plainText, nSEED_Jumin    ); }
    public String[] encAcc      (String[] plainText) { return encStringArray(plainText, nSEED_Acc      ); }
    public String[] encCard     (String[] plainText) { return encStringArray(plainText, nSEED_Card     ); }
    public String[] encPwd      (String[] plainText) { return encStringArray(plainText, nSHA256_Pwd    ); }

    // �迭 Column�� ��ȣȭ �Լ�
    public String[] decJumin    (String[] cipherText) { return decStringArray(cipherText, nSEED_Jumin  ); }
    public String[] decAcc      (String[] cipherText) { return decStringArray(cipherText, nSEED_Acc    ); }
    public String[] decCard     (String[] cipherText) { return decStringArray(cipherText, nSEED_Card   ); }
}
