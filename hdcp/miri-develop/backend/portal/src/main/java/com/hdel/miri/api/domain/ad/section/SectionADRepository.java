package com.hdel.miri.api.domain.ad.section;

import com.hdel.miri.api.global.config.database.annotation.DS2Annotation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@DS2Annotation
@Mapper
public interface SectionADRepository {
    List<String> selectImages();
    List<SectionAD.SectionADVO> selectList(SectionAD.SectionADSearch request);
    List<SectionAD.SectionADDetailVO> selectDetails(SectionAD.SectionADDetailSearch request);
    int insert(SectionAD.SectionADCreate request);
    int insertDetails(SectionAD.SectionADDetailCreate request);
    int update(SectionAD.SectionADUpdate request);
    int active(SectionAD.SectionADActivation request);
    int inactive(SectionAD.SectionADActivation request);
    int remove(SectionAD.SectionADRemove request);
    //int deleteDetails(SectionAD.SectionADRemove request);
    int deleteDetail(SectionAD.SectionADDetailDelete request);
}
