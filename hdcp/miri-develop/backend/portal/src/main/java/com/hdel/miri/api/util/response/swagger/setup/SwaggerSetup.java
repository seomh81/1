package com.hdel.miri.api.util.response.swagger.setup;

import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import lombok.Getter;

public class SwaggerSetup {
    @Getter
    public static class SetupBoolResponse extends SwaggerResponse.SwaggerSuccessCapsule<Boolean> {
    }
}
