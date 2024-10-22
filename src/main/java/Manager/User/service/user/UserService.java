package Manager.User.service.user;

import Manager.User.dtos.UserRegistrationRequest;
import Manager.User.model.User;
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
