package com.hdel.miri.api.domain.storage;

import com.hdel.miri.api.global.config.database.annotation.DS2Annotation;
import org.apache.ibatis.annotations.Mapper;

@DS2Annotation
@Mapper
public interface StorageRepository {
    Storage.View selectImage(String key);
    int insert(Storage.ImageSave request);
}
