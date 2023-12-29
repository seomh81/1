package com.hdel.miri.api.util.response.swagger.privilege.view;

import com.hdel.miri.api.domain.privilege.view.View;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import lombok.Getter;

import java.util.List;

public class SwaggerPrivilegeView {
    @Getter
    public static class GetPrivilegeViewsResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<View.ViewVO>> {
    }
    @Getter
    public static class PrivilegeViewsBoolResponse extends SwaggerResponse.SwaggerSuccessCapsule<Boolean> {
    }
}
