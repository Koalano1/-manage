package usermanagement.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmailMessage {

    private String to;

    private String subject;

    private String body;

}
