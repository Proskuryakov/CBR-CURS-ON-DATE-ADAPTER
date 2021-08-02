package ru.proskyryakov.cbrcursondateadapter.adapter.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.CodeWithDates;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.CursOnDate;
import ru.proskyryakov.cbrcursondateadapter.adapter.services.CursService;

import java.util.List;

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

    @PostMapping
    public List<CursOnDate> getCursByDates(@RequestBody CodeWithDates codeWithDates){
        return cursService.getCursByDates(codeWithDates);
    }


}
