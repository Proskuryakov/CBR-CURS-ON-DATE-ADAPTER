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

    public ValuteModel getValuteByCode(String code) {
        var valute = valuteRepository.getValuteByCode(code.toUpperCase());
        if (valute == null) return null;
        return valuteMapper.toValuteModel(valute);
    }

    public void deleteValuteByCode(String code) {
        valuteRepository.deleteByCode(code.toUpperCase());
    }

    public void updateValuteInterval(ValuteModel valuteModel) {
        var updatedValute =  valuteRepository.getValuteByCode(valuteModel.getCode());
        updatedValute.setInterval(valuteModel.getInterval());
        valuteRepository.save(updatedValute);
    }

    public void addValute(ValuteModel valuteModel) {
        var newValute = valuteMapper.fromValuteModel(valuteModel);
        valuteRepository.save(newValute);
    }
}
