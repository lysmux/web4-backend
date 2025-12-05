package dev.lysmux.web4.db.auth.passkey;

import dev.lysmux.web4.auth.provider.passkey.model.PassKeyCredential;
import dev.lysmux.web4.auth.provider.password.model.PasswordCredentials;
import dev.lysmux.web4.db.auth.password.PasswordCredentialsEntity;
import dev.lysmux.web4.db.user.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface PassKeyCredentialsMapper {
    @Mapping(source = "user.id", target = "userId")
    PassKeyCredential fromEntity(PassKeyCredentialEntity entity);

    @Mapping(target = "id", source = "credential.id")
    @Mapping(target = "createdAt", source = "credential.createdAt")
    PassKeyCredentialEntity toEntity(PassKeyCredential credential, UserEntity user);
}
