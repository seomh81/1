package com.hdel.miri.api.domain.masterdata;

import com.hdel.miri.api.util.response.Message;
import com.hdel.miri.api.util.response.ResponseBody;
import com.hdel.miri.api.util.response.ResponseTemplate;
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
public class MasterDataService {

    private final Message message;
    private final MasterDataRepository masterDataRepository;
    private final ResponseTemplate responseTemplate;

    public ResponseEntity getDetails(MasterData.MasterDataSelect request){
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(masterDataRepository.selectDetails(request))
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    /**
     * 마스터 데이터 리스트를 가져옵니다.
     *
     * @param request MasterData$MasterDataHeadSearch
     * @return MasterData$MasterDataHeadVO
     */
    public ResponseEntity getMasterHeadList(MasterData.MasterDataHeadSearch request) {
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(masterDataRepository.selectMasterHeadList(request))
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }
    }

    /**
     * 마스터 데이터 항목을 가져옵니다.
     *
     * @param request MasterData$MasterDataDetailSearch
     * @return MasterData$MasterDataDetailVO
     */
    public ResponseEntity getMasterDetailList(MasterData.MasterDataDetailSearch request) {
        try {
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(masterDataRepository.selectMasterDetailList(request))
                    .build(),request.getUserLocale());
        }catch (Exception e){
            log.info(e.getMessage(),e);
            throw new RuntimeException(message.becauseFailureSelect(request.getUserLocale()));
        }

    }

    /**
     * 마스터 데이터 리스트를 추가합니다.
     *
     * @param request MasterData$MasterDataHeadCreate
     * @return String
     */
    public ResponseEntity<?> createMasterHead(MasterData.MasterDataHeadCreate request) {
        try {
            if(0 < masterDataRepository.insertMasterHead(request)){
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(message.withSuccess(request.getUserLocale()))
                        .build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotUpdate(request.getUserLocale()))
                    .build(),request.getUserLocale());
        }catch (Exception e){
            
            throw new RuntimeException(message.becauseFailureUpdate(request.getUserLocale()));
        }

    }

    /**
     * 마스터 데이터 항목을 추가 합니다.
     *
     * @param request MasterData$MasterDataDetailCreate
     * @return String
     */
    public ResponseEntity createMasterDetail(MasterData.MasterDataDetailCreate request) {
        try {
            if(0 < masterDataRepository.insertMasterDetail(request)){
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(message.withSuccess(request.getUserLocale()))
                        .build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotUpdate(request.getUserLocale()))
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            
            throw new RuntimeException(message.becauseFailureUpdate(request.getUserLocale()));
        }
    }

    /**
     * 마스터 데이터 항목을 추가 합니다.
     *
     * @param request MasterData$MasterDataDetailCreate
     * @return String
     */
    @Transactional(value = "db2TxManager")
    public ResponseEntity createMasterDetailImport(MasterData.MasterDataDetailImport request) {
        try {
            if(0 < masterDataRepository.insertMasterDetailImport(request)){
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(message.withSuccess(request.getUserLocale()))
                        .build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotUpdate(request.getUserLocale()))
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            
            throw new RuntimeException(message.becauseFailureUpdate(request.getUserLocale()));
        }
    }
    

    /**
     * 마스터 데이터 리스트를 삭제합니다.
     *
     * @param request MasterData$MasterDataHeadDelete
     * @return String
     */
    @Transactional(value = "db2TxManager")
    public ResponseEntity deleteMasterHead(MasterData.MasterDataHeadDelete request) {
        try {
            masterDataRepository.deleteMasterHeads(request);
            masterDataRepository.deleteMasterDetailByMasterIds(request);
            return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.SUCCESS.getValue())
                    .data(message.withSuccess(request.getUserLocale()))
                    .build(),request.getUserLocale());
        } catch (Exception e){
            
            throw new RuntimeException(message.becauseFailureDelete(request.getUserLocale()));
        }
    }

    /**
     * 마스터 데이터 항목을 삭제합니다.
     *
     * @param request MasterData$MasterDataDetailDelete
     * @return String
     */
    public ResponseEntity<?> deleteMasterDetail(MasterData.MasterDataDetailDelete request) {
        try {
            if(0 < masterDataRepository.deleteMasterDetails(request)){
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(message.withSuccess(request.getUserLocale()))
                        .build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotDelete(request.getUserLocale()))
                    .build(),request.getUserLocale());

        } catch (Exception e){
            throw new RuntimeException(message.becauseFailureDelete(request.getUserLocale()));
        }
    }

    /**
     * 마스터 데이터 리스트를 수정합니다.
     *
     * @param request MasterData$MasterDataHeadUpdate
     * @return String
     */
    public ResponseEntity<?> updateMasterHead(MasterData.MasterDataHeadUpdate request) {
        try {
            if(0 < masterDataRepository.updateMasterHead(request)){
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(message.withSuccess(request.getUserLocale()))
                        .build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotUpdate(request.getUserLocale()))
                    .build(),request.getUserLocale());
        } catch ( Exception e ){
            
            throw new RuntimeException(message.becauseFailureUpdate(request.getUserLocale()));
        }

    }

    /**
     * 마스터 데이터 항목을 수정합니다.
     *
     * @param request MasterData$MasterDataDetailUpdate
     * @return String
     */
    public ResponseEntity<?> updateMasterDetail(MasterData.MasterDataDetailUpdate request) {
        try {
            if(0 < masterDataRepository.updateMasterDetail(request)){
                return responseTemplate.withSuccess(ResponseBody.builder()
                        .result(ResultCode.SUCCESS.getValue())
                        .data(message.withSuccess(request.getUserLocale()))
                        .build(),request.getUserLocale());
            }else return responseTemplate.withSuccess(ResponseBody.builder()
                    .result(ResultCode.FAILURE.getValue())
                    .because(message.becauseNotUpdate(request.getUserLocale()))
                    .build(),request.getUserLocale());
        } catch (Exception e) {
            
            throw new RuntimeException(message.becauseFailureUpdate(request.getUserLocale()));
        }
    }
}
