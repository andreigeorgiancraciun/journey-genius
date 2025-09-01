package com.journeygenius.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.journeygenius.dto.ItineraryDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ItineraryParser {

    private final ObjectMapper objectMapper;

    public ItineraryParser() {
        this.objectMapper = new ObjectMapper();
    }

    public ItineraryDTO parseResponse(String response) {
        try {
            // Parsarea răspunsului JSON într-un obiect de tip Map
            Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);

            // Extrage valorile corespunzătoare fiecărui câmp din răspunsul JSON
            String temperature = (String) responseMap.get("temperature");
            String currency = (String) responseMap.get("currency");
            List<String> attractions = castList(responseMap.get("attractions"));
            List<String> restaurants = castList(responseMap.get("restaurants"));
            List<String> traditionalFoods = castList(responseMap.get("traditional_foods"));
            List<String> transportationOptions = castList(responseMap.get("transportation_options"));
            List<String> accommodationOptions = castList(responseMap.get("accommodation_options"));
            List<String> localEvents = castList(responseMap.get("local_events"));

            // Crearea și returnarea unui obiect Itinerary cu valorile extrase
            return new ItineraryDTO(temperature, currency, attractions, restaurants,
                    traditionalFoods, transportationOptions, accommodationOptions, localEvents);
        } catch (IOException e) {
            e.printStackTrace(); // Tratează eroarea adecvat
            return null;
        }
    }

    // Metodă pentru a converti obiectul JSON într-o listă de șiruri
    @SuppressWarnings("unchecked")
    private List<String> castList(Object object) {
        if (object instanceof List) {
            return (List<String>) object;
        } else if (object instanceof String) {
            // În cazul în care obiectul este un șir, îl convertim într-o listă cu un singur element
            return Collections.singletonList((String) object);
        }
        return Collections.emptyList(); // Returnăm o listă goală în cazul în care obiectul nu este o listă sau un șir
    }
}

