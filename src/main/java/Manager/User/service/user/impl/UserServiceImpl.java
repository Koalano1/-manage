package Manager.User.service.user.impl;

import Manager.User.dtos.UserRegistrationRequest;
import Manager.User.exception.NotFoundException;
import Manager.User.exception.UnprocessableEntityException;
import Manager.User.mapper.UserMapper;
import Manager.User.model.User;
import Manager.User.repository.UserRepository;
import Manager.User.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User registration(UserRegistrationRequest registrationRequest) {
        if(userRepository.findByUsername(registrationRequest.getUsername()) != null){
            throw new UnprocessableEntityException("Username already exists");
        }

        User user = userMapper.toUser(registrationRequest);
        return userRepository.save(user);
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user != null) {
            throw new NotFoundException("User not found");
        }
        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        try {
            return userRepository.save(user);
        } catch (OptimisticLockingFailureException e){
            throw new UnprocessableEntityException("User was updated by another user");
        }
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
