package com.hdel.miri.api.util.response.swagger.privilege.menu;

import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import lombok.Getter;

public class SwaggerPrivilegeMenu {
    @Getter
    public static class SwaggerMenuBoolResponse extends SwaggerResponse.SwaggerSuccessCapsule<Boolean> {
    }
}
