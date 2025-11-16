package org.kzk.dto;

import lombok.Data;
import org.kzk.data.entity.status.FileStatus;

@Data
public class FileInfoDto {

    private Integer id;

    private String name;

    private String location;

    private FileStatus status;
}
