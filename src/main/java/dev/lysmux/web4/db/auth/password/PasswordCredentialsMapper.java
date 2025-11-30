package dev.lysmux.web4.db.auth.password;

import dev.lysmux.web4.auth.provider.password.model.PasswordCredentials;
import dev.lysmux.web4.db.user.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface PasswordCredentialsMapper {
    @Mapping(source = "user.id", target = "userId")
    PasswordCredentials fromEntity(PasswordCredentialsEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", source = "credentials.createdAt")
    @Mapping(target = "updatedAt", source = "credentials.updatedAt")
    PasswordCredentialsEntity toEntity(PasswordCredentials credentials, UserEntity user);
}
