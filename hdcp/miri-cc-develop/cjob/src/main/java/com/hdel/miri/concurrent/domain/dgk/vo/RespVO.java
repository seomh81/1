package com.hdel.miri.concurrent.domain.dgk.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import lombok.NoArgsConstructor;
import java.math.BigDecimal;

public class RespVO {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class ConcurrentLogVO {
        private BigDecimal concurrent_err_id;
        private String job_nm;
        private String elevator_no;
        private String in_params;
        private String creation_user;
    }
}
