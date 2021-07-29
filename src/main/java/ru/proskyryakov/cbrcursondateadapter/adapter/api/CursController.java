package ru.proskyryakov.cbrcursondateadapter.adapter.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.CursOnDate;
import ru.proskyryakov.cbrcursondateadapter.adapter.services.CursService;

@RestController
@RequestMapping("/curs")
@RequiredArgsConstructor
public class CursController {
    private final CursService cursService;

    @GetMapping("/{code}")
    public CursOnDate getCursByCode(@PathVariable String code){
        return cursService.getCursByCode(code);
    }

    @GetMapping("/{code}/date/{date}")
    public CursOnDate getCursByCodeAndDate(@PathVariable String code, @PathVariable String date){
        return cursService.getCursByCodeAndDate(code, date);
    }



}
