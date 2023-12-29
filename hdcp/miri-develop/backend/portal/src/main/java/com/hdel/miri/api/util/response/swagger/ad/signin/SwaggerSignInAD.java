package com.hdel.miri.api.util.response.swagger.ad.signin;

import com.hdel.miri.api.domain.ad.signin.SignInAD;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import lombok.Getter;

import java.util.List;

public class SwaggerSignInAD {

    @Getter
    public static class GetSignInADImagePathResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<String>> {
    }
    @Getter
    public static class GetSignInADVOResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<SignInAD.SignInADVO>> {
    }
    @Getter
    public static class GetSignInADDetailVOResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<SignInAD.SignInADDetailVO>> {
    }

    @Getter
    public static class SignInADBoolResponse extends SwaggerResponse.SwaggerSuccessCapsule<Boolean> {
    }
}
