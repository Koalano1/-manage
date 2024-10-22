package Manager.User.mapper;

import Manager.User.dtos.UserRegistrationRequest;
import Manager.User.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserRegistrationRequest registrationRequest);

}
