package com.hdel.miri.api.domain.auth;

import com.hdel.miri.api.global.config.database.annotation.DS2Annotation;
import org.apache.ibatis.annotations.Mapper;

@DS2Annotation
@Mapper
public interface TokenRepository {
    AuthVO.Token selectOne(String userId);
    int countByUserId(String userId);
    int merge(AuthVO.Token token);
    int delete(String userId);
}
