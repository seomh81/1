package com.hdel.miri.api.util.response.swagger.auth;

import com.hdel.miri.api.domain.auth.AuthVO;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import lombok.Getter;

public class SwaggerAuth {
    @Getter
    public static class AuthTokenResponse extends SwaggerResponse.SwaggerSuccessCapsule<AuthVO.Token> {
    }
    @Getter
    public static class AuthMyInfoResponse extends SwaggerResponse.SwaggerSuccessCapsule<AuthVO.Token> {
    }
    @Getter
    public static class AuthBoolResponse extends SwaggerResponse.SwaggerSuccessCapsule<Boolean> {
    }
}
