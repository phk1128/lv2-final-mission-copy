package finalmission.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

import finalmission.dto.LoginRequest;
import finalmission.dto.ReservationCreateRequest;
import finalmission.dto.ReservationUpdateRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {

    private String sessionId;

    @BeforeEach
    void setUp() {
        final LoginRequest loginRequest = new LoginRequest("test", "1234");
        sessionId = RestAssured.given().contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/login")
                .then()
                .extract().cookie("JSESSIONID");
    }

    @Nested
    class SuccessCase {
        @Test
        @DisplayName("예약 생성 성공 및 201 반환")
        void addReservationTest() {
            final ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(LocalDate.of(2025, 12, 24), 8, 10, 4);
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .sessionId(sessionId)
                    .body(reservationCreateRequest)
                    .when()
                    .post("/reservation")
                    .then().log().all()
                    .statusCode(HttpStatus.CREATED.value());
        }

        @Test
        @DisplayName("예약 조회 성공 및 200 반환")
        void getReservationsTest() {
            final ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(LocalDate.of(2025, 12, 24), 8, 10, 4);
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .sessionId(sessionId)
                    .body(reservationCreateRequest)
                    .when()
                    .post("/reservation")
                    .then()
                    .statusCode(HttpStatus.CREATED.value());

            RestAssured.given().log().all()
                    .sessionId(sessionId)
                    .when()
                    .get("/reservation")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("$", hasSize(greaterThanOrEqualTo(1)))
                    .body("email", everyItem(equalTo("test")))
                    .body("numberOfPeople", hasItem(4));
        }

        @Test
        @DisplayName("예약 업데이트 성공 및 201 반환")
        void updateReservationTest() {
            final ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(LocalDate.of(2025, 12, 24), 8, 10, 4);
            final int reservationId = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .sessionId(sessionId)
                    .body(reservationCreateRequest)
                    .when()
                    .post("/reservation")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .path("id");


            final ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(
                    LocalDate.of(2025, 12, 26),
                    10,
                    12,
                    3
            );

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .sessionId(sessionId)
                    .body(updateRequest)
                    .when()
                    .patch("/reservation/" + reservationId)
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(reservationId))
                    .body("email", equalTo("test"))
                    .body("startTime", equalTo(10))
                    .body("endTime", equalTo(12))
                    .body("numberOfPeople", equalTo(3));
        }

        @Test
        @DisplayName("예약 삭제 성공 및 204 반환")
        void deleteReservationTest() {
            final ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(LocalDate.of(2025, 12, 24), 8, 10, 4);
            final int reservationId = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .sessionId(sessionId)
                    .body(reservationCreateRequest)
                    .when()
                    .post("/reservation")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .path("id");

            RestAssured.given().log().all()
                    .sessionId(sessionId)
                    .when()
                    .delete("/reservation/" + reservationId)
                    .then().log().all()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        }
    }

    @Nested
    class FailureCase {

        @Test
        @DisplayName("공휴일은 예약이 실패 및 400 반환")
        void addReservationTest() {
            final ReservationCreateRequest reservationCreateRequest = new ReservationCreateRequest(LocalDate.of(2025, 12, 25), 8, 10, 4);
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .sessionId(sessionId)
                    .body(reservationCreateRequest)
                    .when()
                    .post("/reservation")
                    .then().log().all()
                    .statusCode(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("내 예약 아닌거 삭제 및 405 반환")
        void deleteReservationTest() {
            RestAssured.given().log().all()
                    .sessionId(sessionId)
                    .when()
                    .delete("/reservation/" + 2)
                    .then().log().all()
                    .statusCode(HttpStatus.FORBIDDEN.value());
        }
    }

}
