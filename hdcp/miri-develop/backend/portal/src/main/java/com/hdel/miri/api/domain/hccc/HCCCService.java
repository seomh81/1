package com.hdel.miri.api.domain.hccc;

import com.hdel.miri.api.domain.cc.CC;
import com.hdel.miri.api.domain.cc.CCRepository;
import com.hdel.miri.api.domain.contract.Contract;
import com.hdel.miri.api.domain.contract.ContractRepository;
import com.hdel.miri.api.domain.hccc.HCCC.HCCCRunAvgInPeriod;
import com.hdel.miri.api.util.response.Message;
import com.hdel.miri.api.util.response.ResponseBody;
import com.hdel.miri.api.util.response.ResponseTemplate;
import com.hdel.miri.api.util.response.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HCCCService {
    private final Message message;
    private final ResponseTemplate responseTemplate;
    private final HCCCRepository hcccRepository;
    private final CCRepository ccRepository;
    private final ContractRepository contractRepository;

    private String getS(int month){
        return String.valueOf((month < 10 ? "0".concat(String.valueOf(month)) : month));
    }

    public ResponseEntity getELInfoStatus(CC.CCCurrentPortfolioRequest request){
        try {
            // el_info에서 해당 elevator를 가져온다. 
            List<CC.ELInfoJoinToHCCC> joinList = ccRepository.selectPortfolioELInfoJoinToHCCC(request);
            // DB에서 상태를 Join해서 가져온다. 
            List<HCCC.HCCCHoStatusVO> statusList = hcccRepository.selectELInfoJoinHOStatus(joinList);
            for(int i=0,len=joinList.size(); i < len ; i++){
                CC.ELInfoJoinToHCCC one = joinList.get(i);
                one.setElStatus(statusList.get(i).getElStatus());
                one.setActivityStatus(statusList.get(i).getActivityStatus());
                one.setAcptDate(statusList.get(i).getAcptDate());
            }
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(joinList)
                    .build(), request.getUserLocale());
        } catch ( Exception e ){
            log.error(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity getFailStatus(CC.CCCurrentPortfolioRequest request){
        HCCC.HCCCJoinWithMobile join = new HCCC.HCCCJoinWithMobile();
        join.setMobile(request.getMobile());
        try {
            join.setList(ccRepository.selectPortfolioELInfoJoinToHCCC(request));
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(hcccRepository.selectELInfoJoinFailStatus(join))
                    .build(), request.getUserLocale());
        } catch ( Exception e ){
            log.error(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity getCurrentCountInfo(Contract.ContractSearchPortfolio request){
        List<Contract.ContractJoin> joinList = contractRepository.selectByPortfolio(request);
        try {
            if(joinList == null && joinList.size() < 1) {
                return responseTemplate.withSuccess(ResponseBody.builder()
                .result(ResultCode.SUCCESS.getValue())
                .data(HCCC.HCCCCountVO.builder()
                        .allCount(0L)
                        .failCount(0L)
                        .build())
                .build()
                , request.getUserLocale());
            } else {
                return responseTemplate.withSuccess(ResponseBody.builder()
                .result(ResultCode.SUCCESS.getValue())
                .data(HCCC.HCCCCountVO.builder()
                        .allCount(hcccRepository.selectAllCount(joinList))
                        .failCount(hcccRepository.selectFailCount(joinList))
                        .build())
                .build()
                , request.getUserLocale());
            }
        }catch (Exception e){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity getRunAvg(Contract.ContractSearchPortfolio request){
        HCCC.ContractJoin hcccJoin = new HCCC.ContractJoin();

        LocalDateTime currentDate = LocalDateTime.now();
        currentDate = currentDate.minusMonths(1);

        try {
            if(hcccJoin == null && hcccJoin.getList().size() < 1) {
                return responseTemplate.withSuccess(ResponseBody.builder()
                .result(ResultCode.SUCCESS.getValue())
                .data(HCCC.HCCCRunAvgVO.builder()
                        .elAvg(0.0)
                        .esAvg(0.0)
                        .build())
                .build()
                , request.getUserLocale());
            } else {
                hcccJoin.setPrevMonth(getS(currentDate.getYear()).concat(getS(currentDate.getMonth().getValue())));
                hcccJoin.setList(contractRepository.selectByPortfolio(request));
    
                return responseTemplate.withSuccess(ResponseBody.builder()
                                .result(ResultCode.SUCCESS.getValue())
                                .data(HCCC.HCCCRunAvgVO.builder()
                                        .elAvg(hcccRepository.selectELRunAvg(hcccJoin))
                                        .esAvg(hcccRepository.selectESRunAvg(hcccJoin))
                                        .build())
                                .build()
                        , request.getUserLocale());
            }
        } catch ( Exception e ){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity selectElRunAvgInPeriod(Contract.ContractSearchPortfolioId request){

        // 포트폴리오 ID로 포트폴리오내 통합프로젝터 코드 + 거래선 코드를 가져온다. 
        HCCC.ContractJoin hcccJoin = new HCCC.ContractJoin();
        try {
            List<Contract.ContractJoin> _selectedVO = contractRepository.selectByPortfolio2(request);

            if(_selectedVO == null || _selectedVO.isEmpty()) {
                return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(HCCC.HCCCRunAvgInPeriod.builder()
                            .month("0.0")
                            .quarter("0.0")
                            .halfyear("0.0")
                            .build())
                    .build()
                    , request.getUserLocale());
            } else {
                try {
                    hcccJoin.setList(_selectedVO);

                    HCCC.HCCCRunAvgInPeriod _result = hcccRepository.selectElRunAvgInPeriod(hcccJoin);
                    if(_result == null) {
                        return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(HCCC.HCCCRunAvgInPeriod.builder()
                                .month("0.0")
                                .quarter("0.0")
                                .halfyear("0.0")
                                .build())
                        .build()
                        , request.getUserLocale());
                    } else {
                        return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(HCCC.HCCCRunAvgInPeriod.builder()
                                .month(_result.getMonth())
                                .quarter(_result.getQuarter())
                                .halfyear(_result.getHalfyear())
                                .build())
                        .build()
                        , request.getUserLocale());
                    }
                }catch(Exception ex) {
                    throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
                }
            } 
            } catch(Exception ex) {
                throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity getRunCountInfo(Contract.ContractSearchPortfolio request){
        HCCC.ContractJoin hcccJoin = new HCCC.ContractJoin();
        try {
            hcccJoin.setList(contractRepository.selectByPortfolio(request));
            return responseTemplate.withSuccess(ResponseBody.builder()
                            .result(ResultCode.SUCCESS.getValue())
                            .data(HCCC.HCCCELESCountVOResponse.builder()
                                    .el(hcccRepository.selectELRunCountInfo(hcccJoin))
                                    .es(hcccRepository.selectESRunCountInfo(hcccJoin))
                                    .build())
                            .build()
                    , request.getUserLocale());
        } catch ( Exception e ){
            log.error(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity getFailCountInfo(Contract.ContractSearchPortfolioExtendsMonth request){
        request.setList(contractRepository.selectByPortfolio(request));
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                            .result(ResultCode.SUCCESS.getValue())
                            .data(hcccRepository.selectFailCountInfo(request))
                            .build()
                    , request.getUserLocale());
        } catch ( Exception e ){
            log.error(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity getFailReamrks(HCCC.HCCCFailRemakrSearchVO request){
        List<String> orStr    = ccRepository.getFailRemakrOr();
        List<String> notStr   = ccRepository.getFailRemakrNot();

        request.setOrStrs(orStr);
        request.setNotStrs(notStr);

        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                            .result(ResultCode.SUCCESS.getValue())
                            .data(hcccRepository.getFailRemarks(request))
                            .build()
                    , request.getUserLocale());
        } catch ( Exception e ){
            log.error(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    public ResponseEntity getTargetReception(HCCC.HCCCtargetReceptionVO request){
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                            .result(ResultCode.SUCCESS.getValue())
                            .data(hcccRepository.getHcccReceptionInfoForOneTarget(request))
                            .build()
                    , request.getUserLocale());
        } catch ( Exception e ){
            log.error(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }
}
