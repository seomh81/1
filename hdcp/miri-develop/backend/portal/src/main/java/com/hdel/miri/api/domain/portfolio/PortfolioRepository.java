package com.hdel.miri.api.domain.portfolio;

import com.hdel.miri.api.domain.miri.MIRI;
import com.hdel.miri.api.global.config.database.annotation.DS2Annotation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@DS2Annotation
@Mapper
public interface PortfolioRepository {
    List<Portfolio.VO> selectListByUser(Portfolio.PortfolioSearch request);
    int insert(Portfolio.PortfolioCreate request);
    int insertDefault(Portfolio.PortfolioDefaultCreate request);
    int update(Portfolio.PortfolioUpdate request);
    int updateDefault(Portfolio.PortfolioSelection request);
    int updateNotDefault(Portfolio.PortfolioSelection request);
    int delete(Portfolio.PortfolioDelete request);

    List<String> getElevatorNoByUserPortfolioMappingId(MIRI.MIRIServiceSearch request);
}
