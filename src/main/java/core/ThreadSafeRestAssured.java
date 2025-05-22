package core;

import config.TestConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ThreadSafeRestAssured {
    private static final ThreadLocal<RequestSpecification> threadLocalSpec = ThreadLocal.withInitial(() ->
            RestAssured.given()
                    .log().all()
                    .relaxedHTTPSValidation()
                    .contentType("application/json")
    );

    public static RequestSpecification getRequestSpecification() {
        RestAssured.baseURI = TestConfig.baseUrl;
//        return threadLocalSpec.get().given();
        return RestAssured.given()
                .log().all()
                .relaxedHTTPSValidation()
                .contentType(ContentType.JSON);
    }
}
