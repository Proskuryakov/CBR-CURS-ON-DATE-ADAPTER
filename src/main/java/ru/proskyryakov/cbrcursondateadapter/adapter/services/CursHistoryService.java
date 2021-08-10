package ru.proskyryakov.cbrcursondateadapter.adapter.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.proskyryakov.cbrcursondateadapter.adapter.mappers.ValuteMapper;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.ValuteModel;
import ru.proskyryakov.cbrcursondateadapter.db.repositories.ValuteRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CursHistoryService {

    private final ValuteRepository valuteRepository;
    private final ValuteMapper valuteMapper;

    public List<ValuteModel> getAllValutes() {
        var valutes = valuteRepository.findAll();
        return valuteMapper.toValuteModels(valutes);
    }

}
