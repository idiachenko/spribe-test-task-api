package dto.response.getall;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PlayersItem {
    private String gender;
    private Integer id;
    private String screenName;
    private Integer age;
}