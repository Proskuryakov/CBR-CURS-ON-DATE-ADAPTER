package ru.proskyryakov.cbrcursondateadapter.adapter.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.IntervalModel;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.ValuteModel;
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

    @GetMapping("/intervals")
    public List<IntervalModel> getAllInterval() {
        return cursHistoryService.getAllInterval();
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

}
