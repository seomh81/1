package com.hdel.miri.api.domain.portfolio;

import com.hdel.miri.api.util.response.Message;
import com.hdel.miri.api.util.response.ResponseBody;
import com.hdel.miri.api.util.response.ResponseTemplate;
import com.hdel.miri.api.domain.contract.ContractRepository;
import com.hdel.miri.api.util.response.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final Message message;
    private final ResponseTemplate responseTemplate;
    private final PortfolioRepository portfolioRepository;
    private final ContractRepository contractRepository;


    public ResponseEntity getListByUser(Portfolio.PortfolioSearch request) {
        return responseTemplate.withSuccess(ResponseBody.builder()
                .result(ResultCode.SUCCESS.getValue())
                .data(portfolioRepository.selectListByUser(request))
                .build(),request.getUserLocale());
    }

    @Transactional("db2TxManager")
    public ResponseEntity insert(Portfolio.PortfolioCreate request){
        try {
            if(0 < portfolioRepository.insert(request)){
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(Boolean.TRUE)
                        .build(),request.getUserLocale());
            } else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotInsert(request.getUserLocale()))
                    .build(),request.getUserLocale());
        } catch (Exception e) {
            throw new RuntimeException(message.becauseFailureInsert(request.getUserLocale()));
        }
    }

    @Transactional("db2TxManager")
    public ResponseEntity update(Portfolio.PortfolioUpdate request){
        try {
            if(0 < portfolioRepository.update(request)){
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(Boolean.TRUE)
                        .build(),request.getUserLocale());
            } else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotUpdate(request.getUserLocale()))
                    .build(),request.getUserLocale());
        } catch (Exception e) {
            throw new RuntimeException(message.becauseFailureUpdate(request.getUserLocale()));
        }
    }

    @Transactional("db2TxManager")
    public ResponseEntity updateDefault(Portfolio.PortfolioSelection request){
        try {
            portfolioRepository.updateNotDefault(request);
            portfolioRepository.updateDefault(request);
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(Boolean.TRUE)
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            throw new RuntimeException(message.becauseFailureUpdate(request.getUserLocale()));
        }

    }

    @Transactional("db2TxManager")
    public ResponseEntity delete(Portfolio.PortfolioDelete request){
        try {
            contractRepository.deleteByPortfolio(request);
            portfolioRepository.delete(request);
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(Boolean.TRUE)
                    .build(),request.getUserLocale());
        } catch (Exception e) {
            throw new RuntimeException(message.becauseFailureDelete(request.getUserLocale()));
        }
    }
}
