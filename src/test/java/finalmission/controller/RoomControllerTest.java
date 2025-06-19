package finalmission.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

import finalmission.dto.LoginRequest;
import finalmission.dto.RoomCreateRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class RoomControllerTest {

    private String adminSessionId;
    private String userSessionId;

    @BeforeEach
    void setUp() {
        final LoginRequest adminLoginRequest = new LoginRequest("admin@email.com", "1234");
        adminSessionId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(adminLoginRequest)
                .when()
                .post("/login")
                .then()
                .extract().cookie("JSESSIONID");

        final LoginRequest userLoginRequest = new LoginRequest("test@email.com", "1234");
        userSessionId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(userLoginRequest)
                .when()
                .post("/login")
                .then()
                .extract().cookie("JSESSIONID");
    }

    @Nested
    class SuccessCase {
        @Test
        @DisplayName("어드민 회의실 생성 성공 및 201 반환")
        void addRoomTest() {
            final RoomCreateRequest request = new RoomCreateRequest("회의실 Z", 10);
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .sessionId(adminSessionId)
                    .body(request)
                    .when()
                    .post("/admin/room")
                    .then()
                    .log().all()
                    .body("maxNumberOfPeople", equalTo(request.maxNumberOfPeople()))
                    .body("name", equalTo(request.name()))
                    .statusCode(HttpStatus.CREATED.value());
        }
    }

    @Nested
    class FailureCase {
        @Test
        @DisplayName("유저는 회의실 생성 불가 및 401 반한")
        void addRoomTest() {
            final RoomCreateRequest request = new RoomCreateRequest("회의실 Z", 9);
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .sessionId(userSessionId)
                    .body(request)
                    .when()
                    .post("/admin/room")
                    .then().log().all()
                    .statusCode(HttpStatus.UNAUTHORIZED.value());
        }
    }

}
