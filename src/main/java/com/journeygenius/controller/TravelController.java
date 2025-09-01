package com.journeygenius.controller;

import com.journeygenius.dto.ItineraryDTO;
import com.journeygenius.model.Itinerary;
import com.journeygenius.model.TravelForm;
import com.journeygenius.service.TravelService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TravelController {

    private final TravelService travelService;

    public TravelController(TravelService travelService) {
        this.travelService = travelService;
    }

    @GetMapping("/")
    public String redirectToHomePage() {
        return "redirect:/homepage";
    }

    @GetMapping("/homepage")
    public String showHomePage(Model model, HttpServletRequest request) {
        model.addAttribute("travelForm", new TravelForm());
        model.addAttribute("requestURI", request.getRequestURI());
        return "homepage";
    }

    @GetMapping("/admin")
    public String showAdminPage(Model model, HttpServletRequest request) {
        List<Itinerary> itineraries = travelService.getAllItineraries();
        model.addAttribute("itineraries", itineraries);
        model.addAttribute("requestURI", request.getRequestURI());
        return "admin";
    }

    @PostMapping("/submit")
    public String submitForm(TravelForm travelForm, Model model, HttpServletRequest request) {
        String startDate = travelForm.getStartDate().toString();
        String endDate = travelForm.getEndDate().toString();
        String destination = travelForm.getDestination();

        ItineraryDTO itinerary = travelService.getItinerary(startDate, endDate, destination, request);

        model.addAttribute("destination", destination);
        model.addAttribute("itinerary", itinerary);

        return "success";
    }

    @PostMapping("/itineraries/delete/{id}")
    public String deleteItinerary(@PathVariable("id") Long id, Model model) {
        travelService.deleteItineraryById(id);
        List<Itinerary> itineraries = travelService.getAllItineraries();
        model.addAttribute("itineraries", itineraries);
        return "redirect:/admin";
    }

    @PostMapping("/chat")
    @ResponseBody
    public ChatResponse handleChat(@RequestBody ChatRequest chatRequest) {
        try {
            String gptResponse = travelService.chatWithGPT(chatRequest.getMessage());
            return new ChatResponse(gptResponse);
        } catch (Exception e) {
            return new ChatResponse("Error: " + e.getMessage());
        }
    }

    @GetMapping("/chat")
    public String getChatPage() {
        return "chat";
    }

    @Getter
    @Setter
    public static class ChatRequest {
        private String message;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ChatResponse {
        private String response;
    }
}

