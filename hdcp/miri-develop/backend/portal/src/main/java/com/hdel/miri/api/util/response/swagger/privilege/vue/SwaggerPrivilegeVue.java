package com.hdel.miri.api.util.response.swagger.privilege.vue;

import com.hdel.miri.api.domain.privilege.vue.Vue;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import lombok.Getter;

import java.util.List;

public class SwaggerPrivilegeVue {

    public static class GetPrivilegeVueFileListResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<Vue.VueFile>> {
    }
    @Getter
    public static class PrivilegeVueBoolResponse extends SwaggerResponse.SwaggerSuccessCapsule<Boolean> {
    }
}
