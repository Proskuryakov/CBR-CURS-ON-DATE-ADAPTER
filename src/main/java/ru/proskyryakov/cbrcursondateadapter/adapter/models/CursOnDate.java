package ru.proskyryakov.cbrcursondateadapter.adapter.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CursOnDate {

    private String code;
    private BigDecimal curs;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date;

}
