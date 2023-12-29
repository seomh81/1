package com.hdel.miri.api.domain.notice;

import com.hdel.miri.api.global.config.database.annotation.DS2Annotation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@DS2Annotation
@Mapper
public interface NoticeRepository {
    List<Notice.NoticeVO> selectList(Notice.NoticeSearch request);
    int insert(Notice.NoticeCreate request);
    int update(Notice.NoticeUpdate request);
    int remove(Notice.NoticeRemove request);
}
