package org.kzk.mapper;

import org.kzk.data.entity.UserEntity;
import org.kzk.dto.UserDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto map(UserEntity entity);

    @InheritInverseConfiguration
    UserEntity map(UserDto user);
}
