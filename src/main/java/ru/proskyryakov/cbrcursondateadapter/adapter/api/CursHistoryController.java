package ru.proskyryakov.cbrcursondateadapter.adapter.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

}
