package ru.proskyryakov.cbrcursondateadapter.adapter.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.CodeWithDates;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.CursOnDate;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CursControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getCursByCodeUsd(){
        ResponseEntity<CursOnDate> response = restTemplate.getForEntity("/curs/usd", CursOnDate.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), notNullValue());

        String responseDate = new SimpleDateFormat("yyyy-MM-dd").format(response.getBody().getDate());
        String nowDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        assertThat(responseDate, is(nowDate));
        assertThat(response.getBody().getCode(), is("USD"));
    }

    @Test
    void getCursByUncorrectCode(){
        ResponseEntity<CursOnDate> response = restTemplate.getForEntity("/curs/DOLLAR", CursOnDate.class);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void getCursByCodeUsdAndDate2000_01_17() {
        ResponseEntity<CursOnDate> response = restTemplate.getForEntity("/curs/usd/date/2000-01-17", CursOnDate.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), notNullValue());

        assertThat(response.getBody().getCode(), is("USD"));
        assertThat(response.getBody().getCurs(), is(new BigDecimal("28.5700")));

        String responseDate = new SimpleDateFormat("yyyy-MM-dd").format(response.getBody().getDate());
        assertThat(responseDate, is("2000-01-17"));
    }

    @Test
    void getCursByCodeUsdAndUncorrectDate() {
        ResponseEntity<CursOnDate> response = restTemplate.getForEntity("/curs/usd/date/2000-01", CursOnDate.class);
        assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    void getCursByDates() throws JsonProcessingException {
        HttpEntity<CodeWithDates> codeWithDatesHttpEntity = getRequest();
        ResponseEntity<List<CursOnDate>> response = restTemplate.exchange(
                "/curs",
                HttpMethod.POST,
                codeWithDatesHttpEntity,
                new ParameterizedTypeReference<List<CursOnDate>>() {}
        );

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), notNullValue());

        List<CursOnDate> curses = response.getBody();
        assertThat(curses, hasSize(2));
        assertEquals(curses, getCorrectResponse());
    }

    private HttpEntity<CodeWithDates> getRequest(){
        CodeWithDates codeWithDates = new CodeWithDates();
        codeWithDates.setCode("USD");
        codeWithDates.setDates(
                Stream.of("1997-08-02", "1998-08-02").collect(Collectors.toList())
        );

        return new HttpEntity<>(codeWithDates);
    }

    private List<CursOnDate> getCorrectResponse() throws JsonProcessingException {
        List<CursOnDate> correctResponse = objectMapper.readValue(
                "[{\n" +
                        "   \"code\": \"USD\",\n" +
                        "   \"curs\": 5801.0000,\n" +
                        "   \"date\": \"1997-08-02\"\n" +
                        "},\n" +
                        "{\n" +
                        "   \"code\": \"USD\",\n" +
                        "   \"curs\": 6.2410,\n" +
                        "   \"date\": \"1998-08-02\"\n" +
                        "}]",  new TypeReference<List<CursOnDate>>(){}
        );

        return correctResponse;
    }
}