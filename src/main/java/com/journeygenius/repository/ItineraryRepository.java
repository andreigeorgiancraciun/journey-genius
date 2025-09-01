package com.journeygenius.repository;

import com.journeygenius.model.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {
    Optional<Itinerary> findByStartDateAndEndDateAndDestination(String startDate, String endDate, String destination);
}
