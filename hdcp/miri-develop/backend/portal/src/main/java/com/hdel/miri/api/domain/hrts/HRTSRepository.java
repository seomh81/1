package com.hdel.miri.api.domain.hrts;

import com.hdel.miri.api.domain.cc.CC;
import com.hdel.miri.api.domain.cc.CC.PrjNoVO;
import com.hdel.miri.api.domain.hrts.HRTS.HRTSAvgRunDistanceResultVO;
import com.hdel.miri.api.global.config.database.annotation.DS5Annotation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@DS5Annotation
@Mapper
public interface HRTSRepository {
    /***
     * HRTS 마스터 정보
     */
    List<HRTS.HRTSMasterJoin> selectAIMasterJoin(HRTS.HRTSMasterSearch request);
    List<HRTS.HRTSMasterJoin> selectAIMasterJoinTest(HRTS.HRTSMasterSearch request);
    HRTS.HRTSMasterJoinWeb selectAIMasterJoinWeb(HRTS.HRTSMonitVO request);

    /**
     * 운행 정보
     * */
    List<HRTS.CurrentMonthRunTimeTargetVO> selectRunTimeCurrentMonth(HRTS.HRTSRunAvgSearch request);
    List<HRTS.RunAvgTotVO> selectRunAvgType(HRTS.HRTSRunAvgSearch request);
    List<HRTS.RunAvgTargetVO> selectRunAvgTarget(HRTS.HRTSRunAvgSearch request);
    HRTSAvgRunDistanceResultVO selectRunAvgTargetAll(List<PrjNoVO> request);
    /**
     * 도어 개폐
     * */
    List<HRTS.DOCCTypeVO> selectDOCCType(HRTS.HRTSRunAvgSearch request);
    List<HRTS.DOCCTargetVO> selectDOCCTarget(HRTS.HRTSRunAvgSearch request);

    /**
     * 성능 평가 결과
     * */
    HRTS.HRTSPerformanceCheckResultVO selectHRTSPerformanceCheckResult(HRTS.HRTSRunAvgSearch request);
    Long selectSelfInspRemoteWithMonth(CC.InspectSearchByPortfolioExtMonth request);
    List<HRTS.HRTSRemoteInspectVO> selectAIMasterInfoForWeb(HRTS.HRTSMasterSearchWeb request);

    /**
     * HRTS Report Download
     */
    HRTS.HRTSReportVO getHrtsReport(HRTS.HRTSReportReqVO request);
}
