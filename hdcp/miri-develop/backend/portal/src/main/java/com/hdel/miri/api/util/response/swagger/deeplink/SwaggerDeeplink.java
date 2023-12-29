package com.hdel.miri.api.util.response.swagger.deeplink;

import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import lombok.Getter;

public class SwaggerDeeplink {
    @Getter
    public static class DeeplinkBoolResponse extends SwaggerResponse.SwaggerSuccessCapsule<Boolean> {
    }
}
