package com.hdel.miri.api.domain.hccc;

import com.hdel.miri.api.domain.cc.CC;
import com.hdel.miri.api.domain.cc.CC.CCElevatorInfoVO;
import com.hdel.miri.api.domain.cc.CC.CalenderVO;

import com.hdel.miri.api.domain.hccc.HCCC.HCCCReceptionDetailVO;

import com.hdel.miri.api.domain.contract.Contract;
import com.hdel.miri.api.global.config.database.annotation.DS6Annotation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@DS6Annotation
@Mapper
public interface HCCCRepository {

    List<HCCC.HCCCHoStatusVO> selectELInfoJoinHOStatus(List<CC.ELInfoJoinToHCCC> list);
    List<HCCC.HCCCFailStatusVO> selectELInfoJoinFailStatus(HCCC.HCCCJoinWithMobile request);
    Long selectAllCount(List<Contract.ContractJoin> list);
    Long selectAllCount2(List<String> list);
    Long selectFailCount(List<Contract.ContractJoin> list);

    Double selectELRunAvg(HCCC.ContractJoin request);
    Double selectESRunAvg(HCCC.ContractJoin request);

    List<CalenderVO> getHcccReceptionInfoForMonth(CC.CCCurrentCalenderRequest request);
    HCCC.HCCCFailCountInfoVO selectFailCountInfo(Contract.ContractSearchPortfolioExtendsMonth request);

    HCCC.HCCCELESCountVO selectELRunCountInfo(HCCC.ContractJoin request);
    HCCC.HCCCELESCountVO selectESRunCountInfo(HCCC.ContractJoin request);

    List<String> getFailRemarks(HCCC.HCCCFailRemakrSearchVO request);

    // 월, 분기, 반기 가동율 for LV2 Dashboard
    HCCC.HCCCRunAvgInPeriod selectElRunAvgInPeriod(HCCC.ContractJoin request);

    // 엘리베이터 상태정보 가져오기
    List<CCElevatorInfoVO> selectElevatorList(List<CCElevatorInfoVO> request);

    // 고장 이력 상세 정보 가져오기
    List<HCCCReceptionDetailVO> getHcccReceptionInfoForOneTarget(HCCC.HCCCtargetReceptionVO targetAcptNo);
}
