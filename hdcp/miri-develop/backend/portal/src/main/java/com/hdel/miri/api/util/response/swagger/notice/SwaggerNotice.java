package com.hdel.miri.api.util.response.swagger.notice;

import com.hdel.miri.api.domain.notice.Notice;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import lombok.Getter;

import java.util.List;

public class SwaggerNotice {
    @Getter
    public static class GetNoticeResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<Notice.NoticeVO>> {
    }
    @Getter
    public static class NoticeBoolResponse extends SwaggerResponse.SwaggerSuccessCapsule<Boolean> {
    }
}
