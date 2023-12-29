package com.hdel.miri.api.domain.scrm;

import com.hdel.miri.api.domain.cc.CCRepository;
import com.hdel.miri.api.domain.contract.Contract;
import com.hdel.miri.api.util.response.Message;
import com.hdel.miri.api.util.response.ResponseBody;
import com.hdel.miri.api.util.response.ResponseTemplate;
import com.hdel.miri.api.util.response.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.ListUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SCRMService {

    private final Message message;
    private final ResponseTemplate responseTemplate;
    private final CCRepository ccRepository;
    private final SCRMRepository scrmRepository;
    @Transactional(value = "db3TxManager",propagation = Propagation.NESTED)
    public List<Contract.ContractAPI> anonymousContractSearch(Contract.ContractSearchAnonymous request){
        return scrmRepository.selectAnonymous(request);
    }

    @Transactional(value = "db3TxManager",propagation = Propagation.NESTED)
    public List<Contract.ContractEmployeeAPI> employeeContractSearch(Contract.ContractSearchEmployee request){
        return scrmRepository.selectEmployee(request);
    }
    @Transactional(value = "db3TxManager",propagation = Propagation.NESTED)
    public List<Contract.ContractJoinSummary> contractJoinSearchSummary(List<Contract.ContractJoin> request){
        List<Contract.ContractJoinSummary> temp = new ArrayList<>();
        request.stream().forEach(contractJoin -> {
            Contract.ContractJoinSummary selectOne = scrmRepository.selectByContractSummary(contractJoin);
            if(selectOne != null) temp.add(selectOne);
        });
        return temp;
    }
    @Transactional(value = "db3TxManager",propagation = Propagation.NESTED)
    public List<Contract.ContractJoinDetail> contractJoinSearch(List<Contract.ContractJoin> request){
        List<Contract.ContractJoinDetail> temp = new ArrayList<>();
        request.stream().forEach(contractJoin -> {
            Contract.ContractJoinDetail selectOne = scrmRepository.selectByContract(contractJoin);
            if(selectOne != null) temp.add(selectOne);
        });
        return temp;
    }

    @Transactional(value = "db3TxManager",propagation = Propagation.NESTED)
    public List<Contract.ContractDetail> contractJoinSearchExt(Contract.ContractSearchPortfolio request){
        List<Contract.ContractDetail> temp = new ArrayList<>();
        if(request.getJoinList() != null && !request.getJoinList().isEmpty()) {
            temp = scrmRepository.GetContractFromPrjNos(request);
        } 

        return temp;
    }

    @Transactional(value = "db3TxManager",propagation = Propagation.NESTED)
    public List<Contract.ContractJoinDetailPRJNOInfo> contractJoinSearchDistinct(List<Contract.ContractJoinDistinct> request){
        List<Contract.ContractJoinDetailPRJNOInfo> temp = new ArrayList<>();
        request.stream().forEach(contractJoin -> {
            Contract.ContractJoinDetailPRJNOInfo selectOne = scrmRepository.selectByContractDistinct(contractJoin);
            if(selectOne != null) temp.add(selectOne);
        });
        return temp; 
    }

    @Transactional(value = "db3TxManager",propagation = Propagation.NESTED)
    public List<Contract.ContractContactInfo> contractJoinContact(List<Contract.ContractJoin> request){
        return scrmRepository.selectContactByContract(request);
    }
    @Transactional(value = "db3TxManager",propagation = Propagation.NESTED)
    public List<Contract.ContractContactInfo> contractJoinContactDistinct(List<Contract.ContractJoin2> request){
        return scrmRepository.selectContactByContractDistinct(request);
    }
    @Transactional(value = "db3TxManager",propagation = Propagation.NESTED)
    public List<Contract.ContractContactInfo> contractJoinContactEngineer(List<Contract.ContractJoin> request){
        return scrmRepository.selectContactByContractEngineer(request);
    }
    @Transactional(value = "db3TxManager",propagation = Propagation.NESTED)
    public List<Contract.ContractContactInfo> contractJoinContactEngineerDistinct(List<Contract.ContractJoin2> request){
        return scrmRepository.selectContactByContractEngineerDistinct(request);
    }
    @Transactional(value = "db3TxManager",propagation = Propagation.NESTED)
    public int getCompanionDays(List<Contract.ContractJoin> request){
        Integer days = scrmRepository.selectByCompanionDays(request);
        return days.intValue();
    }
    @Transactional(value = "db3TxManager",propagation = Propagation.NESTED)
    public int getCompanionDaysDistinct(List<Contract.ContractJoinDistinct> request){
        Integer days = scrmRepository.selectByCompanionDaysDistinct(request);
        return days.intValue();
    }
}
