package ru.proskyryakov.cbrcursondateadapter.db.repositories;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.proskyryakov.cbrcursondateadapter.db.entities.History;
import ru.proskyryakov.cbrcursondateadapter.db.entities.Interval;
import ru.proskyryakov.cbrcursondateadapter.db.entities.Valute;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest()
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@ContextConfiguration(initializers = {AllRepositoryTest.Initializer.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AllRepositoryTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private IntervalRepository intervalRepository;

    @Autowired
    private ValuteRepository valuteRepository;

    private History firstHistory;
    private History secondHistory;
    private Interval actualInterval;
    private Interval notActualInterval;
    private Valute valute;

    @Test
    @DisplayName("PostgreSQL container test")
    void PostgreSQLContainerTest(){
        assertTrue(postgreSQLContainer.isRunning());
    }

    @Test
    @DisplayName("Context test")
    void contextTest(){
        assertNotNull(valuteRepository);
        assertNotNull(intervalRepository);
        assertNotNull(historyRepository);
    }

    @BeforeEach
    void init(){
        valute = valuteRepository.findValuteByCode("AUD");

        actualInterval = new Interval();
        actualInterval.setInterval(60);
        actualInterval.setIsActual(true);
        actualInterval.setValute(valute);

        notActualInterval = new Interval();
        notActualInterval.setInterval(120);
        notActualInterval.setIsActual(false);
        notActualInterval.setValute(valute);

        firstHistory = new History();
        firstHistory.setCurse(new BigDecimal("100.001"));
        firstHistory.setDatetime(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        firstHistory.setInterval(actualInterval);

        secondHistory = new History();
        secondHistory.setCurse(new BigDecimal("200.002"));
        secondHistory.setDatetime(new GregorianCalendar(2020, Calendar.JULY, 1).getTime());
        secondHistory.setInterval(actualInterval);
    }

    @Test
    @DisplayName("Get valute by correct char code")
    void test_0001() {
        var v = valuteRepository.findValuteByCode("AUD");
        assertNotNull(v);
        assertEquals(valute, v);
    }

    @Test
    @DisplayName("Get valute by !correct char code")
    void test_0002() {
        assertNull(valuteRepository.findValuteByCode("AUSTRAILA"));
    }

    @Test
    @DisplayName("Add new intervals to db")
    void test_0003() {
        actualInterval = intervalRepository.save(actualInterval);
        assertNotNull(actualInterval);

        notActualInterval = intervalRepository.save(notActualInterval);
        assertNotNull(notActualInterval);
    }

    @Test
    @DisplayName("Search not tracked interval")
    void test_0004() {
        actualInterval = intervalRepository.save(actualInterval);
        notActualInterval = intervalRepository.save(notActualInterval);

        List<Interval> intervalList = intervalRepository.findNewIntervals();

        assertEquals(intervalList.size(), 1);
        assertEquals(intervalList.get(0), actualInterval);
    }

    @Test
    @DisplayName("Find all actual intervals")
    void test_0005() {
        actualInterval = intervalRepository.save(actualInterval);
        notActualInterval = intervalRepository.save(notActualInterval);

        List<Interval> intervalList = intervalRepository.findAllByIsActualTrue();

        assertEquals(intervalList.size(), 3);
        assertEquals(intervalList.get(2), actualInterval);
    }

    @Test
    @DisplayName("Find actual interval by char code")
    void test_0006() {
        actualInterval = intervalRepository.save(actualInterval);
        notActualInterval = intervalRepository.save(notActualInterval);

        Interval interval = intervalRepository.findIntervalByIsActualTrueAndValute_Code("AUD");

        assertEquals(interval, actualInterval);
    }

    @Test
    @DisplayName("Add new data in history table")
    void test_0007() {
        actualInterval = intervalRepository.save(actualInterval);
        notActualInterval = intervalRepository.save(notActualInterval);
        firstHistory = historyRepository.save(firstHistory);
        secondHistory = historyRepository.save(secondHistory);

        assertNotNull(firstHistory);
        assertNotNull(secondHistory);
    }

    @Test
    @DisplayName("Find all last entries in history db")
    void test_0008() {
        actualInterval = intervalRepository.save(actualInterval);
        notActualInterval = intervalRepository.save(notActualInterval);
        firstHistory = historyRepository.save(firstHistory);
        secondHistory = historyRepository.save(secondHistory);

        List<History> histories = historyRepository.findAllLastEntry();

        assertEquals(histories.size(), 3);

        assertEquals(histories.get(2), secondHistory);
    }

    @Test
    @DisplayName("Find history by code and dates")
    void test_0009() {
        actualInterval = intervalRepository.save(actualInterval);
        notActualInterval = intervalRepository.save(notActualInterval);
        firstHistory = historyRepository.save(firstHistory);
        secondHistory = historyRepository.save(secondHistory);

        List<History> histories = historyRepository.findAllByInterval_Valute_CodeAndAndDatetimeBetweenOrderByDatetime(
                "AUD",
                new Date(0),
                new Date()
        );

        assertEquals(histories.size(), 2);

        assertEquals(histories.get(0), firstHistory);
        assertEquals(histories.get(1), secondHistory);
    }

}