package com.hdel.miri.concurrent.domain.auth;

import com.hdel.miri.concurrent.global.config.database.annotation.DS1Annotation;
import com.hdel.miri.concurrent.util.request.AbstractRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@DS1Annotation
@Mapper
public interface AuthRepository {
    Optional<AuthVO.Summary> findByEmail(String userId);
    AuthVO.My selectByCurrentUser(AbstractRequest request);
}
