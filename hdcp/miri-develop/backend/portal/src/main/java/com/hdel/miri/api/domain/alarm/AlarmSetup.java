package com.hdel.miri.api.domain.alarm;

import com.hdel.miri.api.domain.alarm.valid.OnAlarmUpdate;
import com.hdel.miri.api.domain.masterdata.MasterData;
import com.hdel.miri.api.util.request.AbstractRequest;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public class AlarmSetup {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlarmSetupCurrent extends AbstractRequest {
        @Hidden
        private String userId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlarmDefaultCreate{
        private String userId;
        // private List<MasterData.AlarmCode> codes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlarmSetupUpdate extends AbstractRequest{
        @Schema(type = "List<AlarmSetupUpdateCode>", description = " 다수 알람 코드 설정 값",required = true)
        @NotNull(groups = { OnAlarmUpdate.class }, message = "변경할 코드 입력이 필요합니다.")
        @Size(min = 1,groups = { OnAlarmUpdate.class }, message = "변경할 코드가 하나 이상 필요합니다.")
        List<AlarmSetupUpdateCode> codes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlarmRuleUpdate extends AbstractRequest{
        @Schema(type = "List<AlarmRuleUpdateCode>", description = " 다수 알람 코드 설정 값",required = true)
        @NotNull(groups = { OnAlarmUpdate.class }, message = "변경할 코드 입력이 필요합니다.")
        @Size(min = 1,groups = { OnAlarmUpdate.class }, message = "변경할 코드가 하나 이상 필요합니다.")
        List<AlarmRuleUpdateCode> codes;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlarmRuleUpdateCode {
        @Schema(type = "String", example = "100204", description = "카테고리번호+디비전번호+전송방식코드로 총 6자리 숫자")
        private String updateId;

        @Schema(type = "String", example = "n", description = "사용유무")
        private String useYn;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlarmSetupUpdateCode {
        @Schema(type = "BigDecimal", example = "1", description = "알람 설정 키 밸류")
        private BigDecimal alarmSetupId;

        @Schema(type = "String", example = "y", description = "알람 사용여부")
        private String useYn;
    }
    
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlarmSetupVO {
        @Schema(example = "56")
        private BigDecimal alarmSetupId;
        @Schema(example = "0010")
        private String catCode;
        @Schema(example = "고장")
        private String catName;
        @Schema(example = "0011")
        private String divCode;
        @Schema(example = "발생/접수")
        private String divName;
        @Schema(example = "101101")
        private String typeCode;
        @Schema(example = "E-mail 알람")
        private String typeName;
        @Schema(example = "n")
        private String useYn;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlarmRuleVO {
        @Schema(example = "korea@korea.com")
        private String userId;
        private List<AlarmRuleListVO> alarmRuleList;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlarmRuleListVO {
        @Schema(example = "10")
        private String catNo;
        @Schema(example = "고장")
        private String catNm;
        private List<AlarmDivListVO> alarmDivList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlarmDivListVO {
        @Schema(example = "56")
        private String divNo;
        @Schema(example = "접수")
        private String divNm;
        @Schema(example = "y")
        private String useYn;
        private List<AlarmDetailVO> alarmDetailList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlarmDetailVO {
        @Schema(example = "56")
        private String alarmSetupId;
        @Schema(example = "56")
        private String sendMethodNo;
        @Schema(example = "KAKAO")
        private String sendMethodNm;
        @Schema(example = "y")
        private String useYn;
        @Schema(example = "y")
        private String methodYn;        
    }
}
