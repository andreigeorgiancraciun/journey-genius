package com.journeygenius.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItineraryDTO {
    private String temperature;
    private String currency;
    private List<String> attractions;
    private List<String> restaurants;
    private List<String> traditionalFoods;
    private List<String> transportationOptions;
    private List<String> accommodationOptions;
    private List<String> localEvents;
}
