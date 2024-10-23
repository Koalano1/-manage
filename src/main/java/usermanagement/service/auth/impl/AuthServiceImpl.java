package usermanagement.service.auth.impl;

import usermanagement.dto.LoginRequestDto;
import usermanagement.dto.LoginResponseDto;
import usermanagement.exception.NotFoundException;
import usermanagement.exception.UnprocessableEntityException;
import usermanagement.model.User;
import usermanagement.repository.UserRepository;
import usermanagement.service.auth.AuthService;
import usermanagement.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public LoginResponseDto authenticate(LoginRequestDto loginUserDto) {
        User user = userRepository.findByEmail(loginUserDto.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));

        boolean matches = passwordEncoder.matches(loginUserDto.getPassword(), user.getPassword());
        if (!matches) {
            throw new UnprocessableEntityException("Password is incorrect");
        }

        String token = jwtUtils.generateJwtToken(user.getEmail());
        return LoginResponseDto.builder()
                .token(token)
                .isAuthenticated(true)
                .build();
    }

}
