package dev.lysmux.web4.db.auth.vk;

import dev.lysmux.web4.auth.provider.vk.model.VKUser;
import dev.lysmux.web4.db.user.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface VKUserMapper {
    @Mapping(source = "user.id", target = "userId")
    VKUser fromEntity(VKUserEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", source = "vkUser.createdAt")
    VKUserEntity toEntity(VKUser vkUser, UserEntity user);
}
