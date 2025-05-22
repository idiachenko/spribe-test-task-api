package config;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class TestConfig {

    private static Properties properties;

    public static String baseUrl = getProperty("base_url", "http://3.68.165.45");
    public static String suiteName = getProperty("suite_name", "regressionTests");


    private static Properties getProperties() {
        if (null == properties) {
            properties = new Properties();
            try (InputStream stream = TestConfig.class.getResourceAsStream("/application.properties")) {
                if (null != stream) {
                    properties.load(stream);
                    log.info("Loading properties from located file application.properties");
                } else {
                    log.info("No application.properties file was found. Using default properties");
                }
            } catch (Exception e) {
                log.error("Failed to load application.properties", e);
            }
        }
        return properties;
    }

    private static String getProperty(String key, String def) {
        Properties properties = getProperties();
        String result = System.getProperty(key);
        if (null == result) {
            result = properties.getProperty(key, def);
        }
        log.debug("Test run property '{}' is set to '{}'", key, result);
        return result;
    }
}
