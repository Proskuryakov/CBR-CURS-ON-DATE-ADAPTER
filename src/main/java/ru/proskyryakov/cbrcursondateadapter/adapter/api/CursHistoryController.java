package ru.proskyryakov.cbrcursondateadapter.adapter.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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
        return cursHistoryService.getAllValutes();
    }

    @GetMapping("/valute/{code}")
    public ValuteModel getValuteByCode(@PathVariable("code") String code) {
        return cursHistoryService.getValuteByCode(code);
    }

    @DeleteMapping("/valute/{code}")
    public void deleteValuteByCode(@PathVariable("code") String code) {
        cursHistoryService.deleteValuteByCode(code);
    }

    @PutMapping("/valute")
    public void updateValuteInterval(@RequestBody ValuteModel valuteModel) {
        cursHistoryService.updateValuteInterval(valuteModel);
    }

    @PostMapping("/valute")
    public void addValute(@RequestBody ValuteModel valuteModel) {
        cursHistoryService.addValute(valuteModel);
    }

}
