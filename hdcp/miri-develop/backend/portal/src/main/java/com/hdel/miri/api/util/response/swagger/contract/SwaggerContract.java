package com.hdel.miri.api.util.response.swagger.contract;

import com.hdel.miri.api.domain.contract.Contract;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import lombok.Getter;

import java.util.List;

public class SwaggerContract {
    @Getter
    public static class GetContractAPIResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<Contract.ContractAPI>> {
    }
    @Getter
    public static class GetContractEmployeeAPIResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<Contract.ContractEmployeeAPI>> {
    }
    @Getter
    public static class GetContractMIRIStatusResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<Contract.ContractMIRIStatus>> {
    }
    @Getter
    public static class GetContractListResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<String>> {
    }
    @Getter
    public static class ContractIntegerResponse extends SwaggerResponse.SwaggerSuccessCapsule<Integer> {
    }
    @Getter
    public static class ContractBoolResponse extends SwaggerResponse.SwaggerSuccessCapsule<Boolean> {
    }

}
