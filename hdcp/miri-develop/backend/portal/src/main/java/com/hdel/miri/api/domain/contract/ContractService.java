package com.hdel.miri.api.domain.contract;

import com.hdel.miri.api.domain.cc.CCRepository;
import com.hdel.miri.api.domain.scrm.SCRMService;
import com.hdel.miri.api.domain.srm.SRMService;
import com.hdel.miri.api.util.ksign.KsignSPinCrypto;
import com.hdel.miri.api.util.request.AbstractRequest;
import com.hdel.miri.api.util.response.Message;
import com.hdel.miri.api.util.response.ResponseBody;
import com.hdel.miri.api.util.response.ResponseTemplate;
import com.hdel.miri.api.util.response.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.ListUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractService {
    private final Message message;
    private final SRMService srmService;
    private final SCRMService scrmService;
    private final ResponseTemplate responseTemplate;
    private final ContractRepository contractRepository;
    private final KsignSPinCrypto ksignSPinCrypto;
    private final CCRepository  ccRepository;


    public ResponseEntity anonymousContractSearch(Contract.ContractSearchAnonymous request){
        try {
            List<Contract.ContractAPI> unionList = null;
            unionList = ListUtils.union(srmService.anonymousContractSearch(request),scrmService.anonymousContractSearch(request));
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(unionList)
                    .build(),request.getUserLocale());
        } catch (Exception e){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    /**
     * Admin이 영업사원/점검기사 등록을 위한 담당 프로젝트번호 가져오는 Query
     * @param empId : 사번
     * @param roleType : 권한, ACCOUNT or ENGINEER
     * 
     * @return
     */
    public ResponseEntity employeeContractSearch(Contract.ContractSearchEmployee request){
        try {
            List<Contract.ContractEmployeeAPI> unionList = null;
            
            if("ACCOUNT".equals(request.getRoleType())) {
                unionList = ccRepository.employeeContractSearch(request);
            } else {
                // 주점검자 or 부점검자의 사번에 일치하는 List를 가져온다. 
                unionList = ListUtils.union(srmService.employeeContractSearch(request),scrmService.employeeContractSearch(request));
            }

            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(unionList)
                    .build(),request.getUserLocale());
        } catch (Exception e){
            log.error(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }



    public ResponseEntity currentManagerContractSearch(AbstractRequest request){
        try {
            List<String> list = contractRepository.select(request);
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(list)
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }
    public ResponseEntity currentContactList(Contract.ContractSearchPortfolio request){
        //  2023.05.18 고객요청으로 선택된 포트폴리오내 계약담당자만 조회
        // List<Contract.ContractJoin2> joinList = contractRepository.selectByCurrentUser(request);

        List<Contract.ContractJoin2> joinList = contractRepository.selectByPortfolio3(request);
        try {
            List<Contract.ContractContactInfo> contractInfoList = contractRepository.selectContractInfoList(request);
            // List<Contract.ContractContactInfo> unionList = null;
            // List<Contract.ContractContactInfo> srmList = srmService.contractJoinContactDistinct(joinList);
            // List<Contract.ContractContactInfo> scrmList = scrmService.contractJoinContactDistinct(joinList);

            // if(srmList != null && scrmList != null) {
            //     unionList = ListUtils.union(srmList, scrmList);
            // } else {
            //     if(srmList == null && scrmList == null) {
            //         unionList = new ArrayList();
            //     } else {
            //         if(srmList == null) {
            //             unionList = scrmList;
            //         } else {
            //             unionList = srmList;
            //         }
            //     }
            // }
            // unionList = ListUtils.union((srmList == null || srmList.isEmpty()) ? new ArrayList() : srmList, (scrmList == null || scrmList.isEmpty()) ? new ArrayList() : scrmList);

            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(contractInfoList)
                    .build(),request.getUserLocale());
        } catch (Exception e){
            log.info(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    // 내 엔지니어 가져오기
    public ResponseEntity currentContactEngineerList(Contract.ContractSearchPortfolio request){
        List<Contract.ContractJoin2> joinList = contractRepository.selectByPortfolio3(request);
        try {
            List<Contract.ContractAPI> unionList = null;
            unionList = ListUtils.union(srmService.contractJoinContactEngineerDistinct(joinList),scrmService.contractJoinContactEngineerDistinct(joinList));
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(unionList)
                    .build(),request.getUserLocale());
        } catch (Exception e){
            log.info(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity contractDetailsByCurrentUser(Contract.ContractSearchCurrentUserWithCondition request){
        request.setUserPortfolioMappingId("10");
        
        List <Contract.ContractDetail> resultList = contractRepository.selectByCurrentUserWithCondition(request);

        if(resultList != null && !resultList.isEmpty()) {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(resultList.get(0))
                    .build(),request.getUserLocale());
        } else {
            Contract.ContractDetail rtnVal =Contract.ContractDetail.builder().userPortfolioMappingId("10").contractList(new ArrayList()).build();
            return responseTemplate.withSuccess(ResponseBody.builder()
            .result(ResultCode.SUCCESS.getValue())
            .data(rtnVal)
            .build(),request.getUserLocale());
        }
    }
    public ResponseEntity contractDetailsByPortfolio(Contract.ContractSearchPortfolio request){
        try {
            List<Contract.ContractDetail> resultList = contractRepository.selectByPortfolio4(request);

            if(resultList != null && !resultList.isEmpty())
            {
                return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(resultList.get(0))
                    .build(),request.getUserLocale());
            }
            else
            {
                Contract.ContractDetail rtnVal = Contract.ContractDetail.builder().userPortfolioMappingId(request.getUserPortfolioMappingId()).contractList(new ArrayList()).build();
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(rtnVal)
                        .build(),request.getUserLocale());
            }
        } catch (Exception e){
            log.info(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }

    }

    public ResponseEntity contractDetailsByPortfolioSummary(Contract.ContractSearchPortfolio request){
        List<Contract.ContractJoin> joinList = contractRepository.selectByPortfolio(request);
        try {
            List<Contract.ContractAPI> unionList = null;
            unionList = ListUtils.union(scrmService.contractJoinSearchSummary(joinList),srmService.contractJoinSearchSummary(joinList));
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(unionList)
                    .build(),request.getUserLocale());
        } catch (Exception e){
            log.info(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }

    }

    public ResponseEntity currentCompanionDays(Contract.ContractSearchCurrentUser request){
        //List<Contract.ContractJoin2> joinList = contractRepository.selectByCurrentUser(request);
        try {

            // 기존 SCRM/SRM에서 가져오던 부분을 우리 DB CT_EL_INFO에서 가져오도록 수정
            // SCRM/SRM데이타를 일 단위로 동기화 하고 있는 DB임
            // int srmDays = srmService.getCompanionDaysDistinct(joinList);
            // int scrmDays = scrmService.getCompanionDaysDistinct(joinList);
            // int days = (srmDays < scrmDays? scrmDays : srmDays );

            int days = ccRepository.getCompanionDaysDistinct(request);

            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(days)
                    .build(),request.getUserLocale());
        } catch (Exception e){
            log.info(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity currentInsert(Contract.ContractInsert request){
        try {
            if(0 < contractRepository.insertDefault(request.getUserPortfolioMappingId(),request.getCurrentUser(), request.getLobby())) {
                return responseTemplate.withSuccess(ResponseBody.builder()
                .result(ResultCode.SUCCESS.getValue())
                .data(Boolean.TRUE)
                .build(),request.getUserLocale());
            } else {
                return responseTemplate.withSuccess(ResponseBody.builder()
                .result(ResultCode.FAILURE.getValue())
                .because(message.becauseNotDelete(request.getUserLocale()))
                .build(),request.getUserLocale());
            }
        } catch ( Exception e ){
            throw new RuntimeException(message.becauseFailureInsert(request.getUserLocale()));
        }
    }

    public ResponseEntity delete(Contract.ContractDelete request){
        try {
            if(0 <  contractRepository.delete(request)){
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(Boolean.TRUE)
                        .build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotDelete(request.getUserLocale()))
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            throw new RuntimeException(message.becauseFailureDelete(request.getUserLocale()));
        }
    }

    public ResponseEntity deleteFromLobby(Contract.ContractDelete request){
        try {
            if(0 <  contractRepository.deleteFromLobby(request)){
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(Boolean.TRUE)
                        .build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotDelete(request.getUserLocale()))
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            throw new RuntimeException(message.becauseFailureDelete(request.getUserLocale()));
        }
    }

    public ResponseEntity isAvailableMiricall(Contract.ContractMiriServiceAPI request){
        try {
            Contract.ContractMIRIStatus _rst = contractRepository.isAvailableMiricall(request);

            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(_rst)
                    .build(),request.getUserLocale());
        } catch (Exception e){
            log.info(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity isAvailableMiriview(Contract.ContractMiriServiceAPI request){
        try {
            Contract.ContractMIRIStatus _rst = contractRepository.isAvailableMiriview(request);

            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(_rst)
                    .build(),request.getUserLocale());
        } catch (Exception e){
            log.info(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity isAvailableMiricall2(Contract.ContractMiriServiceAPI2 request){
        try {
            Contract.ContractMIRIStatus _rst = contractRepository.isAvailableMiricall2(request);

            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(_rst)
                    .build(),request.getUserLocale());
        } catch (Exception e){
            log.info(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity isAvailableMiriview2(Contract.ContractMiriServiceAPI2 request){
        try {
            Contract.ContractMIRIStatus _rst = contractRepository.isAvailableMiriview2(request);

            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(_rst)
                    .build(),request.getUserLocale());
        } catch (Exception e){
            log.info(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity NewContractInsert(Contract.NewContractInsert request){
        try {
            if(0 < contractRepository.NewContractInsert(request)){
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(Boolean.TRUE)
                        .build(),request.getUserLocale());
            } else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotInsert(request.getUserLocale()))
                    .build(),request.getUserLocale());
        } catch (Exception e) {
            log.info(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureInsert(request.getUserLocale()));
        }
    }

    public ResponseEntity copyFromContractTo(Contract.ContractCopyListVO request){
        try {
            Contract.ContractCopyVO requestVO = new Contract.ContractCopyVO();
            requestVO.setFromUserId(request.getFromUserId());
            requestVO.setCurrentUserId(request.getCurrentUser());

            List<String> toUserList = request.getToUserList();

            for(String toUserId : toUserList)
            {
                requestVO.setToUserId(toUserId);

                contractRepository.insertContractCopyLogMaster(requestVO);
                contractRepository.insertContractCopyLogDetail(requestVO);
                contractRepository.deleteClearToLobby(requestVO);
                contractRepository.insertContractCopy(requestVO);
                contractRepository.deletePortfolioNotInLobby(requestVO);
            }

            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(Boolean.TRUE)
                    .build(),request.getUserLocale());
        } catch (Exception e) {
            log.info(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureInsert(request.getUserLocale()));
        }
    }
}
