package com.hdel.miri.api.util.response.swagger.hrts;

import com.hdel.miri.api.domain.hrts.HRTS;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import lombok.Getter;

import java.util.List;

public class SwaggerHRTS {
    @Getter
    public static class GetHRTSLongResponse extends SwaggerResponse.SwaggerSuccessCapsule<Long> {
    }
    @Getter
    public static class GetHRTSResultRunAvgVOResponse extends SwaggerResponse.SwaggerSuccessCapsule<HRTS.ResultRunAvgVO> {
    }
    @Getter
    public static class GetHRTSConnectionResultVOResponse extends SwaggerResponse.SwaggerSuccessCapsule<HRTS.HRTSConnectionResultVO> {
    }
    @Getter
    public static class GetHRTSRuleVOResponse extends SwaggerResponse.SwaggerSuccessCapsule<HRTS.HRTSRuleVO> {
    }
    @Getter
    public static class GetHRTSMonitResultVOResponse extends SwaggerResponse.SwaggerSuccessCapsule<HRTS.HRTSMonitResultVO> {
    }

    @Getter
    public static class GetHRTSMasterJoinListResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<HRTS.HRTSMasterJoin>> {
    }
    @Getter
    public static class GetHRTSMasterJoinWebResponse extends SwaggerResponse.SwaggerSuccessCapsule<HRTS.HRTSMasterJoinWeb> {
    }
    @Getter
    public static class GetHRTSCurrentMonthRunTimeTargetVOOResponse extends SwaggerResponse.SwaggerSuccessCapsule<HRTS.CurrentMonthRunTimeTargetVO> {
    }
    @Getter
    public static class GetHRTSResultDOCCAvgVOResponse extends SwaggerResponse.SwaggerSuccessCapsule<HRTS.ResultDOCCAvgVO> {
    }
    @Getter
    public static class GetHRTSPerformanceCheckResultVOResponse extends SwaggerResponse.SwaggerSuccessCapsule<HRTS.HRTSPerformanceCheckResultVO> {
    }
    @Getter
    public static class GetHRTSRemoteInspectVOListResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<HRTS.HRTSRemoteInspectVO>> {
    }


}
