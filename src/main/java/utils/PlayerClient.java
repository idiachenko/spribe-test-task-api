package utils;

import dto.request.CreatePlayerRequest;
import enums.Editors;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

import static core.BaseRestAssured.getRequestSpecification;

public class PlayerClient {

    private static final Logger logger = LoggerFactory.getLogger(PlayerClient.class);

    private static Response executeRequest(Supplier<Response> responseSupplier, int expectedStatusCode) {
        logger.debug("Executing request with expected status code: {}", expectedStatusCode);
        Response response = responseSupplier.get();
        String result = (response == null || response.asString().isEmpty()) ? "Empty response" : response.asPrettyString();
        Allure.attachment("Response", result);
        response.then()
                .statusCode(expectedStatusCode)
                .contentType(ContentType.JSON);
        logger.info("Request executed successfully with status code: {}", response.getStatusCode());
        return response;
    }

    @Step("Create a user with editor: {editor} and request: {request}")
    public static Response createUser(CreatePlayerRequest request, Editors editor, int expectedStatusCode) {
        logger.info("Creating user with editor: {} and request: {}", editor, request);
        RequestSpecification newRequest = getRequestSpecification();
        return executeRequest(
                () -> newRequest
                        .basePath("/player/create")
                        .pathParam("editor", editor.getEditor())
                        .queryParam("age", request.getAge())
                        .queryParam("gender", request.getGender())
                        .queryParam("login", request.getLogin())
                        .queryParam("password", request.getPassword())
                        .queryParam("role", request.getRole())
                        .queryParam("screenName", request.getScreenName())
                        .when()
                        .get("/{editor}"),
                expectedStatusCode
        );
    }

    @Step("Get user by playerId: {playerId}")
    public static Response getUser(Long playerId, int expectedStatusCode) {
        logger.info("Fetching user with playerId: {}", playerId);
        String body = String.format("{\"playerId\": %d}", playerId);
        RequestSpecification newRequest = getRequestSpecification();
        return executeRequest(
                () -> newRequest
                        .contentType(ContentType.JSON)
                        .body(body)
                        .when()
                        .post("/player/get"),
                expectedStatusCode
        );
    }

    @Step("Get all users")
    public static Response getAllUsers(int expectedStatusCode) {
        return getAllUsers(expectedStatusCode, ContentType.JSON);
    }

    @Step("Get all users")
    public static Response getAllUsers(int expectedStatusCode, ContentType contentType) {
        logger.info("Fetching all users");
        RequestSpecification newRequest = getRequestSpecification();
        return executeRequest(
                () -> newRequest
                        .accept(contentType)
                        .when()
                        .get("/player/get/all"),
                expectedStatusCode
        );
    }

    @Step("Update user with playerId: {playerId}, editor: {editor}, and request: {request}")
    public static Response updateUser(Long playerId, Editors editor, CreatePlayerRequest request, int expectedStatusCode) {
        logger.info("Updating user with playerId: {}, editor: {}, and request: {}", playerId, editor, request);
        RequestSpecification newRequest = getRequestSpecification();
        return executeRequest(
                () -> newRequest
                        .contentType(ContentType.JSON)
                        .body(request)
                        .when()
                        .patch("/player/update/" + editor.getEditor() + "/" + playerId),
                expectedStatusCode
        );
    }

    @Step("Delete user with playerId: {playerId} by editor: {editor}")
    public static Response deleteUser(Long playerId, Editors editor, int expectedStatusCode) {
        logger.info("Deleting user with playerId: {} by editor: {}", playerId, editor);
        String body = String.format("{\"playerId\": %d}", playerId);
        RequestSpecification newRequest = getRequestSpecification();
        return executeRequest(
                () -> newRequest
                        .contentType(ContentType.JSON)
                        .body(body)
                        .when()
                        .delete("/player/delete/" + editor.getEditor()),
                expectedStatusCode
        );
    }
}