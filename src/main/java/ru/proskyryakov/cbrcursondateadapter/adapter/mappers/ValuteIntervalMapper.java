package ru.proskyryakov.cbrcursondateadapter.adapter.mappers;


import ru.proskyryakov.cbrcursondateadapter.adapter.models.IntervalModel;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.ValuteModel;
import ru.proskyryakov.cbrcursondateadapter.db.entities.Interval;
import ru.proskyryakov.cbrcursondateadapter.db.entities.Valute;

import java.util.List;

public interface ValuteIntervalMapper {

    ValuteModel toValuteModel(Valute valute);

    List<ValuteModel> toValuteModels(List<Valute> valutes);

    IntervalModel toIntervalModel(Interval interval);

    List<IntervalModel> toIntervalModels(List<Interval> intervals);

    Interval fromIntervalModel(IntervalModel intervalModel, Valute valute);
}
