package com.interview.empik.coupons.controller;

import com.interview.empik.coupons.service.CountryService;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import io.restassured.RestAssured;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TestCouponsRestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @MockitoBean
    private CountryService countryService;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.reset();
        RestAssured.port = port;
    }

    @AfterEach
    public void clearDb() {
        jdbcTemplate.execute("DELETE FROM USER_COUPON_USAGE");
        jdbcTemplate.execute("DELETE FROM COUPONS");
    }

    @ParameterizedTest
    @MethodSource("createCouponArguments")
    void shouldProperlyHandleCreateNewCoupon(String json, int httpCode, int internalCode) {
        given()
                .contentType(ContentType.JSON)
                .body(json)
        .when()
                .post("/api/coupons/create")
        .then()
                .statusCode(httpCode)
                .body("code", equalTo(internalCode));
    }

    @Test
    void shouldNotAllowDuplicateCoupon() {
        createTestCoupon(5);

        given()
                .contentType(ContentType.JSON)
                .body(getCreateCouponJson("TEST_CODE", 1, "Polska"))
        .when()
                .post("/api/coupons/create")
        .then()
                .statusCode(400)
                .body("code", equalTo(4005));
    }

    @ParameterizedTest
    @MethodSource("useCouponArguments")
    void shouldProperlyHandleUseCoupon(String json, int httpCode, int internalCode) {
        doReturn("Polska").when(countryService).getCountryByIP(anyString());

        createTestCoupon(5);

        given()
                .contentType(ContentType.JSON)
                .header("X-Forwarded-For", "127.0.0.1")
                .body(json)
        .when()
                .post("/api/coupons/use")
        .then()
                .statusCode(httpCode)
                .body("code", equalTo(internalCode));
    }

    @Test
    void shouldReturnErrorIfCouponDoesNotExist() {
        doReturn("Polska").when(countryService).getCountryByIP(anyString());

        given()
                .contentType(ContentType.JSON)
                .header("X-Forwarded-For", "127.0.0.1")
                .body(getUseCouponJson("TEST_CODE", "USER"))
        .when()
                .post("/api/coupons/use")
        .then()
                .statusCode(400)
                .body("code", equalTo(4001));
    }

    @Test
    void shouldReturnErrorIfUserAlreadyUsedThisCoupon() {
        doReturn("Polska").when(countryService).getCountryByIP(anyString());

        createTestCoupon(5);

        given()
                .contentType(ContentType.JSON)
                .header("X-Forwarded-For", "127.0.0.1")
                .body(getUseCouponJson("TEST_CODE", "USER"))
        .when()
                .post("/api/coupons/use")
        .then()
                .statusCode(200)
                .body("code", equalTo(2000));

        given()
                .contentType(ContentType.JSON)
                .header("X-Forwarded-For", "127.0.0.1")
                .body(getUseCouponJson("TEST_CODE", "USER"))
        .when()
                .post("/api/coupons/use")
        .then()
                .statusCode(400)
                .body("code", equalTo(4003));

    }

    @Test
    void shouldReturnErrorIfMaxUsageHasBeenReached() {
        doReturn("Polska").when(countryService).getCountryByIP(anyString());

        createTestCoupon(1);

        given()
                .contentType(ContentType.JSON)
                .header("X-Forwarded-For", "127.0.0.1")
                .body(getUseCouponJson("TEST_CODE", "USER"))
        .when()
                .post("/api/coupons/use")
        .then()
                .statusCode(200)
                .body("code", equalTo(2000));

        given()
                .contentType(ContentType.JSON)
                .header("X-Forwarded-For", "127.0.0.1")
                .body(getUseCouponJson("TEST_CODE", "USER2"))
        .when()
                .post("/api/coupons/use")
        .then()
                .statusCode(400)
                .body("code", equalTo(4004));
    }

    @Test
    void shouldReturnErrorIfCountryMismatch() {
        doReturn("USA").when(countryService).getCountryByIP(anyString());

        createTestCoupon(1);

        given()
                .contentType(ContentType.JSON)
                .header("X-Forwarded-For", "127.0.0.1")
                .body(getUseCouponJson("TEST_CODE", "USER"))
        .when()
                .post("/api/coupons/use")
        .then()
                .statusCode(400)
                .body("code", equalTo(4002));
    }

    private void createTestCoupon(int maxUsage) {
        given()
                .contentType(ContentType.JSON)
                .body(getCreateCouponJson("TEST_CODE", maxUsage, "Polska"))
        .when()
                .post("/api/coupons/create")
        .then()
                .statusCode(200)
                .body("code", equalTo(2000));
    }

    static Stream<Arguments> createCouponArguments() {
        return Stream.of(
                Arguments.arguments(getCreateCouponJson("TEST_CODE", 1, "Polska"), 200, 2000),
                Arguments.arguments(getCreateCouponJson("", 1, "Polska"), 400, 4000),
                Arguments.arguments(getCreateCouponJson("TEST_CODE", 0, "Polska"), 400, 4000),
                Arguments.arguments(getCreateCouponJson("TEST_CODE", 1, ""), 400, 4000)
                );
    }

    static Stream<Arguments> useCouponArguments() {
        return Stream.of(
                Arguments.arguments(getUseCouponJson("TEST_CODE", "USER"), 200, 2000),
                Arguments.arguments(getUseCouponJson("", "USER"), 400, 4000),
                Arguments.arguments(getUseCouponJson("TEST_CODE", ""), 400, 4000)
        );
    }

    static String getCreateCouponJson(String code, int maxUsages, String country) {
        return "{" +
                "\"code\": \"" + code + "\", " +
                "\"maxUsages\": \"" + maxUsages + "\", " +
                "\"country\": \"" + country + "\"" +
                "}";
    }

    static String getUseCouponJson(String code, String user) {
        return "{" +
                "\"code\": \"" + code + "\", " +
                "\"user\": \"" + user + "\"" +
                "}";
    }
}
