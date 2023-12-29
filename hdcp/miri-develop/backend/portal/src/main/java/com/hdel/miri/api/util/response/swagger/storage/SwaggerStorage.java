package com.hdel.miri.api.util.response.swagger.storage;

import com.hdel.miri.api.domain.alarm.AlarmSetup;
import com.hdel.miri.api.util.response.swagger.SwaggerResponse;
import lombok.Getter;

import java.util.List;

public class SwaggerStorage {
    @Getter
    public static class GetStorageUploadResponse extends SwaggerResponse.SwaggerSuccessCapsule<List<String>> {
    }
    @Getter
    public static class StorageByteResponse extends SwaggerResponse.SwaggerSuccessCapsule<byte[]> {
    }
}
