package usermanagement.service.user;

import usermanagement.dto.UserRegistrationRequest;
import usermanagement.model.User;
import jakarta.validation.Valid;

import java.util.List;

public interface UserService {

    List<User> findAllUsers();

    User createUser(User user);

    User updateUser(Long id, User user);

    void deleteUser(Long id);

    User findByUsername(String username);

    User registration(@Valid UserRegistrationRequest registrationRequest);

}
