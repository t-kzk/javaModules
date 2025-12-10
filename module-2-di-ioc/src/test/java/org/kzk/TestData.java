package org.kzk;

import org.kzk.data.entity.EventEntity;
import org.kzk.data.entity.status.EventStatus;

public class TestData {

    public static EventEntity eventWithStatusCreated() {
      return   EventEntity.builder()
                .userId(1)
                .fileId(2)
                .status(EventStatus.CREATED)
                .build();
    }
}
