package com.hdel.miri.api.domain.ad.popup;

import com.hdel.miri.api.global.config.database.annotation.DS2Annotation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@DS2Annotation
@Mapper
public interface PopupADRepository {
    List<PopupAD.PopupADVO> selectList(PopupAD.PopupADSearch request);
    int insert(PopupAD.PopupADCreate request);
    int update(PopupAD.PopupADUpdate request);
    int active(PopupAD.PopupADActivation request);
    int inactive(PopupAD.PopupADActivation request);
    int remove(PopupAD.PopupADRemove request);
}
