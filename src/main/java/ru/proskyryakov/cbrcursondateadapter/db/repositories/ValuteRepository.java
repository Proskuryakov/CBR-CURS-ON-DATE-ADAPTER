package ru.proskyryakov.cbrcursondateadapter.db.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.proskyryakov.cbrcursondateadapter.db.entities.Valute;

@Repository
public interface ValuteRepository extends JpaRepository<Valute, Long> {
}
