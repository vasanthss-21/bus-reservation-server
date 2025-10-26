package com.SpringBoot.Bus_Reservation_System.controller;

import com.SpringBoot.Bus_Reservation_System.model.Reservation;
import com.SpringBoot.Bus_Reservation_System.model.Route;
import com.SpringBoot.Bus_Reservation_System.repository.ReservationRepository;
import com.SpringBoot.Bus_Reservation_System.repository.RouteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:5173") // Or your frontend URL
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final RouteRepository routeRepository; // Required for capacity check

    // Inject both repositories
    public ReservationController(ReservationRepository reservationRepository, RouteRepository routeRepository) {
        this.reservationRepository = reservationRepository;
        this.routeRepository = routeRepository;
    }

    /**
     * Creates a new reservation with validation.
     * This uses the new methods from the repository.
     */
    @PostMapping
    public ResponseEntity<String> bookSeat(@RequestBody Reservation reservation) {

        // 1. Find the route to get its capacity
        Optional<Route> routeOpt = routeRepository.findById(reservation.getRouteId());
        if (routeOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid route ID.");
        }
        int capacity = routeOpt.get().getCapacity();

        // 2. Check if the bus is full (for that route and time)
        // Uses countByRouteIdAndTravelTime
        long currentBookings = reservationRepository.countByRouteIdAndTravelTime(
                reservation.getRouteId(), reservation.getTravelTime());

        if (currentBookings >= capacity) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This bus is full.");
        }

        // 3. Check if the specific seat is already taken (for that route and time)
        // Uses existsByRouteIdAndTravelTimeAndSeatNumber
        boolean seatTaken = reservationRepository.existsByRouteIdAndTravelTimeAndSeatNumber(
                reservation.getRouteId(), reservation.getTravelTime(), reservation.getSeatNumber());

        if (seatTaken) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Seat " + reservation.getSeatNumber() + " is already taken.");
        }

        // 4. All checks passed. Save the reservation.
        reservationRepository.save(reservation);
        return ResponseEntity.ok("Reservation successful!");
    }

    /**
     * Cancels an existing reservation.
     * Uses deleteByCustomerNameAndRouteId
     */
    @DeleteMapping
    public String cancelSeat(@RequestBody Map<String, Object> request) {
        String name = (String) request.get("customerName");
        String routeId = (String) request.get("routeId");

        long deletedCount = reservationRepository.deleteByCustomerNameAndRouteId(name, routeId);

        return (deletedCount > 0) ? "Reservation cancelled!" : "No reservation found.";
    }

    /**
     * Gets a list of occupied seat numbers for a specific route and time.
     * Uses findOccupiedSeatNumbers
     */
    @GetMapping("/occupied")
    public List<Integer> getOccupiedSeats(
            @RequestParam String routeId,
            @RequestParam String travelTime) {
        return reservationRepository.findOccupiedSeatNumbers(routeId, travelTime);
    }
}

