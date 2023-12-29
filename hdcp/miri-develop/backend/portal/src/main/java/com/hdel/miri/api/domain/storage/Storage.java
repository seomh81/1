package com.hdel.miri.api.domain.storage;

import com.hdel.miri.api.util.request.AbstractRequest;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class Storage {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageSave extends AbstractRequest {
        @Hidden
        private List<FileInfo> files;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileInfo {
        private String physicalFileName;
        private String originalFileName;
        private String fullFilePath;
        private String requester;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class View {
        private String physicalFileName;
        private String originalFileName;
        private String fullFilePath;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CAFileInfo {
        private String savedFilePath;
        private String caFileNm;
    }

}
