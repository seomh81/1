package com.hdel.miri.api.domain.ad.video;

import com.hdel.miri.api.global.config.database.annotation.DS2Annotation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@DS2Annotation
@Mapper
public interface VideoADRepository {
    List<VideoAD.VideoADVO> selectList(VideoAD.VideoADSearch request);
    int insert(VideoAD.VideoADCreate request);
    int update(VideoAD.VideoADUpdate request);
    int active(VideoAD.VideoADActivation request);
    int inactive(VideoAD.VideoADActivation request);
    int remove(VideoAD.VideoADRemove request);
}
