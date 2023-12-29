package com.hdel.miri.concurrent.domain.auth;

import com.hdel.miri.concurrent.global.config.database.annotation.DS1Annotation;
import org.apache.ibatis.annotations.Mapper;

@DS1Annotation
@Mapper
public interface TokenRepository {
    AuthVO.Token selectOne(String userId);
    int countByUserId(String userId);
    int merge(AuthVO.Token token);
    int delete(String userId);
}
