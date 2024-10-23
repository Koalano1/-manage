package usermanagement.mapper;

import org.mapstruct.Mapper;
import usermanagement.dto.UserRegistrationRequest;
import usermanagement.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRegistrationRequest registrationRequest);

}
