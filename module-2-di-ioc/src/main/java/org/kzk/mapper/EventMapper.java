package org.kzk.mapper;

import org.kzk.data.entity.EventEntity;
import org.kzk.dto.EventDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper {
    EventDto map(EventEntity event);

    @InheritInverseConfiguration
    EventEntity map(EventDto eventDto);
}
