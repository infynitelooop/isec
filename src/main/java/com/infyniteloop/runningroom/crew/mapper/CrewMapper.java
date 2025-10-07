package com.infyniteloop.runningroom.crew.mapper;

import com.infyniteloop.runningroom.crew.dto.*;
import com.infyniteloop.runningroom.crew.entity.Crew;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CrewMapper {
    CrewResponse toDto(Crew crew);
    Crew toEntity(CrewRequest dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(CrewRequest dto, @MappingTarget Crew crew);
}
