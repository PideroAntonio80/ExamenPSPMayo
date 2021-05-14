package com.sanvalero.examenpspmayo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Creado por @ author: Pedro Orós
 * el 14/05/2021
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Job {

    private String title;
    private String type;
    private String location;
    private String description;
    private String company_logo;
}
