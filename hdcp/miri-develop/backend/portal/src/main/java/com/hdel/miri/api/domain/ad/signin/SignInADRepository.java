package com.hdel.miri.api.domain.ad.signin;

import com.hdel.miri.api.global.config.database.annotation.DS2Annotation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@DS2Annotation
@Mapper
public interface SignInADRepository {
    List<String> selectImages();
    List<SignInAD.SignInADVO> selectList(SignInAD.SignInADSearch request);
    List<SignInAD.SignInADDetailVO> selectDetails(SignInAD.SignInADDetailSearch request);
    int insert(SignInAD.SignInADCreate request);
    int insertDetails(SignInAD.SignInADDetailCreate request);
    int update(SignInAD.SignInADUpdate request);
    int active(SignInAD.SignInADActivation request);
    int inactive(SignInAD.SignInADActivation request);
    int remove(SignInAD.SignInADRemove request);
    //int deleteDetails(SignInAD.SignInADRemove request);
    int deleteDetail(SignInAD.SignInADDetailDelete request);
}
