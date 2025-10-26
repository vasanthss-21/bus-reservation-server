package com.SpringBoot.Bus_Reservation_System.repository;

import com.SpringBoot.Bus_Reservation_System.model.Route;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

// This interface gives us all standard CRUD operations for the Route collection for free.
@Repository
public interface RouteRepository extends MongoRepository<Route, String> {
}
