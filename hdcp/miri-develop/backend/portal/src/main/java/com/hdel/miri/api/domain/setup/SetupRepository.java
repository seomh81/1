package com.hdel.miri.api.domain.setup;

import com.hdel.miri.api.global.config.database.annotation.DS2Annotation;
import org.apache.ibatis.annotations.Mapper;

@DS2Annotation
@Mapper
public interface SetupRepository {
    String selectRenderType(String userId);
    int updateTheme(Setup.SetupThemeUpdate request);
    int updateLocale(Setup.SetupLocaleUpdate request);
    int updateLandingPage(Setup.SetupLandingPageUpdate request);
    int insertDefault(Setup.SetupDefaultCreate request);
}
