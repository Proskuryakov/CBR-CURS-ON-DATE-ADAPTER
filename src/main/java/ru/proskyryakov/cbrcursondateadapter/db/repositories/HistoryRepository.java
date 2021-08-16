package ru.proskyryakov.cbrcursondateadapter.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.proskyryakov.cbrcursondateadapter.db.entities.History;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    @Query(
            nativeQuery = true,
            value = "select h.* from history h, " +
                    "(select interval_id, max(datetime) as datetime from history " +
                    "    join intervals i on i.id = interval_id where i.is_actual group by interval_id) as nh " +
                    "where h.interval_id = nh.interval_id and h.datetime = nh.datetime"
    )
    List<History> findAllLastEntry();

}
