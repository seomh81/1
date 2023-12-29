package com.hdel.miri.api.util.response.swagger.privilege.func;

import com.hdel.miri.api.domain.privilege.func.FunctionVO;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import lombok.Getter;

import java.util.List;

public class SwaggerPrivilegeFunc {
    @Getter
    public static class GetFunctionsResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<FunctionVO.Function>> {
    }
    @Getter
    public static class FunctionsBoolResponse extends SwaggerResponse.SwaggerSuccessCapsule<Boolean> {
    }
}
