package ru.proskyryakov.cbrcursondateadapter.adapter.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.proskyryakov.cbrcursondateadapter.adapter.exceptions.AlreadyExistsException;
import ru.proskyryakov.cbrcursondateadapter.adapter.exceptions.NotFoundException;
import ru.proskyryakov.cbrcursondateadapter.adapter.mappers.ValuteIntervalMapper;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.*;
import ru.proskyryakov.cbrcursondateadapter.db.entities.Interval;
import ru.proskyryakov.cbrcursondateadapter.db.entities.Valute;
import ru.proskyryakov.cbrcursondateadapter.db.repositories.HistoryRepository;
import ru.proskyryakov.cbrcursondateadapter.db.repositories.IntervalRepository;
import ru.proskyryakov.cbrcursondateadapter.db.repositories.ValuteRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CursHistoryService {

    private final ValuteRepository valuteRepository;
    private final IntervalRepository intervalRepository;
    private final HistoryRepository historyRepository;
    private final ValuteIntervalMapper valuteIntervalMapper;

    public List<ValuteModel> getAllValute() {
        var valutes = valuteRepository.findAll();
        return valuteIntervalMapper.toValuteModels(valutes);
    }

    public List<IntervalModel> getAllInterval() {
        var intervals = intervalRepository.findAll();
        return valuteIntervalMapper.toIntervalModels(intervals);
    }

    public List<IntervalModel> getActualInterval() {
        List<Interval> intervals = intervalRepository.findAllByIsActualTrue();
        return valuteIntervalMapper.toIntervalModels(intervals);
    }

    @SneakyThrows
    public IntervalModel getIntervalByCode(@NonNull String code) {
        Interval interval = intervalRepository.findIntervalByIsActualTrueAndValute_Code(code.toUpperCase());
        if(interval == null) throw new NotFoundException(String.format("Interval with code %s not found", code));
        return valuteIntervalMapper.toIntervalModel(interval);
    }

    @SneakyThrows
    public void deleteIntervalByCode(String code) {
        try {
            var interval = intervalRepository.findIntervalByIsActualTrueAndValute_Code(code.toUpperCase());
            interval.setIsActual(false);
            intervalRepository.save(interval);
        } catch (Exception e) {
            throw new NotFoundException("Nothing found by code " + code);
        }

    }

    @SneakyThrows
    public IntervalModel updateInterval(IntervalModel intervalModel) {
        Interval interval = intervalRepository.findIntervalByIsActualTrueAndValute_Code(intervalModel.getCode());
        if(interval == null) throw new NotFoundException(
                String.format("Interval with code %s not found", intervalModel.getCode())
        );
        interval.setInterval(intervalModel.getInterval());
        Interval updatedInterval = intervalRepository.save(interval);
        return valuteIntervalMapper.toIntervalModel(updatedInterval);
    }

    @SneakyThrows
    public IntervalModel addInterval(IntervalModel intervalModel) {
        Valute valute = valuteRepository.findValuteByCode(intervalModel.getCode());
        if(valute == null) throw new NotFoundException(
                String.format("Valute with code %s does not exist", intervalModel.getCode())
        );
        Interval interval = valuteIntervalMapper.fromIntervalModel(intervalModel, valute);
        try {
            var createdInterval = intervalRepository.save(interval);
            return valuteIntervalMapper.toIntervalModel(createdInterval);
        } catch (Exception e) {
            throw new AlreadyExistsException(
                    String.format("Interval with code %s already exists", intervalModel.getCode())
            );
        }
    }

    public CodeHistoryModel getHistoryByCodeAndDates(String code, HistoryDateRequest historyDateRequest) {
        checkDates(historyDateRequest);

        var history = historyRepository.findAllByInterval_Valute_CodeAndAndDatetimeBetweenOrderByDatetime(
                code.toUpperCase(),
                historyDateRequest.getFrom(),
                addDay(historyDateRequest.getTo())
        );

        CodeHistoryModel codeHistoryModel = new CodeHistoryModel();
        codeHistoryModel.setCode(code.toUpperCase());
        codeHistoryModel.setHistory(
                history.stream()
                        .map(h -> new HistoryModel(h.getCurse(), h.getDatetime()))
                        .collect(Collectors.toList())
        );

        return codeHistoryModel;
    }

    public List<CodeHistoryModel> getHistoryByCodesAndDates(CodesDatesHistoryRequest request) {
        return request.getCodes().stream()
                .map(code -> getHistoryByCodeAndDates(code, request.getDates()))
                .collect(Collectors.toList());
    }

    private Date addDay(Date date){
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.DATE, 1);
        return instance.getTime();
    }

    private void checkDates(HistoryDateRequest request) {
        if (request.getFrom() == null) {
            request.setFrom(new Date(0));
        }

        if (request.getTo() == null) {
            request.setTo(new Date());
        }
    }
}
