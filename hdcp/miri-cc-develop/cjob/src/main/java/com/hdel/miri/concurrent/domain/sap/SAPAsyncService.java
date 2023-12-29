package com.hdel.miri.concurrent.domain.sap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import com.hdel.miri.concurrent.domain.dgk.CcRepository;
import com.hdel.miri.concurrent.domain.dgk.vo.CcElevatorVO;
import com.hdel.miri.concurrent.domain.dgk.vo.CcLogVO;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoTable;
import org.springframework.scheduling.annotation.Async;

@Slf4j
@Service
@RequiredArgsConstructor
public class SAPAsyncService {
    private static final String DESC_NAME1 = "MIRI-SCRM";
    private static final String DESC_NAME2 = "MIRI-SRM";
    private final CcRepository      ccRepository;
    /*
     * 제목 : SAP로부터 미수금액을 가져온다. 
     * API : SAP RFC Communication
     */
    // @Async("CcAsyncExcutor1")
    public void getUnbilledInfo(String type, CcElevatorVO.CcElevatorInfoForSAP data) throws Exception {
        Date date_now = new Date(System.currentTimeMillis());
        SimpleDateFormat fourteen_format = new SimpleDateFormat("yyyyMMdd");
        String      nowDt   = fourteen_format.format(date_now);
        String      errStr  = "";
        String      inStr   = "";
        String      tempStr = "";
        int lastLogIdx = 10;

        if(data.getIntgPrjNo() != null && !"".equals(data.getIntgPrjNo()) && !" ".equals(data.getIntgPrjNo())) {
            try
            {
                JCoDestination dest=JCoDestinationManager.getDestination("SCRM".equals(type) ? DESC_NAME1 : DESC_NAME2);
                JCoFunction fn = dest.getRepository().getFunction("ZWEB_CS_GET_MISU");

                log.info("============== SAP FUNCTION ============ {}", type);
                log.info("IP:{}", dest.getApplicationServerHost());
                log.info("INTG:{}{}",data.getIntgPrjNo(),data.getTrlineCd() );
                log.info("fn : {}", fn.getName());

                if (fn == null) throw new RuntimeException("<"+fn+"> not found in SAP.");

                fn.getImportParameterList().setValue("STADA",   "20200101");
                fn.getImportParameterList().setValue("ENDDA",   nowDt);
                fn.getImportParameterList().setValue("I_DDAT",  nowDt);
                fn.getImportParameterList().setValue("I_PROJ",  data.getIntgPrjNo());
                fn.getImportParameterList().setValue("I_CST",   data.getTrlineCd());

                fn.execute(dest);
                inStr = fn.getImportParameterList().toJSON();
                
                HashMap rst = null;

                JCoTable rows = fn.getTableParameterList().getTable("T_ITAB");
                
                if(rows != null && !rows.isEmpty()) {
                    for (int i = 0; i < rows.getNumRows(); i++) {
                        rows.setRow(i);
                        rst = new HashMap();

                        tempStr = rows.toJSON();

                        log.info("============================================== ");
                        log.info("미수금액 : {}", rows.getString("MISUA"));

                        Integer misuAmount = 0;
                        Integer invoiceAmt = 0;
                        
                        if("SRM".equals(type)) {
                            if(rows.getString("MISUA") != null) misuAmount = (int)Math.round(Double.parseDouble(rows.getString("MISUA").trim()));
                            if(rows.getString("RKNETWR") != null) invoiceAmt = (int)Math.round(Double.parseDouble(rows.getString("RKNETWR").trim()));
                        } else {
                            if(rows.getString("MISUA") != null) misuAmount = (int)Math.round(Double.parseDouble(rows.getString("MISUA").trim()) * 100);
                            if(rows.getString("RKNETWR") != null) invoiceAmt = (int)Math.round(Double.parseDouble(rows.getString("RKNETWR").trim()) * 100);
                        }

                        // if(misuAmount <= 0 ) continue;
                        try{
                            rst.put("KUNNR", (String)rows.getString("KUNNR"));	
                            rst.put("VBELN", (String)rows.getString("VBELN"));	         // "0011108481",
                            rst.put("POSID",    (String)rows.getString("POSID").replace("-",""));       //"183797U2112A01",
                            rst.put("KUNNR_NM", (String)rows.getString("KUNNR_NM"));    //"김용일(명지주택)",
                            rst.put("BSTDK",    (String)rows.getString("BSTDK"));       //"2021-12-02",
                            rst.put("CONTR_DA", (String)rows.getString("CONTR_DA"));    //"0000-00-00",
                            rst.put("VKGRP_NM", (String)rows.getString("VKGRP_NM"));    //"부산지사(서비스)",
                            rst.put("BLDAT",    (String)rows.getString("BLDAT"));       //"2022-03-20",
                            rst.put("BANKVN1",  (String)rows.getString("BANKVN1"));     //"2022-03-20"79018245174177,
                            rst.put("BANKVN2",  (String)rows.getString("BANKVN2"));     //"2022-03-20"56214398720907,
                            rst.put("BUDAT",    (String)rows.getString("BUDAT"));       //"",
                            rst.put("RKFKDAT",  (String)rows.getString("RKFKDAT"));     //"2022-03-20",


                            log.info("============================================== ");
                            log.info("NETWR : {}", rows.getString("NETWR"));

                            rst.put("NETWR",    (String)rows.getString("NETWR").trim());       //"2022-03-20"4000,
                            rst.put("CPROGN",   (String)rows.getString("CPROGN"));      //"",
                            rst.put("WAERS",    (String)rows.getString("WAERS"));       //"KRW",
                            rst.put("BSTKD",    (String)rows.getString("BSTKD"));       //"명지주택(명지동)",
                            rst.put("RKVBELN",  type+(String)rows.getString("RKVBELN"));     //"2022-03-20"9900032254,
                            rst.put("RKNETWR",  invoiceAmt);     //"2022-03-20"1000,
                            rst.put("BANKL1",   (String)rows.getString("BANKL1"));      //"011",
                            rst.put("HWBAS",    (String)rows.getString("HWBAS").trim());       //"2022-03-20"0,
                            rst.put("BANKL2",   (String)rows.getString("BANKL2"));      //"088",
                            rst.put("MISUA",    misuAmount);       //"2022-03-20" 1000,
                            rst.put("ABR_NM",   (String)rows.getString("ABR_NM"));      //"부산 서부산직영"
                            rst.put("INTG_PRJ_NO", data.getIntgPrjNo());
                            rst.put("TRLINE_CD", data.getTrlineCd());
                        } catch(Exception ex) {
                            System.out.println(ex);
                        }

                        if(rst != null && rst.size() > 0) {
                            try {

                                ccRepository.upsertUnbilledInfo(rst);
                            } catch(Exception ex) {
                                CcLogVO.CcLogCreate idata = new CcLogVO.CcLogCreate();
                                idata.setJobNm("SAP미수");
                                idata.setInParams(inStr);
                                idata.setElNo(data.getIntgPrjNo());
                                idata.setErrorYn("Y");

                                log.error("SAP inrst != null && rst.size() > 0", ex);
                                lastLogIdx = ex.toString().length() >= 2000 ? 1800 : ex.toString().length();
                                log.error("lastIndex2:"+lastLogIdx);
                                errStr = ex.toString();
                                errStr = lastLogIdx == 0 ? "" : errStr.substring(0,lastLogIdx);
                                idata.setErrors(errStr);
                                ccRepository.insertStartLog(idata);
                            }
                        }
                    }
                }
            }
            catch (JCoException e)
            {
                CcLogVO.CcLogCreate idata = new CcLogVO.CcLogCreate();
                idata.setJobNm("SAP미수");
                idata.setInParams(inStr);
                idata.setElNo(data.getIntgPrjNo());
                idata.setErrorYn("Y");
                log.error("SAP insert error", tempStr);
            
                lastLogIdx = e.toString().length() >= 2000 ? 1800 : e.toString().length();
                log.error("lastIndex:"+lastLogIdx);
                errStr = e.toString();
                errStr = lastLogIdx == 0 ? "" : errStr.substring(0,lastLogIdx);
                idata.setErrors(errStr);
                ccRepository.insertStartLog(idata);

                log.error("SAP insert error", e);
                throw e;
            }
        }
    }   
}
