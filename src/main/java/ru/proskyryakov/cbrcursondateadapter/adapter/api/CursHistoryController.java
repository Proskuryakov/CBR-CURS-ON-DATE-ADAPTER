package ru.proskyryakov.cbrcursondateadapter.adapter.api;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import ru.proskyryakov.cbrcursondateadapter.adapter.exceptions.NotFoundException;
import ru.proskyryakov.cbrcursondateadapter.adapter.logging.RabbitLogging;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.*;
import ru.proskyryakov.cbrcursondateadapter.adapter.services.CursHistoryService;

import java.util.List;

@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
public class CursHistoryController {

    private final CursHistoryService cursHistoryService;

    @RabbitLogging
    @GetMapping("/valutes")
    public List<ValuteModel> getAllValute() {
        return cursHistoryService.getAllValute();
    }

    @RabbitLogging
    @GetMapping("/intervals/all")
    public List<IntervalModel> getAllInterval() {
        return cursHistoryService.getAllInterval();
    }

    @RabbitLogging
    @GetMapping("/intervals")
    public List<IntervalModel> getActualInterval() {
        return cursHistoryService.getActualInterval();
    }

    @RabbitLogging
    @GetMapping("/interval/{code}")
    public IntervalModel getIntervalByCode(@PathVariable("code") String code) {
        return cursHistoryService.getIntervalByCode(code);
    }

    @RabbitLogging
    @DeleteMapping("/interval/{code}")
    public void deleteIntervalByCode(@PathVariable("code") String code) {
        cursHistoryService.deleteIntervalByCode(code);
    }

    @RabbitLogging
    @PutMapping("/interval")
    public IntervalModel updateInterval(@RequestBody IntervalModel intervalModel) {
        return cursHistoryService.updateInterval(intervalModel);
    }

    @RabbitLogging
    @PostMapping("/interval")
    public IntervalModel addInterval(@RequestBody IntervalModel intervalModel) {
        return cursHistoryService.addInterval(intervalModel);
    }

    @RabbitLogging
    @SneakyThrows
    @PostMapping("/{code}")
    public CodeHistoryModel getHistoryByCodeAndDates(
            @PathVariable("code") String code,
            @RequestBody HistoryDateRequest historyDateRequest
    ) {
        var codeHistoryModel = cursHistoryService.getHistoryByCodeAndDates(code, historyDateRequest);
        if (codeHistoryModel.getHistory().isEmpty()) {
            throw new NotFoundException("History not found");
        }
        return codeHistoryModel;
    }

    @RabbitLogging
    @PostMapping
    public List<CodeHistoryModel> getHistoryByCodesAndDates(@RequestBody CodesDatesHistoryRequest request) {
        return cursHistoryService.getHistoryByCodesAndDates(request);
    }


}
