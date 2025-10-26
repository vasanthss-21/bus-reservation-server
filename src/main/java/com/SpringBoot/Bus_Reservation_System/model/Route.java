package com.SpringBoot.Bus_Reservation_System.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

// @Document maps this class to the "routes" collection in MongoDB
@Document(collection = "routes")
public class Route {

    @Id
    private String id;

    @Field("bus_name")
    private String busName;

    private String origin;
    private String destination;
    private int capacity;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getBusName() { return busName; }
    public void setBusName(String busName) { this.busName = busName; }
    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
}

