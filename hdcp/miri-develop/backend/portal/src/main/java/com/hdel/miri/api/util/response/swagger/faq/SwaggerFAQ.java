package com.hdel.miri.api.util.response.swagger.faq;

import com.hdel.miri.api.domain.faq.FAQ;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import lombok.Getter;

import java.util.List;

public class SwaggerFAQ {
    @Getter
    public static class GetFAQVOListResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<FAQ.FAQVO>> {
    }
    @Getter
    public static class GetFAQBoolResponse extends SwaggerResponse.SwaggerSuccessCapsule<Boolean> {
    }
}
