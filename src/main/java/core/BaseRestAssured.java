package core;

import config.TestConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseRestAssured {
    public static RequestSpecification getRequestSpecification() {
        RestAssured.baseURI = TestConfig.baseUrl;
        return RestAssured.given()
                .log().all()
                .relaxedHTTPSValidation()
                .contentType(ContentType.JSON);
    }
}
