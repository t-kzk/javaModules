package org.kzk;

import org.kzk.data.entity.EventEntity;
import org.kzk.data.entity.FileEntity;
import org.kzk.data.entity.status.EventStatus;
import org.kzk.data.entity.status.FileStatus;


public class TestData {


    public static final String FILE_PATH = "testData/test.txt";

    public static EventEntity eventWithStatusCreated() {
      return   EventEntity.builder()
                .userId(1)
                .fileId(2)
                .status(EventStatus.CREATED)
                .build();
    }

    public static FileEntity fileWithStatusActive() {
        return FileEntity.builder()
                .id(10)
                .name("test.txt")
                .location("1/%s".formatted(FILE_PATH))
                .status(FileStatus.ACTIVE)
                .build();
    }
}
