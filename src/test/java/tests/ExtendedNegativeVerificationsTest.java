package tests;

import dto.request.CreatePlayerRequest;
import dto.response.CreatePlayerResponse;
import enums.Editors;
import io.qameta.allure.Allure;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.PlayerClient;
import utils.PlayerRequestUtils;

import static assertions.PlayerAssertions.assertPlayerFieldsNotNull;
import static enums.Editors.*;

public class ExtendedNegativeVerificationsTest extends BaseTest {

    @DataProvider(name = "negativeCreateUser")
    public Object[][] negativeCreateUser() {
        return new Object[][]{
                {ADMIN, PlayerRequestUtils.generateRandomPlayerRequest("15", "male", "user")},
                {SUPERVISOR, PlayerRequestUtils.generateRandomPlayerRequest("61", "female", "admin")},
                {SUPERVISOR, PlayerRequestUtils.generateRandomPlayerRequest("0", "male", "user")},
                {SUPERVISOR, PlayerRequestUtils.generateRandomPlayerRequest("999", "male", "admin")},
                {ADMIN, PlayerRequestUtils.generateRandomPlayerRequest("20", "incorrect", "user")},
                {ADMIN, PlayerRequestUtils.generateRandomPlayerRequest("20", "female", "incorrect")},
                {ADMIN, PlayerRequestUtils.buildCreatePlayerRequest("18", "male", "", "password123", "admin", "normalName")},
        };
    }

    @Test(dataProvider = "negativeCreateUser", groups = "regression", description = "Verify creating a player with invalid data results in a 4xx error")
    public void negativeCreateUserTest(Editors editor, CreatePlayerRequest request) {
        Allure.description("This test verifies that creating a player with invalid data results in a 4xx error.");

        Allure.step("Attempting to create a player with editor: " + editor);
        PlayerClient.createUser(request, editor, 400);

    }

    @DataProvider(name = "negativeGetByIdUser")
    public Object[][] negativeGetByIdUser() {
        return new Object[][]{
                {0},
                {-1},
                {1000000001},
                {2147483648L},
        };
    }

    @Test(dataProvider = "negativeGetByIdUser", groups = "regression", description = "Verify retrieving a player by invalid ID results in a 4xx error")
    public void negativeGetByIdUser(long userId) {
        Allure.description("This test verifies that retrieving a player by an invalid ID results in a 4xx error.");

        Allure.step("Attempting to retrieve a player with ID: " + userId);
        PlayerClient.getUser(userId, 400);
    }

    @DataProvider(name = "negativeUserUpdate")
    public Object[][] negativeUserUpdate() {
        Allure.step("Preparing test data by creating a player with supervisor as the editor");
        CreatePlayerRequest request = PlayerRequestUtils.generateRandomPlayerRequest("16", "male", "user");
        CreatePlayerResponse createResponse = PlayerClient.createUser(request, SUPERVISOR, 200)
                .as(CreatePlayerResponse.class);
        Long playerId = createResponse.getId();
        assertPlayerFieldsNotNull(createResponse);
        return new Object[][]{
                {SUPERVISOR, 0L, request},
                {UNKNOWN, playerId, request},
                {SUPERVISOR, playerId, PlayerRequestUtils.buildCreatePlayerRequest("", "", "", "", "", "")},
        };
    }

    @Test(dataProvider = "negativeUserUpdate", groups = "regression", description = "Verify updating a player with invalid data results in a 4xx error")
    public void negativePatchUser(Editors editor, Long playerId, CreatePlayerRequest request) {
        Allure.description("This test verifies that updating a player with invalid data results in a 4xx error.");
        Allure.step("Attempting to update a player with editor: " + editor + " and player ID: " + playerId);
        PlayerClient.updateUser(playerId, editor, request, 400);

    }

    @DataProvider(name = "negativeDeleteByIdUser")
    public Object[][] negativeDeleteByIdUser() {
        CreatePlayerRequest request = PlayerRequestUtils.generateRandomPlayerRequest("16", "male", "admin");
        CreatePlayerResponse createResponse = PlayerClient.createUser(request, ADMIN, 200)
                .as(CreatePlayerResponse.class);
        Long createdByAdminPlayerId = createResponse.getId();
        assertPlayerFieldsNotNull(createResponse);
        return new Object[][]{
                {SUPERVISOR, 0},
                {ADMIN, -1},
                {SUPERVISOR, 1000000001},
                {ADMIN, 2147483648L},
                {ADMIN, createdByAdminPlayerId},
        };
    }

    @Test(dataProvider = "negativeDeleteByIdUser", groups = "regression", description = "Verify deleting a player with invalid data results in a 4xx error")
    public void negativeDeleteByIdUser(Editors editor, long userId) {
        Allure.description("This test verifies that deleting a player with invalid data results in a 4xx error.");
        Allure.step("Attempting to delete a player with editor: " + editor + " and player ID: " + userId);
        PlayerClient.deleteUser(userId, editor, 400);

    }
}