package com.hdel.miri.concurrent.domain.scrm;

import com.hdel.miri.concurrent.domain.dgk.vo.ReqVO;
import com.hdel.miri.concurrent.domain.message.CcMessageVO.CcKakaoVO;
import com.hdel.miri.concurrent.domain.message.CcMessageVO.CcMMSVO;
import com.hdel.miri.concurrent.domain.mms.mmsVO;
import com.hdel.miri.concurrent.domain.mms.mmsVO.request;
import com.hdel.miri.concurrent.domain.user.User;
import com.hdel.miri.concurrent.global.config.database.annotation.DS2Annotation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.HashMap;

@DS2Annotation
@Mapper
public interface SCRMRepository {
    List<ReqVO.ElVO> getCcTargetElList(ReqVO.ElevatorTargetSyncVO request);
    List<ReqVO.NoElVO> getCcTargetNoElList(ReqVO.ElevatorTargetSyncVO request);
    List<ReqVO.NoElVO> getOrgInfoList();

    List<SCRM.VO> getInternalKeysFromSCRM(List data); 

    //2023-10-04 gmw add
    List<SCRM.VO> getScrmElInfo(String elevatorNo); 

    //2023-10-13 gmw add
    List<User.DefaultUserCreate> getAutoRegUserList();   
    int updateAutoUserRegister(User.DefaultUserCreate req);
    int insertSendMMS(mmsVO.request request);
    
    int SendMMS(CcMMSVO req);
    int SendKAKAO(CcKakaoVO req);
}
