package usermanagement.dto;

import lombok.*;

@Builder
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequest {

    private String username;

    private String password;

    private String email;

}
