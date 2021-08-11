package ru.proskyryakov.cbrcursondateadapter.adapter.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
public class IntervalModel {

    private Long id;
    @Nullable
    private Long valuteId;
    @NonNull
    private String code;
    private Integer interval;
    private Boolean isActual;

}
