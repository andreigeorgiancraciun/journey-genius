package com.journeygenius.model;

import com.journeygenius.converter.JsonConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "itineraries", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"start_date", "end_date", "destination"})
})
public class Itinerary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String startDate;
    private String endDate;
    private String destination;

    private String temperature;
    private String currency;

    @Convert(converter = JsonConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> attractions;

    @Convert(converter = JsonConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> restaurants;

    @Convert(converter = JsonConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> traditionalFoods;

    @Convert(converter = JsonConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> transportationOptions;

    @Convert(converter = JsonConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> accommodationOptions;

    @Convert(converter = JsonConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> localEvents;

    private String ipAddress;
}
