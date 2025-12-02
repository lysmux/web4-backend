package dev.lysmux.web4.dto.hit;

import dev.lysmux.web4.domain.model.Hit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface HitMapper {
    @Mapping(target="createdAt", expression="java(hit.getCreatedAt().toEpochMilli())")
    HitDto toDto(Hit hit);
}
