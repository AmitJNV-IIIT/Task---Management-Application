package com.example.taskmanager.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.TimeZone;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private TimeZone timezone;

    private Boolean isActive;
}
