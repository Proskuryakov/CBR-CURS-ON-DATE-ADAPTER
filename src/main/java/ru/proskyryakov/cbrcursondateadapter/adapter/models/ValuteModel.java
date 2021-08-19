package ru.proskyryakov.cbrcursondateadapter.adapter.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValuteModel {

    private Long id;
    private String code;
    private String name;

}
