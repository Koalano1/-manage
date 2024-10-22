package Manager.User.service;

import Manager.User.entity.User;

import java.util.List;


public interface UserService {

    User findByUsername(String username);

    List<User> findAllUsers();

    User createUser(User user);

    User updateUser(Long id, User user);

    void deleteUser(Long id);
}
