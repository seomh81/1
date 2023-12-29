package com.hdel.miri.api.domain.faq;

import com.hdel.miri.api.global.config.database.annotation.DS2Annotation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@DS2Annotation
@Mapper
public interface FAQRepository {
    List<FAQ.FAQVO> selectList(FAQ.FAQSearchOption request);
    
    List<FAQ.FAQVO> selectListMobile(FAQ.FAQSearchOptionMobile request);
    
    int insert(FAQ.FAQCreate request);
    int update(FAQ.FAQUpdate request);
    int remove(FAQ.FAQRemove request);

}
