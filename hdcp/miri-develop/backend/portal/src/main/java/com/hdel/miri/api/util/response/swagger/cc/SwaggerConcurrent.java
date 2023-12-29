package com.hdel.miri.api.util.response.swagger.cc;

import com.hdel.miri.api.domain.cc.CC;
import com.hdel.miri.api.domain.cc.xml.InspectFail;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import lombok.Getter;

import java.util.List;

public class SwaggerConcurrent {
    @Getter
    public static class GetConcurrentSelfInspResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<CC.SelfInspectVO>> {
    }
    @Getter
    public static class GetConcurrentSelfInspHeadResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<CC.SelfInspectHeadVO>> {
    }
    @Getter
    public static class GetConcurrentSelfInspectHistoryVOResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<CC.SelfInspectHistoryVO>> {
    }
    @Getter
    public static class GetConcurrentInspHeadResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<CC.InspectHeadVO>> {
    }
    @Getter
    public static class GetConcurrentInspectFailItemsResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<InspectFail.Item>> {
    }
    @Getter
    public static class GetConcurrentInspResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<CC.InspectVO>> {
    }
    @Getter
    public static class GetConcurrentInspTotalResponse extends SwaggerResponse.SwaggerSuccessCapsule<CC.ELCountStatusVO> {
    }
    @Getter
    public static class GetConcurrentElevatorListResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<CC.CCElevatorInfoVO>> {
    }
    @Getter
    public static class GetConcurrentElevatorResponse extends SwaggerResponse.SwaggerSuccessCapsule<CC.CCElevatorDetailInfoVO> {
    }
    @Getter
    public static class CCCurrentElevatorChangeDateResponse extends SwaggerResponse.SwaggerSuccessCapsule<CC.CCCurrentElevatorChangeDateResponse> {
    }
    @Getter
    public static class GetCCBillHistoryListResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<CC.CCBillHistoryVO>> {
    }
    @Getter
    public static class GetCCUnitsTreeResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<CC.CCUnitsTreeVO>> {
    }
    @Getter
    public static class GetCCIntegratedInspectVOResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<CC.IntegratedInspectVO>> {
    }
    @Getter
    public static class GetCCIntegratedMasterInfoVOResponse extends SwaggerResponse.SwaggerSuccessCapsule<CC.IntegratedMasterInfoVO> {
    }
    @Getter
    public static class GetCCDashBoradeSelfInspectCountInfoVOResponse extends SwaggerResponse.SwaggerSuccessCapsule<CC.DashBoradeSelfInspectCountInfoVO> {
    }
    @Getter
    public static class GetCCCalenderVOListResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<CC.CalenderVO>> {
    }
    @Getter
    public static class GetCCServiceVOListResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<CC.CCServiceVO>> {
    }



}
