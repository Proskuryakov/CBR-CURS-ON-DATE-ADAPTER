package ru.proskyryakov.cbrcursondateadapter.adapter.api;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.proskyryakov.cbrcursondateadapter.adapter.exceptions.AlreadyExistsException;
import ru.proskyryakov.cbrcursondateadapter.adapter.exceptions.NotFoundException;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.*;
import ru.proskyryakov.cbrcursondateadapter.adapter.services.CursHistoryService;
import ru.proskyryakov.cbrcursondateadapter.adapter.services.CursService;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith({MockitoExtension.class})
public class CursHistoryControllerTest {

    @Mock
    private CursHistoryService cursHistoryService;

    @InjectMocks
    private CursHistoryController cursHistoryController;

    private IntervalModel intervalModel;

    @BeforeEach
    public void setUp() {
        cursHistoryController = new CursHistoryController(cursHistoryService);
        RestAssuredMockMvc.standaloneSetup(cursHistoryController);

        intervalModel = new IntervalModel(1L, 1L, "USD", 360, true);

    }

    @Test
    public void getAllValute() {
        List<ValuteModel> valuteModels = Stream.of(
                new ValuteModel(1L,"AUD","Австралийский доллар"),
                new ValuteModel(2L,"AZN","Азербайджанский манат")
        ).collect(Collectors.toList());

        Mockito.when(cursHistoryService.getAllValute()).thenReturn(valuteModels);

        RestAssuredMockMvc
                .given().contentType("application/json")
                .when()
                .get("/history/valutes")
                .then()
                .statusCode(200)
                .body("", Matchers.hasSize(2))
                .body("[0].code", Matchers.equalTo("AUD"))
                .body("[0].name", Matchers.equalTo("Австралийский доллар"))
                .body("[1].code", Matchers.equalTo("AZN"))
                .body("[1].name", Matchers.equalTo("Азербайджанский манат"))
        ;
    }

    @Test
    public void getAllInterval() {
        List<IntervalModel> intervalModels = Stream.of(
                new IntervalModel(1L, 1L, "USD", 360, true),
                new IntervalModel(2L, 1L, "USD", 60, false)
        ).collect(Collectors.toList());

        Mockito.when(cursHistoryService.getAllInterval()).thenReturn(intervalModels);

        RestAssuredMockMvc
                .given().contentType("application/json")
                .when()
                .get("/history/intervals/all")
                .then()
                .statusCode(200)
                .body("", Matchers.hasSize(2))
                .body("[0].code", Matchers.equalTo("USD"))
                .body("[0].valuteId", Matchers.equalTo(1))
                .body("[0].interval", Matchers.equalTo(360))
                .body("[0].isActual", Matchers.equalTo(true))
                .body("[1].isActual", Matchers.equalTo(false))
        ;

    }

    @Test
    public void getActualInterval() {
        List<IntervalModel> intervalModels = Stream.of(
                new IntervalModel(3L, 1L, "USD", 150, true)
        ).collect(Collectors.toList());

        Mockito.when(cursHistoryService.getActualInterval()).thenReturn(intervalModels);

        RestAssuredMockMvc
                .given().contentType("application/json")
                .when()
                .get("/history/intervals")
                .then()
                .statusCode(200)
                .body("", Matchers.hasSize(1))
                .body("[0].code", Matchers.equalTo("USD"))
                .body("[0].valuteId", Matchers.equalTo(1))
                .body("[0].interval", Matchers.equalTo(150))
                .body("[0].isActual", Matchers.equalTo(true))
        ;

    }

    @Test
    public void getIntervalByCode() {
        String code = "USD";

        Mockito.when(cursHistoryService.getIntervalByCode(code)).thenReturn(intervalModel);

        RestAssuredMockMvc
                .given().contentType("application/json")
                .when()
                .get("/history/interval/" + code)
                .then()
                .statusCode(200)
                .body("code", Matchers.equalTo("USD"))
                .body("valuteId", Matchers.equalTo(1))
                .body("interval", Matchers.equalTo(360))
                .body("isActual", Matchers.equalTo(true))
        ;

    }

    @Test
    public void getHistoryByCodeAndDates() {
        String code = "USD";

        HistoryDateRequest request = new HistoryDateRequest();
        request.setFrom(new GregorianCalendar(2021, Calendar.JANUARY, 1, 3, 0).getTime());
        request.setTo(new GregorianCalendar(2021, Calendar.DECEMBER, 31, 3, 0).getTime());

        String requestJson =
                "{\n" +
                "    \"from\": \"2021-01-01\",\n" +
                "    \"to\": \"2021-12-31\"\n" +
                "}";

        CodeHistoryModel codeHistoryModel = getCodeHistoryModel(code);

        Mockito.when(cursHistoryService.getHistoryByCodeAndDates(code, request))
                .thenReturn(codeHistoryModel);

        var response = RestAssuredMockMvc
                .given().contentType("application/json")
                .body(requestJson)
                .when()
                .post("/history/" + code)
                .then()
                .statusCode(200)
                .extract()
                .as(CodeHistoryModel.class);

        assertThat(response, Matchers.equalTo(codeHistoryModel));
    }

    @Test
    public void getHistoryByCodesAndDates() {
        HistoryDateRequest dates = new HistoryDateRequest();
        dates.setFrom(new GregorianCalendar(2021, Calendar.JANUARY, 1, 3, 0).getTime());
        dates.setTo(new GregorianCalendar(2021, Calendar.DECEMBER, 31, 3, 0).getTime());

        CodesDatesHistoryRequest request = new CodesDatesHistoryRequest();
        request.setDates(dates);
        request.setCodes(List.of("USD", "BYN"));

        String requestJson =
                "{\n" +
                        "    \"codes\": [\"USD\", \"BYN\"],\n" +
                        "    \"dates\":\n" +
                        "    {\n" +
                        "        \"from\": \"2021-01-01\",\n" +
                        "        \"to\": \"2021-12-31\"\n" +
                        "    }\n" +
                        "}";

        List<CodeHistoryModel> codeHistoryModels = List.of(
                getCodeHistoryModel("USD"),
                getCodeHistoryModel("BYN")
        );

        Mockito.when(cursHistoryService.getHistoryByCodesAndDates(request)).thenReturn(codeHistoryModels);

        var response = RestAssuredMockMvc
                .given().contentType("application/json")
                .body(requestJson)
                .when()
                .post("/history")
                .then()
                .statusCode(200)
                .extract()
                .as(CodeHistoryModel[].class);

        assertThat(response, Matchers.hasItemInArray(codeHistoryModels.get(0)));
        assertThat(response, Matchers.hasItemInArray(codeHistoryModels.get(1)));

    }

    private CodeHistoryModel getCodeHistoryModel(String code) {
        CodeHistoryModel codeHistoryModel = new CodeHistoryModel();
        codeHistoryModel.setCode(code);
        codeHistoryModel.setHistory(getHistoryModels());

        return codeHistoryModel;
    }

    private List<HistoryModel> getHistoryModels() {
        return List.of(
                new HistoryModel(
                        new BigDecimal("100.001"),
                        new GregorianCalendar(2001, Calendar.JULY, 21).getTime()
                ),
                new HistoryModel(
                        new BigDecimal("200.002"),
                        new GregorianCalendar(2001, Calendar.AUGUST, 21).getTime()
                )
        );
    }

}