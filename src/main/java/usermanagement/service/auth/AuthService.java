package usermanagement.service.auth;

import jakarta.validation.Valid;
import usermanagement.dto.LoginRequestDto;
import usermanagement.dto.LoginResponseDto;
import usermanagement.dto.UserRegistrationRequest;
import usermanagement.dto.UserResponse;

public interface AuthService {

    LoginResponseDto authenticate(LoginRequestDto request);

    UserResponse register(@Valid UserRegistrationRequest registrationRequest);

}
