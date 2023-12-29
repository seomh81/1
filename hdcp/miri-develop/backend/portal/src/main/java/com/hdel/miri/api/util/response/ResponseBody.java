package com.hdel.miri.api.util.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
public class ResponseBody {
    private String result;
    private Object data;
    private Object because;

}
