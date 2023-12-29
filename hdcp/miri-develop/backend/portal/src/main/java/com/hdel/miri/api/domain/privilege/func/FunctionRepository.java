package com.hdel.miri.api.domain.privilege.func;

import com.hdel.miri.api.global.config.database.annotation.DS2Annotation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@DS2Annotation
@Mapper
public interface FunctionRepository {
    List<FunctionVO.Function> selectList(FunctionVO.FunctionSearch request);
    int insert(FunctionVO.FunctionCreate request);
    int update(FunctionVO.FunctionUpdate request);
    int delete(FunctionVO.FunctionDelete request);
}
