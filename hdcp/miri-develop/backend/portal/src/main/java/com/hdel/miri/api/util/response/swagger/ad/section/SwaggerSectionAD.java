package com.hdel.miri.api.util.response.swagger.ad.section;

import com.hdel.miri.api.domain.ad.section.SectionAD;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import lombok.Getter;

import java.util.List;

public class SwaggerSectionAD {

    @Getter
    public static class SwaggerSectionADListResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<SectionAD.SectionADVO>> {
    }
    @Getter
    public static class SwaggerSectionADDetailListResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<SectionAD.SectionADDetailVO>> {
    }
    @Getter
    public static class SwaggerSectionADStringsResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<String>> {
    }
    @Getter
    public static class SwaggerSectionADBoolResponse extends SwaggerResponse.SwaggerSuccessCapsule<Boolean> {
    }
}
