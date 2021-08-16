package ru.proskyryakov.cbrcursondateadapter.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.proskyryakov.cbrcursondateadapter.db.entities.Interval;

import java.util.List;

public interface IntervalRepository extends JpaRepository<Interval, Long> {

    Interval findIntervalByValute_Code(String code);

    Interval findIntervalByIsActualTrueAndValute_Code(String code);

    List<Interval> findAllByIsActualTrue();

    @Query(
            nativeQuery = true,
            value = "select i.* from intervals i left " +
                    "join history h on i.id = h.interval_id where h.id is null and i.is_actual"
    )
    List<Interval> findNewIntervals();

}
