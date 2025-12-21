package org.kzk.mapper;

import org.kzk.data.entity.FileEntity;
import org.kzk.dto.FileInfoDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileMapper {
    FileInfoDto map(FileEntity file);

    @InheritInverseConfiguration
    FileEntity map(FileInfoDto file);
}
