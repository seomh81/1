package com.hdel.miri.api.util.response.swagger.masterdata;

import com.hdel.miri.api.domain.masterdata.MasterData;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import lombok.Getter;

import java.util.List;

public class SwaggerMasterData {
    @Getter
    public static class GetMasterDetailAPIResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<MasterData.MasterDataDetailVO>> {
    }
    @Getter
    public static class GetMasterHeadAPIResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<MasterData.MasterDataHeadVO>> {
    }
    @Getter
    public static class MasterDataBoolResponse extends SwaggerResponse.SwaggerSuccessCapsule<Boolean> {
    }
}
