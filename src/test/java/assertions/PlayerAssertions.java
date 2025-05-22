package assertions;

import dto.request.CreatePlayerRequest;
import dto.response.CreatePlayerResponse;
import dto.response.getall.GetAllPlayersResponse;
import dto.response.getall.PlayersItem;
import io.qameta.allure.Step;
import org.testng.asserts.SoftAssert;

public class PlayerAssertions {

    @Step("Verify that all fields of the created player are not null")
    public static void assertPlayerFieldsNotNull(CreatePlayerResponse playerResponse) {
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertNotNull(playerResponse.getId(), "Player ID should not be null");
        softAssert.assertNotNull(playerResponse.getAge(), "Player age should not be null");
        softAssert.assertNotNull(playerResponse.getGender(), "Player gender should not be null");
        softAssert.assertNotNull(playerResponse.getLogin(), "Player login should not be null");
        softAssert.assertNotNull(playerResponse.getPassword(), "Player password should not be null");
        softAssert.assertNotNull(playerResponse.getScreenName(), "Player screen name should not be null");
        softAssert.assertNotNull(playerResponse.getRole(), "Player role should not be null");
        softAssert.assertAll();
    }

    @Step("Verify that the created player exists in the list and matches the details")
    public static void assertPlayerExistsAndMatches(
            GetAllPlayersResponse allPlayersResponse,
            CreatePlayerResponse createResponse
    ) {
        PlayersItem playersItem = allPlayersResponse.getPlayers().stream()
                .filter(player -> player.getId().equals(createResponse.getId()))
                .findFirst()
                .orElseThrow(() -> new AssertionError(String.format("Created player %s should exist in the list", createResponse.getId())));

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(playersItem.getAge(), createResponse.getAge(), "Player age should match");
        softAssert.assertEquals(playersItem.getGender(), createResponse.getGender(), "Player gender should match");
        softAssert.assertEquals(playersItem.getScreenName(), createResponse.getScreenName(), "Player screen name should match");
        softAssert.assertAll();
    }

    @Step("Verify that the updated player matches the update request")
    public static void assertUpdatedPlayerMatchesRequest(
            CreatePlayerResponse updatePlayerResponse,
            CreatePlayerRequest updateRequest
    ) {
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(updatePlayerResponse.getAge(), updateRequest.getAge(), "Player age should match");
        softAssert.assertEquals(updatePlayerResponse.getGender(), updateRequest.getGender(), "Player gender should match");
        softAssert.assertEquals(updatePlayerResponse.getLogin(), updateRequest.getLogin(), "Player login should match");
        softAssert.assertEquals(updatePlayerResponse.getPassword(), updateRequest.getPassword(), "Player password should match");
        softAssert.assertEquals(updatePlayerResponse.getScreenName(), updateRequest.getScreenName(), "Player screen name should match");
        softAssert.assertEquals(updatePlayerResponse.getRole(), updateRequest.getRole(), "Player role should match");
        softAssert.assertAll();
    }
}