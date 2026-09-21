package com.foodcal.foodcal_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "weight")
public class Weight {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private UserDetail user;

    @Column(name = "weight_kg", nullable = false)
    private double weightKg;

    @Column(name = "logged_on", nullable = false)
    private LocalDate loggedOn;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public UUID getId() {
        return id;
    }

    public UserDetail getUser() {
        return user;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public LocalDate getLoggedOn() {
        return loggedOn;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUser(UserDetail user) {
        this.user = user;
    }

    public void setWeightKg(double weightKg) {
        this.weightKg = weightKg;
    }

    public void setLoggedOn(LocalDate loggedOn) {
        this.loggedOn = loggedOn;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
    }
}