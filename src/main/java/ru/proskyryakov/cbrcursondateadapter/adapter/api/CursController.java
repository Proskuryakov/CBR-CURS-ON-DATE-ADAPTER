package ru.proskyryakov.cbrcursondateadapter.adapter.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.proskyryakov.cbrcursondateadapter.adapter.exceptions.NotFoundException;
import ru.proskyryakov.cbrcursondateadapter.adapter.logging.RabbitLogging;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.CodeWithDates;
import ru.proskyryakov.cbrcursondateadapter.adapter.models.CursOnDate;
import ru.proskyryakov.cbrcursondateadapter.adapter.services.CursService;

import java.util.List;

@RestController
@RequestMapping("/curs")
@RequiredArgsConstructor
public class CursController {
    private final CursService cursService;

    @RabbitLogging
    @GetMapping("/{code}")
    public CursOnDate getCursByCode(@PathVariable String code) throws NotFoundException {
        var result = cursService.getCursByCode(code);
        if(result == null) throw new NotFoundException(String.format("Curs by %s code not found", code));
        return result;
    }

    @RabbitLogging
    @GetMapping("/{code}/date/{date}")
    public CursOnDate getCursByCodeAndDate(@PathVariable String code, @PathVariable String date) throws NotFoundException {
        var result = cursService.getCursByCodeAndDate(code, date);
        if(result == null) throw new NotFoundException(String.format("Curs by %s code not found", code));
        return result;
    }

    @RabbitLogging
    @PostMapping
    public List<CursOnDate> getCursByDates(@RequestBody CodeWithDates codeWithDates){
        return cursService.getCursByDates(codeWithDates);
    }


}
