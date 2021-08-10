package ru.proskyryakov.cbrcursondateadapter.adapter.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.ValuteModel;
import ru.proskyryakov.cbrcursondateadapter.db.entities.Valute;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ValuteMapper {

    @Mapping(target = "code", source = "valute.code")
    @Mapping(target = "interval", source = "valute.interval")
    ValuteModel toValuteModel(Valute valute);

    List<ValuteModel> toValuteModels(List<Valute> valutes);

    @Mapping(target="code", source="valuteModel.code")
    @Mapping(target="interval", source="valuteModel.interval")
    Valute fromValuteModel(ValuteModel valuteModel);

    List<Valute> fromValuteModels(List<ValuteModel> valuteModels);

}
