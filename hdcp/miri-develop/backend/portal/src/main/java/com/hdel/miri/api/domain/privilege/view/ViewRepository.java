package com.hdel.miri.api.domain.privilege.view;

import com.hdel.miri.api.global.config.database.annotation.DS2Annotation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@DS2Annotation
@Mapper
public interface ViewRepository {
    List<View.ViewVO> selectList(View.ViewSearch request);
    int insert(View.ViewCreate request);
    int update(View.ViewUpdate request);
    int delete(View.ViewDelete request);
}
