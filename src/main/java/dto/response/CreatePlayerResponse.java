package dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class CreatePlayerResponse {
    private String password;
    private String role;
    private String gender;
    private Long id;
    private String screenName;
    private String login;
    private Integer age;
}
