package ru.proskyryakov.cbrcursondateadapter.db.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Valute {

    @Id
    @GeneratedValue(generator = "valute_id_seq")
    private Long id;
    @Column(unique = true)
    private String code;
    private Integer interval;

}
