package com.hdel.miri.api.domain.privilege.vue;

import com.hdel.miri.api.global.config.database.annotation.DS2Annotation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@DS2Annotation
@Mapper
public interface VueRepository {
    List<Vue.VueFile> selectList(Vue.VueSearch request);
    int insert(Vue.VueCreate request);
    int update(Vue.VueUpdate request);
    int delete(Vue.VueDelete request);
}
