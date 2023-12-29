package com.hdel.miri.api.util.response.swagger.privilege.role;

import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import lombok.Getter;

public class SwaggerPrivilegeRole {
    @Getter
    public static class PrivilegeRoleBoolResponse extends SwaggerResponse.SwaggerSuccessCapsule<Boolean> {
    }
}
