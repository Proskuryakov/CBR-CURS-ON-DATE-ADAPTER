package ru.proskyryakov.cbrcursondateadapter.db.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Valute {

    @Id
    private Long id;
    private String code;
    private Integer interval;

}
