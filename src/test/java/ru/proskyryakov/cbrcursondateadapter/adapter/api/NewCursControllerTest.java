package ru.proskyryakov.cbrcursondateadapter.adapter.api;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.DecoderConfig;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.CursOnDate;
import ru.proskyryakov.cbrcursondateadapter.adapter.services.CursService;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.mockito.Mockito.when;


@ExtendWith({MockitoExtension.class})
class NewCursControllerTest {

    @Mock
    private CursService cursService;

    @InjectMocks
    private CursController cursController;

    @BeforeEach
    void setUp() {
        cursController = new CursController(cursService);
        RestAssuredMockMvc.standaloneSetup(cursController);
    }

    @Test
    void getCursByCodeAndDate() throws Exception {

        String code = "USD";
        String strDate = "2000-01-17";

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        var date = new GregorianCalendar();
        date.setTime(df.parse(strDate));
        date.add(Calendar.DATE, 1);

        CursOnDate result = new CursOnDate();
        result.setCode(code);
        result.setCurs(new BigDecimal("28.5700"));
        result.setDate(date.getTime());

        when(cursService.getCursByCodeAndDate(code, strDate)).thenReturn(result);

        ResponseSpecification checkStatusCodeAndContentType =
                new ResponseSpecBuilder()
                        .expectStatusCode(200)
                        .expectContentType(ContentType.JSON)
                        .build();

        RestAssuredMockMvc
                .given().log().body().contentType("application/json")
                .when()
                    .get("/curs/" + code + "/date/" + strDate)
                .then()
                    .log()
                    .body()
                    .assertThat()
                    .spec(checkStatusCodeAndContentType)
                    .assertThat()
                    .body("code", Matchers.equalTo("USD"))
                    .body("date", Matchers.equalTo("2000-01-17"))
        ;
    }

    @Test
    void getCursByDates() {

        String requestBody = "{\n" +
                "    \"code\": \"usd\",\n" +
                "    \"dates\": [\n" +
                "        \"1997-08-02\",\n" +
                "        \"1998-08-02\"\n" +
                "    ]\n" +
                "}";

        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .config(RestAssuredMockMvcConfig.config()
                        .decoderConfig(DecoderConfig.decoderConfig().defaultContentCharset("UTF-8"))
                        .encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset("UTF-8")))
                .body(requestBody)
                .when()
                .post("/curs")
                .then()
                .assertThat()
                .statusCode(200);
    }

}