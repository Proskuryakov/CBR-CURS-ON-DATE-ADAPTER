package ru.proskyryakov.cbrcursondateadapter.db.entities;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "valutes")
@Data
public class Valute {

    @Id
    @GeneratedValue(generator = "valutes_id_seq")
    private Long id;
    private String code;
    private String name;

}
