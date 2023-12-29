package com.hdel.miri.api.util.response.swagger.ad.popup;

import com.hdel.miri.api.domain.ad.video.VideoAD;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import lombok.Getter;

import java.util.List;

public class SwaggerPopupAD {
    @Getter
    public static class GetPopupADListResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<VideoAD.VideoADVO>> {
    }
    @Getter
    public static class PopupADBoolResponse extends SwaggerResponse.SwaggerSuccessCapsule<Boolean> {
    }
}
