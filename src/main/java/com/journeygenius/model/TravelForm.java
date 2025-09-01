package com.journeygenius.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TravelForm {
    private LocalDate startDate;
    private LocalDate endDate;
    private String destination;
}
