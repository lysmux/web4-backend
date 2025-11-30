package dev.lysmux.web4.db.user;

import dev.lysmux.web4.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.JAKARTA_CDI)
public interface UserMapper {
    User fromEntity(UserEntity entity);

    UserEntity toEntity(User user);
}
