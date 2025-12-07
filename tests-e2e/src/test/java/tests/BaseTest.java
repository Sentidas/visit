package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {

    private static final String BASE_URL = "http://localhost:8088/api";

    @BeforeEach
    void setupUp() {
        RestAssured.baseURI = BASE_URL;

    }
}
