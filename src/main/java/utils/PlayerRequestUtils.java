package utils;

import dto.request.CreatePlayerRequest;

import java.util.Random;
import java.util.UUID;

public class PlayerRequestUtils {

    private static final Random RANDOM = new Random();

    public static CreatePlayerRequest buildCreatePlayerRequest(String age,
                                                               String gender,
                                                               String login,
                                                               String password,
                                                               String role,
                                                               String screenName) {
        return CreatePlayerRequest.builder()
                .age(age)
                .gender(gender)
                .login(login)
                .password(password)
                .role(role)
                .screenName(screenName)
                .build();
    }

    public static String randomLogin() {
        return "user" + UUID.randomUUID().toString().substring(0, 8); // Unique login
    }

    public static String randomPassword() {
        return "Pass" + RANDOM.nextInt(10000) + "w0rd"; // Password with letters and numbers
    }

    public static String randomScreenName() {
        return "Screen" + UUID.randomUUID().toString().substring(0, 8); // Unique screen name
    }

    public static CreatePlayerRequest generateRandomPlayerRequest(String age, String gender, String role) {
        return CreatePlayerRequest.builder()
                .age(age)
                .gender(gender)
                .login(randomLogin())
                .password(randomPassword())
                .role(role)
                .screenName(randomScreenName())
                .build();
    }

    public static Long randomPlayerId() {
        return Math.abs(RANDOM.nextLong());
    }
}
