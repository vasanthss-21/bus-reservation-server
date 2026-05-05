package com.SpringBoot.Bus_Reservation_System.repository;

import com.SpringBoot.Bus_Reservation_System.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    // Spring auto-generates the query: SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);
}
