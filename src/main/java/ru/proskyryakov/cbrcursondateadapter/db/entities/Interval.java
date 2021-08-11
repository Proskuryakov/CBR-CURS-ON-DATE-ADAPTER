package ru.proskyryakov.cbrcursondateadapter.db.entities;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "intervals")
@Data
public class Interval {

    @Id
    @GeneratedValue(generator = "intervals_id_seq")
    private Long id;
    private Integer interval;
    @Column(name = "is_actual")
    private Boolean isActual;

    @OneToOne
    @JoinColumn(name = "valute_id", referencedColumnName = "id")
    private Valute valute;

}
