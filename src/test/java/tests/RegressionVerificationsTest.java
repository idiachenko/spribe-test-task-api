package tests;

import dto.request.CreatePlayerRequest;
import dto.response.CreatePlayerResponse;
import dto.response.getall.GetAllPlayersResponse;
import enums.Editors;
import io.qameta.allure.Allure;
import io.restassured.http.ContentType;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.PlayerClient;
import utils.PlayerRequestUtils;

import static assertions.PlayerAssertions.assertPlayerFieldsNotNull;
import static assertions.PlayerAssertions.assertUpdatedPlayerMatchesRequest;
import static enums.Editors.*;

public class RegressionVerificationsTest extends BaseTest {

    @DataProvider(name = "dataProviderRequests")
    public Object[][] dataProviderRequests() {
        return new Object[][]{
                {ADMIN, PlayerRequestUtils.generateRandomPlayerRequest("17", "male", "user")},
                {SUPERVISOR, PlayerRequestUtils.generateRandomPlayerRequest("18", "male", "user")},
                {SUPERVISOR, PlayerRequestUtils.generateRandomPlayerRequest("59", "female", "admin")}
        };
    }

    @Test(dataProvider = "dataProviderRequests", groups = "regression", description = "Verify that admin or supervisor can successfully create a user or admin")
    public void testUserCreatePositive(Editors editor, CreatePlayerRequest request) {
        Allure.description("This test verifies that admin or supervisor can successfully create a user or admin.");
        Allure.step("Creating a player with editor: " + editor);
        CreatePlayerResponse createResponse = PlayerClient.createUser(request, editor, 200)
                .as(CreatePlayerResponse.class);
        Allure.step("Validating that all fields of the created player are not null");
        assertPlayerFieldsNotNull(createResponse);
    }

    @Test(groups = "regression", description = "Verify that creating a user with an unknown editor fails")
    public void testUserCreateNegative() {
        Allure.description("This test verifies that creating a user with an unknown editor fails.");
        CreatePlayerRequest createPlayerRequest = PlayerRequestUtils.generateRandomPlayerRequest("25", "male", "user");
        Allure.step("Attempting to create a player with editor: " + UNKNOWN.getEditor());
        PlayerClient.createUser(createPlayerRequest, UNKNOWN, 400);
    }

    @Test(dataProvider = "dataProviderRequests", groups = "regression", description = "Verify that admin or supervisor can successfully update a user or admin")
    public void testUserUpdatePositive(Editors editor, CreatePlayerRequest request) {
        Allure.description("This test verifies that admin or supervisor can successfully update a user or admin.");
        Allure.step("Creating a player with editor: " + editor);
        CreatePlayerResponse createResponse = PlayerClient.createUser(request, editor, 200)
                .as(CreatePlayerResponse.class);
        Long playerId = createResponse.getId();
        Assert.assertNotNull(playerId, "Player id should not be null");

        Allure.step("Updating the player details");
        CreatePlayerRequest updateRequest = PlayerRequestUtils.generateRandomPlayerRequest("58", "male", request.getRole());
        CreatePlayerResponse updatePlayerResponse = PlayerClient.updateUser(playerId, editor, updateRequest, 200)
                .as(CreatePlayerResponse.class);
        Allure.step("Validating that the updated player matches the update request");
        assertUpdatedPlayerMatchesRequest(updatePlayerResponse, updateRequest);
    }

    @Test(dataProvider = "dataProviderRequests", groups = "regression", description = "Verify that updating a non-existing user fails")
    public void testUserUpdateNegative(Editors editor, CreatePlayerRequest request) {
        Allure.description("This test verifies that updating a non-existing user fails.");
        Allure.step("Attempting to update a non-existing player with random player ID");
        PlayerClient.updateUser(PlayerRequestUtils.randomPlayerId(), editor, request, 400);
    }

    @Test(dataProvider = "dataProviderRequests", groups = "regression", description = "Verify that admin or supervisor can retrieve a user by ID")
    public void testUserGetByIdPositive(Editors editor, CreatePlayerRequest request) {
        Allure.description("This test verifies that admin or supervisor can retrieve a user by ID.");
        Allure.step("Creating a player with editor: " + editor);
        CreatePlayerResponse createResponse = PlayerClient.createUser(request, editor, 200)
                .as(CreatePlayerResponse.class);
        Long playerId = createResponse.getId();
        Assert.assertNotNull(createResponse.getId(), "Player id is null");

        Allure.step("Retrieving the player by ID and verifying the response matches the created player");
        CreatePlayerResponse getPlayerResponse = PlayerClient.getUser(playerId, 200)
                .as(CreatePlayerResponse.class);
        Assert.assertEquals(getPlayerResponse, createResponse, "Wrong player response");
    }

    @Test(groups = "regression", description = "Verify that retrieving a non-existing user by ID fails")
    public void testUserGetByIdNegative() {
        Allure.description("This test verifies that retrieving a non-existing user by ID fails.");
        Allure.step("Attempting to retrieve a non-existing player by random player ID");
        PlayerClient.getUser(PlayerRequestUtils.randomPlayerId(), 400);
    }

    @Test(groups = "regression", description = "Verify that admin or supervisor can retrieve all users")
    public void testUserGetAllPositive() {
        Allure.description("This test verifies that admin or supervisor can retrieve all users.");
        Allure.step("Retrieving all players and verifying the response");
        PlayerClient.getAllUsers(200)
                .as(GetAllPlayersResponse.class);
    }

    @Test(groups = "regression", description = "Verify that retrieving all users with invalid content type fails")
    public void testUserGetAllNegative() {
        Allure.description("This test verifies that retrieving all users with an invalid content type fails.");
        Allure.step("Attempting to retrieve all players with invalid content type");
        PlayerClient.getAllUsers(400, ContentType.TEXT);
    }

    @Test(dataProvider = "dataProviderRequests", groups = "regression", description = "Verify that admin or supervisor can delete a user or admin")
    public void testUserDeletePositive(Editors editor, CreatePlayerRequest request) {
        Allure.description("This test verifies that admin or supervisor can delete a user or admin.");
        Allure.step("Creating a player with editor: " + editor);
        CreatePlayerResponse createResponse = PlayerClient.createUser(request, editor, 200)
                .as(CreatePlayerResponse.class);
        Long playerId = createResponse.getId();

        Allure.step("Deleting the player");
        PlayerClient.deleteUser(playerId, editor, 204);

        Allure.step("Validating that the player is deleted");
        PlayerClient.getUser(playerId, 400);
    }

    @Test(groups = "regression", description = "Verify that deleting a non-existing user fails")
    public void testUserDeleteNegative() {
        Allure.description("This test verifies that deleting a non-existing user fails.");
        Allure.step("Attempting to delete a non-existing player with random player ID");
        PlayerClient.deleteUser(PlayerRequestUtils.randomPlayerId(), SUPERVISOR, 400);
    }

    @Test(groups = "regression", description = "User edits and retrieves his own information")
    public void testGetEditByUserPositive() {
        Allure.description("This test verifies if a user is able to edit and retrieve information about themselves.");

        Allure.step("Preparing test data by creating a player with supervisor as the editor");
        CreatePlayerRequest request = PlayerRequestUtils.generateRandomPlayerRequest("17", "male", "user");
        CreatePlayerResponse createResponse = PlayerClient.createUser(request, SUPERVISOR, 200)
                .as(CreatePlayerResponse.class);
        Long playerId = createResponse.getId();
        Assert.assertNotNull(playerId, "Player id is null");

        Allure.step("Updating the player details on behalf of the user");
        CreatePlayerRequest updateRequest = PlayerRequestUtils.generateRandomPlayerRequest("50", "male", request.getRole());
        CreatePlayerResponse updatePlayerResponse = PlayerClient.updateUser(playerId, USER, updateRequest, 200)
                .as(CreatePlayerResponse.class);
        assertUpdatedPlayerMatchesRequest(updatePlayerResponse, updateRequest);

        Allure.step("Validating the updated player details");
        CreatePlayerResponse updatedPlayerResponse = PlayerClient.getUser(playerId, 200)
                .as(CreatePlayerResponse.class);
        Assert.assertEquals(updatedPlayerResponse, updatePlayerResponse, "Player login should be updated");
    }
}