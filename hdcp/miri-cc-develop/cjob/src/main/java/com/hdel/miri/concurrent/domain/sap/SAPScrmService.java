package com.hdel.miri.concurrent.domain.sap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hdel.miri.concurrent.domain.dgk.vo.CcElevatorVO;

@Slf4j
@Service
@RequiredArgsConstructor
public class SAPScrmService {
    private final SAPAsyncService   sapService;

    /*
     * 제목 : SAP 미수금 조회
     * API : SAP RFC Call
     */    
    public void syncUnbilledInfo(List<CcElevatorVO.CcElevatorInfoForSAP> _data) {
        // _data = null;
        if(_data != null && _data.size() > 0) {
            for(int i=0; i<_data.size(); i++) {
                try {
                    sapService.getUnbilledInfo("SCRM", _data.get(i));
                } catch(Exception ex) {
                    System.out.println(ex);
                }
            }
        }
    }

}
