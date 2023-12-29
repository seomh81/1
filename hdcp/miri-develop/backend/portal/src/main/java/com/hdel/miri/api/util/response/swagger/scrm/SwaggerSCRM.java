package com.hdel.miri.api.util.response.swagger.scrm;

import com.hdel.miri.api.domain.scrm.SCRM;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import lombok.Getter;

import java.util.List;

public class SwaggerSCRM {
    @Getter
    public static class SCRMUnitPurchaseMSTResponseListResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<SCRM.SCRMUnitPurchaseMSTResponse>> {
    }
}
