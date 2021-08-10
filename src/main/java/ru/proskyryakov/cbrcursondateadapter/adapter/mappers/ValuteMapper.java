package ru.proskyryakov.cbrcursondateadapter.adapter.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.ValuteModel;
import ru.proskyryakov.cbrcursondateadapter.db.entities.Valute;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ValuteMapper {

    ValuteModel toValuteModel(Valute valute);

    List<ValuteModel> toValuteModels(List<Valute> valutes);

    Valute fromValuteModel(ValuteModel valuteModel);

    List<Valute> fromValuteModels(List<ValuteModel> valuteModels);

}
