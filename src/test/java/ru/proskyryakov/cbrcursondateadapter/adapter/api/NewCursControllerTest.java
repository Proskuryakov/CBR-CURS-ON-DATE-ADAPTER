package ru.proskyryakov.cbrcursondateadapter.adapter.api;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import ru.proskyryakov.cbrcursondateadapter.adapter.services.CursService;

import static io.restassured.RestAssured.given;
class NewCursControllerTest {

    @MockBean
    private CursService cursService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void getCursByCode() {

        given().log().body()
                .contentType("application/json")
                .pathParam("code", "USD")
                .when().get("/curs/{code}")
                .then().log().body()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void getCursByCodeAndDate() {
    }

    @Test
    void getCursByDates() {
    }
}