package com.hdel.miri.concurrent.util.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
public class ResponseBody<D,E> implements Response<D,E>{
    private String result;
    private D data;
    private E because;


    @NoArgsConstructor
    @AllArgsConstructor
    public enum ResultEnum{
        SUCCESS("success")
        ,FAILURE("failure")
        ,REFRESH("refresh")
        ,PROVE("prove");
        String value;
    }
}
