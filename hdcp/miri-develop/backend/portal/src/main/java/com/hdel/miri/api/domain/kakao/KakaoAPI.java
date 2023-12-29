package com.hdel.miri.api.domain.kakao;

import com.hdel.miri.api.util.request.AbstractRequest;
import io.jsonwebtoken.io.Deserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class KakaoAPI {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UnitMaterialsByProjnoRequest extends AbstractRequest {
        @Schema(type = "List<String>"
                ,example = "[\"117149L01\" ,\"117245L03\"]",description = "원프로젝트+호기번호 리스트")
        private List<String> projnos;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UnitMaterialsByProjnoCountInfoRequest extends AbstractRequest {
        @Schema(type = "BigDecimal", example = "21", description = "사용자 포트폴리오 매핑 번호")
        private BigDecimal userPortfolioMappingId;
    }

    @Data
    public static class UnitMaterialsByProjnoSubRequest implements Serializable {
        private List<String> projnos;
        public UnitMaterialsByProjnoSubRequest(){}
        public UnitMaterialsByProjnoSubRequest(UnitMaterialsByProjnoRequest request){
            this.projnos = request.getProjnos();
        }

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UnitMaterialsByProjnoResponse {
        private List<UnitMaterialByProjno> projnos;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UnitMaterialByProjno {
        private String projno;
        private String buldNm;
        private List<UnitMaterial> materials;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UnitMaterial {
        private String mgroup;
        private String sgroup;
        private String material_number;
        private String visible_status;
        private String visible_changer_id;
        private String visible_change_date;
        private String mater_name;
        private String replacement_date;
        private BigDecimal days_elapsed_installation;
        private String predict_replacement_date;
        private UnitMaterialStandardDurability standard_durability;
        private BigDecimal accumulated_move;
        private String comment;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UnitMaterialStandardDurability {
        private String criteria;
        private BigDecimal limit;
        private BigDecimal average_limit;
        private BigDecimal replacement_cycle;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UnitMaterialsByProjnoCountInfoResponse {
        private long allCount;
        private long ropeCount;
        private long sheaveCount;
        private long mainInvCount;
        private long edgeCount;
        private long doorInvCount;
        private long otherCount;
    }

}
