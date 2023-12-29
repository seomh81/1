package com.hdel.miri.api.domain.masterdata;

import com.hdel.miri.api.global.config.database.annotation.DS2Annotation;
import com.hdel.miri.api.domain.masterdata.MasterData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@DS2Annotation
@Mapper
public interface MasterDataRepository {
    List<MasterData.MasterDataHeadVO> selectMasterHeadList(MasterData.MasterDataHeadSearch request);
    List<MasterData.MasterDataDetailVO> selectMasterDetailList(MasterData.MasterDataDetailSearch request);
    List<MasterData.MasterDataDetailVO> selectDetails(MasterData.MasterDataSelect request);
    List<MasterData.AlarmCode> selectAlarmSetup();
    int insertMasterHead(MasterData.MasterDataHeadCreate request);
    int insertMasterDetail(MasterData.MasterDataDetailCreate request);

    int insertMasterDetailImport(MasterData.MasterDataDetailImport request);

    int updateMasterHead(MasterData.MasterDataHeadUpdate request);
    int updateMasterDetail(MasterData.MasterDataDetailUpdate request);
    int deleteMasterHeads(MasterData.MasterDataHeadDelete request);
    int deleteMasterDetailByMasterIds(MasterData.MasterDataHeadDelete request);
    int deleteMasterDetails(MasterData.MasterDataDetailDelete request);

}
