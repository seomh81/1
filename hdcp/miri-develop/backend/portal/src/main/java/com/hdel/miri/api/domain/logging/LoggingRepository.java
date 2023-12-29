package com.hdel.miri.api.domain.logging;

import com.hdel.miri.api.global.config.database.annotation.DS2Annotation;
import org.apache.ibatis.annotations.Mapper;

@DS2Annotation
@Mapper
public interface LoggingRepository {
    int insert(Logging.Log request);
    int updateComplete(Logging.Log request);
    int updateError(Logging.Log request);
}
