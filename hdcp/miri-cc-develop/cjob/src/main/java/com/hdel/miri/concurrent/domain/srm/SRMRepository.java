package com.hdel.miri.concurrent.domain.srm;

import com.hdel.miri.concurrent.global.config.database.annotation.DS3Annotation;
import org.apache.ibatis.annotations.Mapper;

import com.hdel.miri.concurrent.domain.dgk.vo.ReqVO;
import com.hdel.miri.concurrent.domain.scrm.SCRM;
import com.hdel.miri.concurrent.domain.user.User;

import java.util.List;

@DS3Annotation
@Mapper
public interface SRMRepository {
    List<ReqVO.ElVO> getCcTargetElList(ReqVO.ElevatorTargetSyncVO request);
    List<ReqVO.NoElVO> getCcTargetNoElList(ReqVO.ElevatorTargetSyncVO request);
    List<ReqVO.NoElVO> getOrgInfoList();
    
    List<SCRM.VO> getInternalKeysFromSRM(List data); 

    //2023-10-04 gmw add
    List<SCRM.VO> getSrmElInfo(String elevatorNo); 

    //2023-10-13 gmw add
    List<User.DefaultUserCreate> getAutoRegUserList();   
    int updateAutoUserRegister(User.DefaultUserCreate req);
}
