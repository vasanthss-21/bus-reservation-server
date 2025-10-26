package com.SpringBoot.Bus_Reservation_System.repository;

import com.SpringBoot.Bus_Reservation_System.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {

    // Method for the cancellation logic
    long deleteByCustomerNameAndRouteId(String customerName, String routeId);

    /**
     * Counts all reservations for a specific route and departure time.
     * This is used by the controller to check if the bus is full.
     */
    long countByRouteIdAndTravelTime(String routeId, String travelTime);

    /**
     * Checks if a specific seat is already reserved on a specific bus (route + time).
     * This is used by the controller to prevent double-booking a seat.
     */
    boolean existsByRouteIdAndTravelTimeAndSeatNumber(String routeId, String travelTime, int seatNumber);

    /**
     * 1. A private query to find reservations matching the route/time,
     * but only returning the 'seatNumber' field for efficiency.
     */
    @Query(value = "{ 'routeId' : ?0, 'travelTime' : ?1 }", fields = "{ 'seatNumber' : 1, '_id' : 0 }")
    List<Reservation> findBookedReservations(String routeId, String travelTime);

    /**
     * 2. A public default method to easily convert the list of Reservation objects
     * into a simple list of Integers (seat numbers).
     * This is used by the controller's /occupied endpoint.
     */
    default List<Integer> findOccupiedSeatNumbers(String routeId, String travelTime) {
        return findBookedReservations(routeId, travelTime)
                .stream()
                .map(Reservation::getSeatNumber)
                .collect(Collectors.toList());
    }
}

