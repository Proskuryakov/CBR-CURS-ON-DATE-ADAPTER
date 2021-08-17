package ru.proskyryakov.cbrcursondateadapter.rabbit;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ChangeCursModel {

    private BigDecimal difference;
    private ChangeDirection direction;
    private Date datetime;

}
