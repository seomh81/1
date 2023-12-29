package com.hdel.miri.api.util.response.swagger.alarm;

import com.hdel.miri.api.domain.alarm.AlarmSetup;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import lombok.Getter;

import java.util.List;

public class SwaggerAlarm {
    @Getter
    public static class GetAlarmSetupResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<AlarmSetup.AlarmSetupVO>> {
    }
    @Getter
    public static class AlarmSetupBoolResponse extends SwaggerResponse.SwaggerSuccessCapsule<Boolean> {
    }
}
