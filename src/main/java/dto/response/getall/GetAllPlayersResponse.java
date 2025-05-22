package dto.response.getall;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class GetAllPlayersResponse {
    private List<PlayersItem> players;
}