package ru.proskyryakov.cbrcursondateadapter.adapter.api;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import ru.proskyryakov.cbrcursondateadapter.adapter.exceptions.NotFoundException;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.*;
import ru.proskyryakov.cbrcursondateadapter.adapter.services.CursHistoryService;

import java.util.List;

@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
public class CursHistoryController {

    private final CursHistoryService cursHistoryService;

    @GetMapping("/valutes")
    public List<ValuteModel> getAllValute() {
        return cursHistoryService.getAllValute();
    }

    @GetMapping("/intervals/all")
    public List<IntervalModel> getAllInterval() {
        return cursHistoryService.getAllInterval();
    }

    @GetMapping("/intervals")
    public List<IntervalModel> getActualInterval() {
        return cursHistoryService.getActualInterval();
    }

    @GetMapping("/interval/{code}")
    public IntervalModel getIntervalByCode(@PathVariable("code") String code) {
        return cursHistoryService.getIntervalByCode(code);
    }

    @DeleteMapping("/interval/{code}")
    public void deleteIntervalByCode(@PathVariable("code") String code) {
        cursHistoryService.deleteIntervalByCode(code);
    }

    @PutMapping("/interval")
    public IntervalModel updateInterval(@RequestBody IntervalModel intervalModel) {
        return cursHistoryService.updateInterval(intervalModel);
    }

    @PostMapping("/interval")
    public IntervalModel addInterval(@RequestBody IntervalModel intervalModel) {
        return cursHistoryService.addInterval(intervalModel);
    }

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

    @PostMapping
    public List<CodeHistoryModel> getHistoryByCodesAndDates(@RequestBody CodesDatesHistoryRequest request) {
        return cursHistoryService.getHistoryByCodesAndDates(request);
    }


}
