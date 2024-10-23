package usermanagement.service.user;

import usermanagement.model.User;

import java.util.List;

public interface UserService {

    List<User> findAllUsers();

    User createUser(User user);

    User updateUser(Long id, User user);

    void deleteUser(Long id);

    User findByUsername(String username);

}
