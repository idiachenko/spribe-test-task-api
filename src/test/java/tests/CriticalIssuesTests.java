package tests;

import dto.request.CreatePlayerRequest;
import dto.response.CreatePlayerResponse;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.PlayerClient;
import utils.PlayerRequestUtils;

import static enums.Editors.*;
import static enums.Editors.SUPERVISOR;

public class CriticalIssuesTests extends BaseTest {

   /* Critical issues:
   1. Editor can create the same user twice
   2. Get by ID returns password
   3. User can delete other users
   4. User can be created without password
   5. Password can be updated to nothing and successfully saved.
   6. User's age can be updated to negative value
    */

    @Test(groups = "regression", description = "Verify that the same user cannot be created twice.")
    public void testDuplicateUserCreation() {
        Allure.description("This test verifies that creating the same user twice is not allowed.");
        CreatePlayerRequest request = PlayerRequestUtils.generateRandomPlayerRequest("25", "male", "user");

        Allure.step("Creating the user for the first time.");
        PlayerClient.createUser(request, ADMIN, 200);

        Allure.step("Attempting to create the same user again.");
        PlayerClient.createUser(request, ADMIN, 400);
    }

    @Test(groups = "regression", description = "Verify that the password is not returned when retrieving a user by ID.")
    public void testGetByIdDoesNotReturnPassword() {
        Allure.description("This test verifies that the password is not included in the response when retrieving a user by ID.");
        CreatePlayerRequest request = PlayerRequestUtils.generateRandomPlayerRequest("30", "female", "user");

        Allure.step("Creating a user.");
        CreatePlayerResponse createResponse = PlayerClient.createUser(request, SUPERVISOR, 200)
                .as(CreatePlayerResponse.class);

        Allure.step("Retrieving the user by ID.");
        Response getResponse = PlayerClient.getUser(createResponse.getId(), 200);

        Allure.step("Validating that the password is not present in the response.");
        Assert.assertNull(getResponse.jsonPath().get("password"), "Password should not be returned in the response. Security reasons");
    }

    @Test(groups = "regression", description = "Verify that a user cannot delete other users.")
    public void testUserCannotDeleteOtherUsers() {
        Allure.description("This test verifies that a user cannot delete other users.");
        CreatePlayerRequest request = PlayerRequestUtils.generateRandomPlayerRequest("22", "male", "user");

        Allure.step("Creating a user.");
        CreatePlayerResponse createResponse = PlayerClient.createUser(request, ADMIN, 200)
                .as(CreatePlayerResponse.class);

        Allure.step("Attempting to delete the user as another user.");
        PlayerClient.deleteUser(createResponse.getId(), USER, 403); // Expecting a 403 Forbidden error.
    }

    @Test(groups = "regression", description = "Verify that a user cannot be created without a password.")
    public void testUserCreationWithoutPassword() {
        Allure.description("This test verifies that a user cannot be created without a password.");
        CreatePlayerRequest request = PlayerRequestUtils.buildCreatePlayerRequest("28", "female", "user", "", "userWithoutPassword", "someName");

        Allure.step("Attempting to create a user without a password.");
        PlayerClient.createUser(request, SUPERVISOR, 400);
    }

    @Test(groups = "regression", description = "Verify that a password cannot be updated to an empty value.")
    public void testPasswordCannotBeUpdatedToEmpty() {
        Allure.description("This test verifies that a password cannot be updated to an empty value.");
        CreatePlayerRequest createRequest = PlayerRequestUtils.generateRandomPlayerRequest("35", "male", "user");

        Allure.step("Creating a user.");
        CreatePlayerResponse createResponse = PlayerClient.createUser(createRequest, SUPERVISOR, 200).as(CreatePlayerResponse.class);

        Allure.step("Attempting to update the user's password to an empty value.");

        PlayerClient.updateUser(createResponse.getId(), SUPERVISOR, "{\"password\": \"\"}", 400);
    }

    @Test(groups = "regression", description = "Verify that a age cannot be updated to a negative value.")
    public void testAgeCannotBeUpdatedToNegative() {
        Allure.description("This test verifies that a negative cannot be updated to negative value.");
        CreatePlayerRequest createRequest = PlayerRequestUtils.generateRandomPlayerRequest("35", "male", "user");

        Allure.step("Creating a user.");
        CreatePlayerResponse createResponse = PlayerClient.createUser(createRequest, SUPERVISOR, 200).as(CreatePlayerResponse.class);

        Allure.step("Attempting to update the user's age to an negative value.");

        PlayerClient.updateUser(createResponse.getId(), SUPERVISOR, "{\"age\": -26}", 400);
    }
}