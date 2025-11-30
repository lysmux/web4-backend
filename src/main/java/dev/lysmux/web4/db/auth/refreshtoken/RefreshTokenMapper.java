package dev.lysmux.web4.db.auth.refreshtoken;

import dev.lysmux.web4.auth.model.RefreshToken;
import dev.lysmux.web4.db.user.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface RefreshTokenMapper {
    @Mapping(source = "user.id", target = "userId")
    RefreshToken fromEntity(RefreshTokenEntity entity);

    @Mapping(target = "id", source = "token.id")
    @Mapping(target = "createdAt", source = "token.createdAt")
    RefreshTokenEntity toEntity(RefreshToken token, UserEntity user);
}
