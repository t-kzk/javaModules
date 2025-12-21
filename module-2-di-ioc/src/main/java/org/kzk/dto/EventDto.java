package org.kzk.dto;

import lombok.Data;
import org.kzk.data.entity.status.EventStatus;

import java.time.OffsetDateTime;

@Data
public class EventDto {

    private Integer id;
    private EventStatus status;
    private OffsetDateTime time;
    private UserDto user;
    private FileInfoDto file;

}
