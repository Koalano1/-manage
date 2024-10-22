package Manager.User.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UserDetailDto {

    private String name;

    private String username;

    private String password;

    private String email;

    private String userRole;

}
