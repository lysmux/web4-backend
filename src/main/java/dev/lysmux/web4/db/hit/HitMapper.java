package dev.lysmux.web4.db.hit;

import dev.lysmux.web4.db.user.UserEntity;
import dev.lysmux.web4.domain.model.Hit;
import dev.lysmux.web4.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface HitMapper {
    @Mapping(source = "owner.id", target = "ownerId")
    Hit fromEntity(HitEntity entity);

    @Mapping(target = "id", source = "hit.id")
    @Mapping(target = "createdAt", source = "hit.createdAt")
    HitEntity toEntity(Hit hit, UserEntity owner);
}
