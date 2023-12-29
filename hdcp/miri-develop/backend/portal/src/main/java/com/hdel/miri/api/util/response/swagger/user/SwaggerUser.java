package com.hdel.miri.api.util.response.swagger.user;

import com.hdel.miri.api.domain.user.User;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import lombok.Getter;

import java.util.List;

public class SwaggerUser {


    @Getter
    public static class GetUserAlreadyExtListResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<User.UserAlreadyExt>> {
    }
    @Getter
    public static class GetUserViewRenderingInfoResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<User.ViewRenderingByUser>> {
    }
    @Getter
    public static class GetUserSummaryResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<User.UserSummary>> {
    }
    @Getter
    public static class GetUserBoolResponse extends SwaggerResponse.SwaggerSuccessCapsule<Boolean> {
    }
    @Getter
    public static class GetUserSendMailResponse extends SwaggerResponse.SwaggerSuccessMailCapsule<String> {
    }

}
