package com.hdel.miri.api.domain.srm;

import com.hdel.miri.api.domain.contract.Contract;
import com.hdel.miri.api.domain.scrm.SCRM;
import com.hdel.miri.api.global.config.database.annotation.DS4Annotation;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@DS4Annotation
@Mapper
public interface SRMRepository {
    List<Contract.ContractAPI> selectAnonymous(Contract.ContractSearchAnonymous request);
    List<Contract.ContractEmployeeAPI> selectEmployee(Contract.ContractSearchEmployee request);
    
    Contract.ContractJoinDetail selectByContract(Contract.ContractJoin request);
    List<Contract.ContractDetail> GetContractFromPrjNos(Contract.ContractSearchPortfolio request);


    Contract.ContractJoinDetailPRJNOInfo selectByContractDistinct(Contract.ContractJoinDistinct request);
    Contract.ContractJoinSummary selectByContractSummary(Contract.ContractJoin request);
    List<Contract.ContractContactInfo> selectContactByContract(List<Contract.ContractJoin> request);
    List<Contract.ContractContactInfo> selectContactByContractDistinct(List<Contract.ContractJoinDistinct> request);
    List<Contract.ContractContactInfo> selectContactByContractDistinct2(List<Contract.ContractJoin2> request);
    List<Contract.ContractContactInfo> selectContactByContractEngineer(List<Contract.ContractJoin> request);
    List<Contract.ContractContactInfo> selectContactByContractEngineerDistinct(List<Contract.ContractJoin2> request);
    Integer selectByCompanionDays(List<Contract.ContractJoin> list);
    Integer selectByCompanionDaysDistinct(List<Contract.ContractJoinDistinct> list);

    // 구매이력
    List<SCRM.SCRMUnitPurchaseMSTResponse> selectUnitPurchaseMST(SCRM.SCRMCurrentPortfolioRequest request);
    List<SCRM.SCRMCurrentWBSNoResponse> selectUnitPurchaseDetail(SCRM.SCRMCurrentWBSNoRequest request);
    
}
