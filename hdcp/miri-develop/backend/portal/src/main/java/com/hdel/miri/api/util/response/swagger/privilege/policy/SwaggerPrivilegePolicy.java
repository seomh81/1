package com.hdel.miri.api.util.response.swagger.privilege.policy;

import com.hdel.miri.api.domain.privilege.policy.Policy;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import lombok.Getter;

import java.util.List;

public class SwaggerPrivilegePolicy {
    @Getter
    public static class GetPrivilegePolicyFunctionsByViewResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<Policy.FunctionsByView>> {
    }
    @Getter
    public static class GetPrivilegePolicyViewsByMenuResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<Policy.ViewsByMenu>> {
    }
    @Getter
    public static class GetPrivilegePolicyMenusByRoleResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<Policy.MenusByRole>> {
    }
    @Getter
    public static class GetPrivilegePolicyUsersByRoleResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<Policy.UsersByRole>> {
    }
    @Getter
    public static class PrivilegePolicyBoolResponse extends SwaggerResponse.SwaggerSuccessCapsule<Boolean> {
    }

}
