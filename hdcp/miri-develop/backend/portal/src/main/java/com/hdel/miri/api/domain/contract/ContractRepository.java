package com.hdel.miri.api.domain.contract;

import com.hdel.miri.api.domain.portfolio.Portfolio;
import com.hdel.miri.api.domain.user.User;
import com.hdel.miri.api.global.config.database.annotation.DS2Annotation;
import com.hdel.miri.api.util.request.AbstractRequest;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;

@DS2Annotation
@Mapper
public interface ContractRepository {
    List<String> select(AbstractRequest request);
    List<Contract.ContractJoin2> selectByCurrentUser(Contract.ContractSearchCurrentUser request);
    List<Contract.ContractDetail> selectByCurrentUserWithCondition(Contract.ContractSearchCurrentUserWithCondition request);
    List<Contract.ContractJoin> selectByPortfolio(Contract.ContractSearchPortfolio request);
    List<Contract.ContractJoin> selectByPortfolio2(Contract.ContractSearchPortfolioId request);
    List<Contract.ContractJoin2> selectByPortfolio3(Contract.ContractSearchPortfolio request);
    List<Contract.ContractDetail> selectByPortfolio4(Contract.ContractSearchPortfolio request);
    List<Contract.ContractContactInfo> selectContractInfoList(Contract.ContractSearchPortfolio request);
    
    int insertDefault(Contract.ContractDefaultCreate request);
    // int insertDefault(BigDecimal userPortfolioMappingId,  String currentUserId, List<Contract.ContractAPI> contractList);
    // by GimaPei
    int insertDefault(BigDecimal userPortfolioMappingId,  String currentUserId, List<Contract.ContractAPI> contractList);

    // 관리자가 입주자를 대량으로 등록할때
    int insertUserFromManager(BigDecimal userPortfolioMappingId, String currentUserId, User.DefaultOtherUserCreate request);

    // 시스템관리자가 영업사원을 대량으로 등록할때
    int insertAccountFromSystem(BigDecimal userPortfolioMappingId, String currentUserId, User.DefaultOtherUserCreate request);
    // 영업사원 portfolio lobby 사용을 막기위한 포트폴리오 default에 로비에서 계약 한건 맵핑
    int insertAccountFromLobby(BigDecimal userPortfolioMappingId, String currentUserId, User.DefaultOtherUserCreate request);

    // 시스템관리자가 서비스사원을 대량으로 등록할때
    int insertEnginerrFromSystem(BigDecimal userPortfolioMappingId, String currentUserId, List<Contract.ContractEmployeeAPI> request);

    int insertContractCopy(Contract.ContractCopyVO request);
    int insertContractCopyLogMaster(Contract.ContractCopyVO request);
    int insertContractCopyLogDetail(Contract.ContractCopyVO request);


    int delete(Contract.ContractDelete request);
    int deleteFromLobby(Contract.ContractDelete request);
    int deleteByPortfolio(Portfolio.PortfolioDelete request);
    int deleteClearToLobby(Contract.ContractCopyVO request);
    int deletePortfolioNotInLobby(Contract.ContractCopyVO request);

    Contract.ContractMIRIStatus isAvailableMiricall(Contract.ContractMiriServiceAPI request);
    Contract.ContractMIRIStatus isAvailableMiriview(Contract.ContractMiriServiceAPI request);

    Contract.ContractMIRIStatus isAvailableMiricall2(Contract.ContractMiriServiceAPI2 request);
    Contract.ContractMIRIStatus isAvailableMiriview2(Contract.ContractMiriServiceAPI2 request);

    int NewContractInsert(Contract.NewContractInsert request);
}
