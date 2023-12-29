package com.hdel.miri.api.util.response.swagger.hccc;

import com.hdel.miri.api.domain.cc.CC;
import com.hdel.miri.api.domain.hccc.HCCC;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import lombok.Getter;

import java.util.List;

public class SwaggerHCCC {
    @Getter
    public static class GetHCCCCountVOResponse extends SwaggerResponse.SwaggerSuccessCapsule<HCCC.HCCCCountVO> {
    }
    @Getter
    public static class GetHCCCRunAvgVOResponse extends SwaggerResponse.SwaggerSuccessCapsule<HCCC.HCCCRunAvgVO> {
    }
    @Getter
    public static class GetHCCCFailStatusVOResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<HCCC.HCCCFailStatusVO>> {
    }
    @Getter
    public static class GetHCCCELInfoJoinToHCCCResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<CC.ELInfoJoinToHCCC>> {
    }
    @Getter
    public static class GetHCCCFailCountInfoVOResponse extends SwaggerResponse.SwaggerSuccessCapsule<HCCC.HCCCFailCountInfoVO> {
    }
    @Getter
    public static class GetHCCCTargetReceptionInfoVOResponse extends SwaggerResponse.SwaggerSuccessCapsule<HCCC.HCCCReceptionDetailVO> {
    }

}
