package com.SpringBoot.Bus_Reservation_System.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

// Saves to the "users" collection in MongoDB
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String name;

    @Indexed(unique = true) // No two users can share the same email
    private String email;

    private String password; // Stored as a BCrypt hash, never plain text

    public String getId()             { return id; }

    public String getName()           { return name; }
    public void   setName(String n)   { this.name = n; }

    public String getEmail()          { return email; }
    public void   setEmail(String e)  { this.email = e; }

    public String getPassword()           { return password; }
    public void   setPassword(String p)   { this.password = p; }
}
