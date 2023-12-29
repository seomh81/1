package com.hdel.miri.api.util.response.swagger.portfolio;

import com.hdel.miri.api.domain.portfolio.Portfolio;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import lombok.Getter;

import java.util.List;

public class SwaggerPortfolio {
    @Getter
    public static class GetPortfolioResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<Portfolio.VO>> {
    }
    @Getter
    public static class PortfolioBoolResponse extends SwaggerResponse.SwaggerSuccessCapsule<Boolean> {
    }
}
