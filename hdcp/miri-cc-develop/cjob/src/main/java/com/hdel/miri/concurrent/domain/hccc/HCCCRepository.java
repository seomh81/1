package com.hdel.miri.concurrent.domain.hccc;

import com.hdel.miri.concurrent.domain.scrm.SCRM;
import com.hdel.miri.concurrent.global.config.database.annotation.DS4Annotation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@DS4Annotation
@Mapper
public interface HCCCRepository {
    List<SCRM.VO> getGisFromHccc(List<SCRM.VO> data);
    //2023-10-17 add
    List<SCRM.VO> getGisFromHcccUpdate(List<SCRM.VO> data);
}
