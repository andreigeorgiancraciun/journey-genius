package com.journeygenius.service;

import com.journeygenius.dto.ItineraryDTO;
import com.journeygenius.model.Itinerary;
import com.journeygenius.parser.ItineraryParser;
import com.journeygenius.repository.ItineraryRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TravelService {

    private final OpenAiChatClient chatClient;
    private final ItineraryParser itineraryParser;
    private final ItineraryRepository itineraryRepository;

    public TravelService(OpenAiChatClient chatClient,
                         ItineraryParser itineraryParser,
                         ItineraryRepository itineraryRepository) {
        this.chatClient = chatClient;
        this.itineraryParser = itineraryParser;
        this.itineraryRepository = itineraryRepository;
    }

    public ItineraryDTO getItinerary(String startDate, String endDate, String destination, HttpServletRequest request) {

        //verifica daca exista in baza de date
        Optional<Itinerary> existingItinerary = itineraryRepository.findByStartDateAndEndDateAndDestination(startDate, endDate, destination);

        if (existingItinerary.isPresent()) {
            return toDTO(existingItinerary.get());
        } else {
            String response = callChatGPT(startDate, endDate, destination);
            ItineraryDTO itineraryDTO = itineraryParser.parseResponse(response);
            saveItineraryInDataBase(startDate, endDate, destination, itineraryDTO, request);
            return itineraryDTO;
        }
    }

    public List<Itinerary> getAllItineraries() {
        return itineraryRepository.findAll();
    }

    public void deleteItineraryById(Long id) {
        itineraryRepository.deleteById(id);
    }

    public String chatWithGPT(String message) {
        return chatClient.call(message);
    }

    private String callChatGPT(String startDate, String endDate, String destination) {
        //daca nu exista in baza de date apeleaza chat gpt
        String message = String.format("""
                "message": "Am de gând să călătoresc în perioada [%s] - [%s] în [%s].\s
                Vreau să mă ajuți cu câteva informații pentru a-mi face călătoria cât mai plăcută.\s
                Răspunsul pe care îl aștept din partea ta trebuie să respecte formatul următor:
                               
                "response_format": {
                  "temperature": "Temperatura medie în perioada respectivă",
                  "currency": "Moneda locală",
                  "attractions": ["Top 10 atracții turistice împreună cu un link către atractia respectivă"],
                  "restaurants": ["Top 10 restaurante împreună cu un link cu locul unde putem gusta preparatul respectiv"],
                  "traditional_foods": ["Top 3 mâncăruri și băuturi tradiționale împreună cu un link către rețetele preparatului respectiv"],
                  "transportation_options": ["Opțiuni de transport împreună cu pretul aproximativ"],
                  "accommodation_options": ["10 sugestii de hoteluri împreună cu un link care duce la hotelul respectiv"],
                  "local_events": ["Evenimente disponibil în perioada respectivă împreună cu un link către evenimentul respectiv"]
                  }
                              
                """, startDate, endDate, destination);

        return chatClient.call(message);
    }

    private void saveItineraryInDataBase(String startDate, String endDate, String destination,
                                         ItineraryDTO itineraryDTO, HttpServletRequest request) {

        Itinerary itinerary = toEntity(startDate, endDate, destination, itineraryDTO, request);
        itineraryRepository.save(itinerary);
    }

    private Itinerary toEntity(String startDate, String endDate, String destination,
                               ItineraryDTO dto, HttpServletRequest request) {
        Itinerary itinerary = new Itinerary();
        itinerary.setStartDate(startDate);
        itinerary.setEndDate(endDate);
        itinerary.setDestination(destination);
        itinerary.setTemperature(dto.getTemperature());
        itinerary.setCurrency(dto.getCurrency());
        itinerary.setAttractions(dto.getAttractions());
        itinerary.setRestaurants(dto.getRestaurants());
        itinerary.setTraditionalFoods(dto.getTraditionalFoods());
        itinerary.setTransportationOptions(dto.getTransportationOptions());
        itinerary.setAccommodationOptions(dto.getAccommodationOptions());
        itinerary.setLocalEvents(dto.getLocalEvents());
        itinerary.setIpAddress(request.getRemoteAddr());
        return itinerary;
    }

    private ItineraryDTO toDTO(Itinerary entity) {
        ItineraryDTO itineraryDTO = new ItineraryDTO();
        itineraryDTO.setTemperature(entity.getTemperature());
        itineraryDTO.setCurrency(entity.getCurrency());
        itineraryDTO.setAttractions(entity.getAttractions());
        itineraryDTO.setRestaurants(entity.getRestaurants());
        itineraryDTO.setTraditionalFoods(entity.getTraditionalFoods());
        itineraryDTO.setTransportationOptions(entity.getTransportationOptions());
        itineraryDTO.setAccommodationOptions(entity.getAccommodationOptions());
        itineraryDTO.setLocalEvents(entity.getLocalEvents());
        return itineraryDTO;
    }

}
