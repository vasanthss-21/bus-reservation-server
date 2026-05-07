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
@CrossOrigin(origins = "*")
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final RouteRepository routeRepository;

    public ReservationController(ReservationRepository reservationRepository, RouteRepository routeRepository) {
        this.reservationRepository = reservationRepository;
        this.routeRepository = routeRepository;
    }

    /**
     * Creates a new reservation.
     * Accepts: customerName, bookedBy, groupId, routeId, travelTime, seatNumber
     */
    @PostMapping
    public ResponseEntity<String> bookSeat(@RequestBody Reservation reservation) {

        // 1. Validate route
        Optional<Route> routeOpt = routeRepository.findById(reservation.getRouteId());
        if (routeOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid route ID.");
        }
        int capacity = routeOpt.get().getCapacity();

        // 2. Check if bus is full
        long currentBookings = reservationRepository.countByRouteIdAndTravelTime(
                reservation.getRouteId(), reservation.getTravelTime());
        if (currentBookings >= capacity) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("This bus is full.");
        }

        // 3. Check if seat is already taken
        boolean seatTaken = reservationRepository.existsByRouteIdAndTravelTimeAndSeatNumber(
                reservation.getRouteId(), reservation.getTravelTime(), reservation.getSeatNumber());
        if (seatTaken) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Seat " + reservation.getSeatNumber() + " is already taken.");
        }

        // 4. Save — bookedBy and groupId are populated from the request body automatically
        reservationRepository.save(reservation);
        return ResponseEntity.ok("Reservation successful!");
    }

    /**
     * Cancel a specific reservation by its MongoDB ObjectId.
     * DELETE /api/reservations/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelById(@PathVariable String id) {
        if (!reservationRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reservation not found.");
        }
        reservationRepository.deleteById(id);
        return ResponseEntity.ok("Reservation cancelled!");
    }

    /**
     * Legacy cancel by customerName + routeId (kept for backward compatibility).
     * DELETE /api/reservations
     */
    @DeleteMapping
    public ResponseEntity<String> cancelByName(@RequestBody Map<String, Object> request) {
        String name = (String) request.get("customerName");
        String routeId = (String) request.get("routeId");
        long deletedCount = reservationRepository.deleteByCustomerNameAndRouteId(name, routeId);
        return deletedCount > 0
                ? ResponseEntity.ok("Reservation cancelled!")
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("No reservation found.");
    }

    /**
     * Returns occupied seat numbers for a given route + time.
     * GET /api/reservations/occupied?routeId=X&travelTime=Y
     */
    @GetMapping("/occupied")
    public List<Integer> getOccupiedSeats(
            @RequestParam String routeId,
            @RequestParam String travelTime) {
        return reservationRepository.findOccupiedSeatNumbers(routeId, travelTime);
    }

    /**
     * Returns all bookings made by the account holder.
     * GET /api/reservations/my?bookedBy=Vasanth
     *
     * Falls back to customerName query for old records that lack bookedBy.
     */
    @GetMapping("/my")
    public List<Reservation> getMyBookings(
            @RequestParam(required = false) String bookedBy,
            @RequestParam(required = false) String name) {

        if (bookedBy != null && !bookedBy.isBlank()) {
            return reservationRepository.findByBookedBy(bookedBy);
        }
        // legacy fallback
        if (name != null && !name.isBlank()) {
            return reservationRepository.findByCustomerName(name);
        }
        return List.of();
    }
}
