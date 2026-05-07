package com.SpringBoot.Bus_Reservation_System.repository;

import com.SpringBoot.Bus_Reservation_System.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {

    // Cancel by name + routeId (legacy, kept for safety)
    long deleteByCustomerNameAndRouteId(String customerName, String routeId);

    // Fetch all bookings for a specific passenger name (legacy)
    List<Reservation> findByCustomerName(String customerName);

    // Fetch all bookings made by a specific account holder (used by Profile & CancelBooking)
    List<Reservation> findByBookedBy(String bookedBy);

    // Count reservations for bus-full check
    long countByRouteIdAndTravelTime(String routeId, String travelTime);

    // Prevent double-booking a seat
    boolean existsByRouteIdAndTravelTimeAndSeatNumber(String routeId, String travelTime, int seatNumber);

    // Fetch only seat numbers for the occupied-seats overlay
    @Query(value = "{ 'route_id' : ?0, 'travel_time' : ?1 }", fields = "{ 'seat_number' : 1, '_id' : 0 }")
    List<Reservation> findBookedReservations(String routeId, String travelTime);

    default List<Integer> findOccupiedSeatNumbers(String routeId, String travelTime) {
        return findBookedReservations(routeId, travelTime)
                .stream()
                .map(Reservation::getSeatNumber)
                .collect(Collectors.toList());
    }
}
