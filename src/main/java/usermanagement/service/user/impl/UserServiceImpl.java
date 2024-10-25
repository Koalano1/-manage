package usermanagement.service.user.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import usermanagement.dto.UserRegistrationRequest;
import usermanagement.dto.UserResponse;
import usermanagement.exception.NotFoundException;
import usermanagement.exception.UnauthorizedException;
import usermanagement.mapper.UserMapper;
import usermanagement.model.ERole;
import usermanagement.model.Role;
import usermanagement.model.User;
import usermanagement.repository.RoleRepository;
import usermanagement.repository.UserRepository;
import usermanagement.service.user.UserService;
import usermanagement.util.JwtTokenProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    RoleRepository roleRepository;

    PasswordEncoder passwordEncoder;

    UserMapper userMapper;

    JwtTokenProvider jwtTokenProvider;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("user not found"));
    }

    @Override
    public UserResponse getUserResponse(UserRegistrationRequest request) {
        Optional<User> existingUserWithUsername = userRepository.findByUsername(request.getUsername());

        if (existingUserWithUsername.isPresent()) {
            throw new NotFoundException("Username already exists");
        }

        Role userRole = roleRepository.findByName(ERole.USER)
                .orElseThrow(() -> new NotFoundException("Role not found")); // EDIT

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(new HashSet<>(List.of(userRole))) // EDIT
                .build();
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    public Object getUserByJwt(String jwt) {
        String userName = jwtTokenProvider.getUserNameFromJwtToken(jwt.substring(7));

        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new NotFoundException("User not found"));

        checkOneTimeToken(jwt);
        return userMapper.toUserResponse(user);
    }

    private void checkOneTimeToken(String jwt) {
        String userName = jwtTokenProvider.getUsername(jwt.substring(7));

        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new NotFoundException("User not found"));

        //TODO: Implement the logic to check if the token is one-time

    }


    @Override
    public ResponseEntity<User> createUser(User user) {
        Role adminRole = roleRepository.findByName(ERole.ADMIN)
                .orElseThrow(() -> new NotFoundException("Role not found"));

        User newUser = User.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .email(user.getEmail())
                .roles(new HashSet<>(List.of(adminRole)))
                .build();
        return ResponseEntity.ok(
                userRepository.save(newUser));
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }


    @Override
    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (user != null) {
            throw new NotFoundException("User not found");
        }
        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        try {
            return userRepository.save(user);
        } catch (OptimisticLockingFailureException e) {
            throw new UnauthorizedException("User was updated by another user");
        }
    }

    @Override
    public void deleteUser(Long id) {
        if (id == null) {
            throw new NotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }
}
