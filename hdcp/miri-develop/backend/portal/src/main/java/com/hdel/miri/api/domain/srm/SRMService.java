package com.hdel.miri.api.domain.srm;

import com.hdel.miri.api.domain.contract.Contract;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.ListUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SRMService {

    private final SRMRepository srmRepository;

    @Transactional(value = "db4TxManager",propagation = Propagation.NESTED)
    public List<Contract.ContractAPI> anonymousContractSearch(Contract.ContractSearchAnonymous request){
        return srmRepository.selectAnonymous(request);
    }

    @Transactional(value = "db4TxManager",propagation = Propagation.NESTED)
    public List<Contract.ContractEmployeeAPI> employeeContractSearch(Contract.ContractSearchEmployee request){
        return srmRepository.selectEmployee(request);
    }

    @Transactional(value = "db4TxManager",propagation = Propagation.NESTED)
    public List<Contract.ContractJoinSummary> contractJoinSearchSummary(List<Contract.ContractJoin> request){
        List<Contract.ContractJoinSummary> temp = new ArrayList<>();
        request.stream().forEach(contractJoin -> {
            Contract.ContractJoinSummary selectOne = srmRepository.selectByContractSummary(contractJoin);
            if(selectOne != null) temp.add(selectOne);
        });
        return temp;
    }
    @Transactional(value = "db4TxManager",propagation = Propagation.NESTED)
    public List<Contract.ContractJoinDetail> contractJoinSearch(List<Contract.ContractJoin> request){
        List<Contract.ContractJoinDetail> temp = new ArrayList<>();
        request.stream().forEach(contractJoin -> {
            Contract.ContractJoinDetail selectOne = srmRepository.selectByContract(contractJoin);
            if(selectOne != null) temp.add(selectOne);
        });
        return temp;
    }
    @Transactional(value = "db4TxManager")
    public List<Contract.ContractDetail> contractJoinSearchExt(Contract.ContractSearchPortfolio request){
        List<Contract.ContractDetail> temp = new ArrayList<>();

        if(request.getJoinList() != null && !request.getJoinList().isEmpty()) {
            temp = srmRepository.GetContractFromPrjNos(request);
        } 

        return temp;


        // request.getJoinList().stream().forEach(contractJoin -> {
        //     Contract.ContractJoinDetail selectOne = srmRepository.selectByContract(contractJoin);
        //     if(selectOne != null) {
        //         String keyword = request.getKeyword();
        //         if(keyword!=null && !keyword.equals("")){
        //             String contractCode = selectOne.getContractCode();
        //             List<String> projNoList = selectOne.getCarList().stream().map(contractCarInfo -> {
        //                 return selectOne.getPrjNo().concat(contractCarInfo.getCarNo());
        //             }).collect(Collectors.toList());
        //             if(contractCode.equals(keyword)){
        //                 temp.add(selectOne);
        //             }else{
        //                 List<Contract.ContractCarInfo> tempList = new ArrayList<>();
        //                 for(int i=0,len =projNoList.size();i<len;i++){
        //                     String projNo = projNoList.get(i);
        //                     if(projNo.equals(keyword)){
        //                         tempList.add(selectOne.getCarList().get(i));
        //                     }
        //                 }
        //                 if(0 < tempList.size()){
        //                     selectOne.setCarList(tempList);
        //                     temp.add(selectOne);
        //                 }
        //             }
        //         }else temp.add(selectOne);
        //     };
        // });
        // return temp;
    }

    @Transactional(value = "db4TxManager",propagation = Propagation.NESTED)
    public List<Contract.ContractJoinDetailPRJNOInfo> contractJoinSearchDistinct(List<Contract.ContractJoinDistinct> request){
        List<Contract.ContractJoinDetailPRJNOInfo> temp = new ArrayList<>();
        request.stream().forEach(contractJoin -> {
            Contract.ContractJoinDetailPRJNOInfo selectOne = srmRepository.selectByContractDistinct(contractJoin);
            if(selectOne != null) temp.add(selectOne);
        });
        return temp;
    }

    @Transactional(value = "db4TxManager",propagation = Propagation.NESTED)
    public List<Contract.ContractContactInfo> contractJoinContact(List<Contract.ContractJoin> request){
        return srmRepository.selectContactByContract(request);
    }
    @Transactional(value = "db4TxManager",propagation = Propagation.NESTED)
    public List<Contract.ContractContactInfo> contractJoinContactDistinct(List<Contract.ContractJoin2> request){

        return srmRepository.selectContactByContractDistinct2(request);
    }

    @Transactional(value = "db4TxManager",propagation = Propagation.NESTED)
    public List<Contract.ContractContactInfo> contractJoinContactEngineer(List<Contract.ContractJoin> request){
        return srmRepository.selectContactByContractEngineer(request);
    }
    @Transactional(value = "db4TxManager",propagation = Propagation.NESTED)
    public List<Contract.ContractContactInfo> contractJoinContactEngineerDistinct(List<Contract.ContractJoin2> request){
        return srmRepository.selectContactByContractEngineerDistinct(request);
    }
    @Transactional(value = "db4TxManager",propagation = Propagation.NESTED)
    public int getCompanionDays(List<Contract.ContractJoin> request){
        Integer days = srmRepository.selectByCompanionDays(request);
        return days.intValue();
    }
    @Transactional(value = "db4TxManager",propagation = Propagation.NESTED)
    public int getCompanionDaysDistinct(List<Contract.ContractJoinDistinct> request){
        Integer days = srmRepository.selectByCompanionDaysDistinct(request);
        return days.intValue();
    }
}
