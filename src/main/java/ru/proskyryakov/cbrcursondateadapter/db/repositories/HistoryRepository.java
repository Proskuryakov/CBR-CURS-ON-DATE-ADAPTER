package ru.proskyryakov.cbrcursondateadapter.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.proskyryakov.cbrcursondateadapter.db.entities.History;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

}
