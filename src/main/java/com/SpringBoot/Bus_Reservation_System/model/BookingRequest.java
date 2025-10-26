package com.SpringBoot.Bus_Reservation_System.model;

public record BookingRequest(
        String customerName,
        int routeId,
        String travelTime,
        int seatNumber
) {}
