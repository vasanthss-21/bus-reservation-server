package com.SpringBoot.Bus_Reservation_System.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "reservations")
public class Reservation {

    @Id
    private String id;

    @Field("customer_name")
    private String customerName;   // actual passenger name

    @Field("booked_by")
    private String bookedBy;       // account holder who made the booking

    @Field("group_id")
    private String groupId;        // shared ID across all seats in one session

    @Field("route_id")
    private String routeId;

    @Field("travel_time")
    private String travelTime;

    @Field("seat_number")
    private int seatNumber;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getBookedBy() { return bookedBy; }
    public void setBookedBy(String bookedBy) { this.bookedBy = bookedBy; }

    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }

    public String getRouteId() { return routeId; }
    public void setRouteId(String routeId) { this.routeId = routeId; }

    public String getTravelTime() { return travelTime; }
    public void setTravelTime(String travelTime) { this.travelTime = travelTime; }

    public int getSeatNumber() { return seatNumber; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }
}
