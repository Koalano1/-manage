package Manager.User.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Data
public class LoginRequestDTO {

    @NotEmpty(message = "{login_username_not_empty}")
    private String username;

    @NotEmpty(message = "{login_password_not_empty}")
    private String password;

}
