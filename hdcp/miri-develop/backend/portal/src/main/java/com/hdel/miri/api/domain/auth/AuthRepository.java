package com.hdel.miri.api.domain.auth;

import com.hdel.miri.api.domain.auth.AuthVO.UserIdsVO;
import com.hdel.miri.api.domain.auth.AuthVO.ValidUserVO;
import com.hdel.miri.api.global.config.database.annotation.DS2Annotation;
import com.hdel.miri.api.util.request.AbstractRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@DS2Annotation
@Mapper
public interface AuthRepository {
    Optional<AuthVO.Summary> findByEmail(String userId);
    AuthVO.My selectByCurrentUser(AbstractRequest request);

    List<ValidUserVO> checkAccountValidate(UserIdsVO request);
    List<ValidUserVO> checkAccountValidate2(UserIdsVO request);

    void upsertFirebaseUserId(AuthVO.SignIn request);
    
    String GetNotAllowStatusUser(AuthVO.SignIn request);
}
