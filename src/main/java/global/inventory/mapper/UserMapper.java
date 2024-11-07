package global.inventory.mapper;

import global.inventory.model.User;
import global.inventory.payload.request.UserDetailsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    List<UserDetailsResponse> userListToDtoList(List<User> users);

    UserDetailsResponse userToDto(User user);
}
