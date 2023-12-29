package com.hdel.miri.concurrent.util.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResultStatus {
    SUCCESS("success")
    , FAILURE("failure");
    String status;

}
