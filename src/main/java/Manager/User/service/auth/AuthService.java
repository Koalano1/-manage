package Manager.User.service.auth;

import Manager.User.dtos.LoginRequestDto;
import Manager.User.dtos.LoginResponseDto;
import Manager.User.exception.NotFoundException;
import Manager.User.exception.UnprocessableEntityException;
import Manager.User.model.User;
import Manager.User.repository.UserRepository;
import Manager.User.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

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
