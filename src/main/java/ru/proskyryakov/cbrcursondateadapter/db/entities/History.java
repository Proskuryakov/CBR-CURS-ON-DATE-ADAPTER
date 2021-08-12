package ru.proskyryakov.cbrcursondateadapter.db.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class History {

    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "interval_id", referencedColumnName = "id")
    private Interval interval;
    private BigDecimal curse;
    private Date datetime;


}
