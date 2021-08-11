package ru.proskyryakov.cbrcursondateadapter.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.proskyryakov.cbrcursondateadapter.db.entities.Interval;

public interface IntervalRepository extends JpaRepository<Interval, Long> {

    Interval findIntervalByValute_Code(String code);

}
