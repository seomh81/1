package com.hdel.miri.api.util.response.swagger.ad.video;

import com.hdel.miri.api.domain.ad.video.VideoAD;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import lombok.Getter;

import java.util.List;

public class SwaggerVideoAD {
    @Getter
    public static class GetVideoADListResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<VideoAD.VideoADVO>> {
    }
    @Getter
    public static class VideoADBoolResponse extends SwaggerResponse.SwaggerSuccessCapsule<Boolean> {
    }
}
