package dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class CreatePlayerRequest {
    private String age;
    private String gender;
    private String login;
    private String password;
    private String role;
    private String screenName;
}
