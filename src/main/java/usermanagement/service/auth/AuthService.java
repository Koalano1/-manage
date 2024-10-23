package usermanagement.service.auth;

import usermanagement.dto.LoginRequestDto;
import usermanagement.dto.LoginResponseDto;

public interface AuthService {

    LoginResponseDto authenticate(LoginRequestDto request);

}
