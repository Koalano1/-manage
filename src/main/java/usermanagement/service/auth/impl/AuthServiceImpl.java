package usermanagement.service.auth.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import usermanagement.dto.LoginRequestDto;
import usermanagement.dto.LoginResponseDto;
import usermanagement.dto.UserRegistrationRequest;
import usermanagement.dto.UserResponse;
import usermanagement.exception.NotFoundException;
import usermanagement.exception.UnauthorizedException;
import usermanagement.mapper.UserMapper;
import usermanagement.model.User;
import usermanagement.repository.UserRepository;
import usermanagement.service.auth.AuthService;
import usermanagement.service.jwt.JwtService;
import usermanagement.service.user.impl.UserDetailsServiceImpl;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public LoginResponseDto authenticate(LoginRequestDto request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        boolean matches = passwordEncoder.matches(request.getPassword(), userDetails.getPassword());
        if (!matches) {
            throw new UnauthorizedException("Password is incorrect");
        }

        String token = jwtService.generateToken(userDetails);
        return LoginResponseDto.builder()
                .token(token)
                .isAuthenticated(true)
                .build();
    }

    @Override
    public UserResponse register(UserRegistrationRequest registrationRequest) {
        Optional<User> existingUserWithUsername = userRepository.findByUsername(registrationRequest.getUsername());

        if (existingUserWithUsername.isPresent()) {
            throw new NotFoundException("Username already exists");
        }

        User user = User.builder()
                .email(registrationRequest.getEmail())
                .username(registrationRequest.getUsername())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .build();

        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

}
