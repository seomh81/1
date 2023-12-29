package com.hdel.miri.api.util.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ResultCode {
    SUCCESS("success")
    ,FAILURE("failure")
    ,REFRESH("refresh")
    ,IN_PROGRESS("outstanding authorization")
    ,PROVE("prove");
    String value;
}
