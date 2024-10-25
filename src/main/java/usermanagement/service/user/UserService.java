package usermanagement.service.user;

import org.springframework.http.ResponseEntity;
import usermanagement.dto.UserRegistrationRequest;
import usermanagement.dto.UserResponse;
import usermanagement.model.User;

import java.util.List;

public interface UserService {

    List<User> findAllUsers();

    User updateUser(Long id, User user);

    void deleteUser(Long id);

    User findByUsername(String username);

    UserResponse getUserResponse(UserRegistrationRequest request);

    Object getUserByJwt(String jwt);

    ResponseEntity<User> createUser(User user);

    //UserResponse registerNewLockedUser(UserRegistrationRequest request);

}
