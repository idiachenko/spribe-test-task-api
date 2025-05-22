package tests;

import com.google.common.collect.ImmutableMap;
import config.TestConfig;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;


public class BaseTest {

    @BeforeClass
    public void baseSetup() {
        RestAssured.baseURI = TestConfig.baseUrl;
        setAllureEnvironment();
    }

    public void setAllureEnvironment() {
        allureEnvironmentWriter(
                ImmutableMap.<String, String>builder()
                        .put("Suite", TestConfig.suiteName)
                        .put("BASE_URL", TestConfig.baseUrl)
                        .put("Layer", "API")
                        .put("App", "SPRIBE_TEST_TASK")
                        .build(), System.getProperty("user.dir")
                        + "/build/allure-results/");
    }

}
