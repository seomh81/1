package com.hdel.miri.api.domain.privilege.role;

import com.hdel.miri.api.global.config.database.annotation.DS2Annotation;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.HashMap;

@DS2Annotation
@Mapper
public interface RoleRepository {
    int insert(Role.RoleCreate request);
    int copy(HashMap<String, BigDecimal> request);
    int update(Role.RoleUpdate request);
    int move(Role.RoleMove request);
    int delete(Role.RoleDelete request);
}
