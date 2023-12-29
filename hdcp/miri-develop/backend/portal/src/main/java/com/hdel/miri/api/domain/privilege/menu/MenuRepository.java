package com.hdel.miri.api.domain.privilege.menu;

import com.hdel.miri.api.global.config.database.annotation.DS2Annotation;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.HashMap;

@DS2Annotation
@Mapper
public interface MenuRepository {
    int insert(Menu.MenuCreate request);
    int copy(HashMap<String, BigDecimal> request);
    int update(Menu.MenuUpdate request);
    int move(Menu.MenuMove request);
    int delete(Menu.MenuDelete request);
}
