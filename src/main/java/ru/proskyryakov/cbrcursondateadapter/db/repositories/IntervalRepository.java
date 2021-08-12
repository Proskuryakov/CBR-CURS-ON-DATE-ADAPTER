package ru.proskyryakov.cbrcursondateadapter.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.proskyryakov.cbrcursondateadapter.db.entities.Interval;

import java.util.List;

public interface IntervalRepository extends JpaRepository<Interval, Long> {

    Interval findIntervalByValute_Code(String code);

    Interval findIntervalByIsActualTrueAndValute_Code(String code);

    List<Interval> findAllByIsActualTrue();
}
