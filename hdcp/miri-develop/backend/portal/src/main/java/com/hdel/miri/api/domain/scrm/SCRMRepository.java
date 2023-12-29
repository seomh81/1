package com.hdel.miri.api.domain.scrm;

import com.hdel.miri.api.domain.contract.Contract;
import com.hdel.miri.api.domain.storage.Storage;
import com.hdel.miri.api.domain.mms.mmsVO;
import com.hdel.miri.api.global.config.database.annotation.DS3Annotation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@DS3Annotation
@Mapper
public interface SCRMRepository {
    List<Contract.ContractAPI> selectAnonymous(Contract.ContractSearchAnonymous request);
    List<Contract.ContractEmployeeAPI> selectEmployee(Contract.ContractSearchEmployee request);

    Contract.ContractJoinDetail selectByContract(Contract.ContractJoin request);
    List<Contract.ContractDetail> GetContractFromPrjNos(Contract.ContractSearchPortfolio request);
    
    Contract.ContractJoinDetailPRJNOInfo selectByContractDistinct(Contract.ContractJoinDistinct request);
    Contract.ContractJoinSummary selectByContractSummary(Contract.ContractJoin request);
    List<Contract.ContractContactInfo> selectContactByContract(List<Contract.ContractJoin> request);
    List<Contract.ContractContactInfo> selectContactByContractDistinct(List<Contract.ContractJoin2> request);
    List<Contract.ContractContactInfo> selectContactByContractEngineer(List<Contract.ContractJoin> request);
    List<Contract.ContractContactInfo> selectContactByContractEngineerDistinct(List<Contract.ContractJoin2> request);
    List<Storage.CAFileInfo> selectCAFiles(String key);
    Integer selectByCompanionDays(List<Contract.ContractJoin> list);
    Integer selectByCompanionDaysDistinct(List<Contract.ContractJoinDistinct> list);

    // 구매이력
    List<SCRM.SCRMUnitPurchaseMSTResponse> selectUnitPurchaseMST(SCRM.SCRMCurrentPortfolioRequest request);
    List<SCRM.SCRMCurrentWBSNoResponse> selectUnitPurchaseDetail(SCRM.SCRMCurrentWBSNoRequest request);

    //문자 전송
    Integer insertSendMMS(mmsVO.request request);
}
