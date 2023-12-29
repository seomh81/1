package com.hdel.miri.api.domain.terms;

import com.hdel.miri.api.global.config.database.annotation.DS2Annotation;
import org.apache.ibatis.annotations.Mapper;

@DS2Annotation
@Mapper
public interface TermsRepository {
    int insertDefault(Terms.TermsDefaultCreate request);
    int updateAdRecvAg(Terms.TermsUpdate request);
}