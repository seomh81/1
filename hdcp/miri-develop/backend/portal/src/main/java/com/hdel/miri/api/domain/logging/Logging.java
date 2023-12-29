package com.hdel.miri.api.domain.logging;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Logging {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Log {
        private BigDecimal logId;
        private String userId;
        private String controllerName;
        private String inValue;
        private String outValue;
        private String errorYn;
        private String errorContents;
        private String platform;
        private String mobile;
        private String userAgent;
        private Timestamp startDt;
        private Timestamp endDt;
        private String creationUser;
        private String lastupdateUser;
    }
}
