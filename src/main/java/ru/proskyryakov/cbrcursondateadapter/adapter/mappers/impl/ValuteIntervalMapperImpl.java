package ru.proskyryakov.cbrcursondateadapter.adapter.mappers.impl;

import org.springframework.stereotype.Component;
import ru.proskyryakov.cbrcursondateadapter.adapter.mappers.ValuteIntervalMapper;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.IntervalModel;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.ValuteModel;
import ru.proskyryakov.cbrcursondateadapter.db.entities.Interval;
import ru.proskyryakov.cbrcursondateadapter.db.entities.Valute;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ValuteIntervalMapperImpl implements ValuteIntervalMapper {
    @Override
    public ValuteModel toValuteModel(Valute valute) {
        ValuteModel valuteModel = new ValuteModel();
        valuteModel.setCode(valute.getCode());
        valuteModel.setId(valute.getId());
        valuteModel.setName(valute.getName());
        return valuteModel;
    }

    @Override
    public List<ValuteModel> toValuteModels(List<Valute> valutes) {
        return valutes.stream().map(this::toValuteModel).collect(Collectors.toList());
    }

    @Override
    public IntervalModel toIntervalModel(Interval interval) {
        return new IntervalModel(
                interval.getId(),
                interval.getValute().getId(),
                interval.getValute().getCode(),
                interval.getInterval(),
                interval.getIsActual()
        );
    }

    @Override
    public List<IntervalModel> toIntervalModels(List<Interval> intervals) {
        return intervals.stream().map(this::toIntervalModel).collect(Collectors.toList());
    }

    @Override
    public Interval fromIntervalModel(IntervalModel intervalModel, Valute valute) {
        var interval = new Interval();
        interval.setInterval(intervalModel.getInterval());
        interval.setValute(valute);
        interval.setId(null);
        interval.setIsActual(true);
        return interval;
    }
}
