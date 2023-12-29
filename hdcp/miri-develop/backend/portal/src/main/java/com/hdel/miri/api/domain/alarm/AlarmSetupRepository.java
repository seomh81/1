package com.hdel.miri.api.domain.alarm;


import com.hdel.miri.api.global.config.database.annotation.DS2Annotation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@DS2Annotation
@Mapper
public interface AlarmSetupRepository {

    List<AlarmSetup.AlarmSetupVO> selectListByNotPush(AlarmSetup.AlarmSetupCurrent request);
    List<AlarmSetup.AlarmSetupVO> selectListByPush(AlarmSetup.AlarmSetupCurrent request);
    int insertDefault(AlarmSetup.AlarmDefaultCreate request);
    int update(AlarmSetup.AlarmSetupUpdate request);
}
