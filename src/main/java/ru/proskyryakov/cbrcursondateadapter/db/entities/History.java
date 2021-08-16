package ru.proskyryakov.cbrcursondateadapter.db.entities;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@ToString
public class History {

    @Id
    @GeneratedValue(generator = "history_id_seq")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "interval_id", referencedColumnName = "id")
    private Interval interval;
    private BigDecimal curse;
    private Date datetime;


}
