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
import ru.proskyryakov.cbrcursondateadapter.adapter.models.CodeWithDates;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.CursOnDate;
import ru.proskyryakov.cbrcursondateadapter.adapter.services.CursService;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith({MockitoExtension.class})
class CursControllerTest {

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

        var date = parseDate(strDate);

        CursOnDate result = new CursOnDate();
        result.setCode(code);
        result.setCurs(new BigDecimal("28.5700"));
        result.setDate(date);

        when(cursService.getCursByCodeAndDate(code, strDate)).thenReturn(result);

        ResponseSpecification checkStatusCodeAndContentType =
                new ResponseSpecBuilder()
                        .expectStatusCode(200)
                        .expectContentType(ContentType.JSON)
                        .build();

        CursOnDate cursOnDate = RestAssuredMockMvc
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
                    .extract()
                    .as(CursOnDate.class);

        assertThat(cursOnDate.getCurs(), Matchers.equalTo(new BigDecimal("28.5700")));

    }

    @Test
    void getCursByDates() throws Exception {

        String requestBody = "{\n" +
                "    \"code\": \"usd\",\n" +
                "    \"dates\": [\n" +
                "        \"1997-08-02\",\n" +
                "        \"1998-08-02\"\n" +
                "    ]\n" +
                "}";

        CodeWithDates codeWithDates = new CodeWithDates();
        codeWithDates.setCode("usd");
        codeWithDates.setDates(Stream.of("1997-08-02", "1998-08-02").collect(Collectors.toList()));

        List<CursOnDate> result = getResult();
        when(cursService.getCursByDates(codeWithDates)).thenReturn(result);

        var curses = RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .config(RestAssuredMockMvcConfig.config()
                        .decoderConfig(DecoderConfig.decoderConfig().defaultContentCharset("UTF-8"))
                        .encoderConfig(EncoderConfig.encoderConfig().defaultContentCharset("UTF-8")))
                .body(requestBody)
                .when()
                .post("/curs")
                .then()
                .log()
                .body()
                .assertThat()
                .statusCode(200)
                .body("", Matchers.hasSize(2))
                .body("[0].code", Matchers.equalTo("USD"))
                .body("[1].code", Matchers.equalTo("USD"))
                .body("[0].date", Matchers.equalTo("1997-08-02"))
                .body("[1].date", Matchers.equalTo("1998-08-02"))
                .extract()
                .as(CursOnDate[].class);

        assertThat(curses[0].getCurs(), Matchers.equalTo(new BigDecimal("5801.0000")));
        assertThat(curses[1].getCurs(), Matchers.equalTo(new BigDecimal("6.2410")));
    }

    private List<CursOnDate> getResult() throws ParseException {
        CursOnDate cursOnDate1 = new CursOnDate();
        cursOnDate1.setCode("USD");
        cursOnDate1.setCurs(new BigDecimal("5801.0000"));
        cursOnDate1.setDate(parseDate("1997-08-02"));

        CursOnDate cursOnDate2 = new CursOnDate();
        cursOnDate2.setCode("USD");
        cursOnDate2.setCurs(new BigDecimal("6.2410"));
        cursOnDate2.setDate(parseDate("1998-08-02"));

        List<CursOnDate> list = new LinkedList<>();
        list.add(cursOnDate1);
        list.add(cursOnDate2);

        return list;
    }

    private Date parseDate(String strDate) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        var date = new GregorianCalendar();
        date.setTime(df.parse(strDate));
        date.add(Calendar.DATE, 1);

        return date.getTime();
    }

}