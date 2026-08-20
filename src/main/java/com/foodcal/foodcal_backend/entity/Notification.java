package com.foodcal.foodcal_backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "Notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id" , nullable = false)
    private UserDetail user;

    @Column(name = "title")
    private String title;
    @Column(name = "message")
    private String message;
    @Column(name = "type")
    private String type;
    @Column(name = "is_read")
    private Boolean isRead;
    @Column(name = "meta_data")
    private String metaData;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public UUID getId() {
        return id;
    }
    public UserDetail getUser() {
        return user;
    }
    public String getTitle() {
        return title;
    }
    public String getMessage() {
        return message;
    }
    public String getType() {
        return type;
    }
    public Boolean getIsRead() {
        return isRead;
    }
    public String getMetaData() {
        return metaData;
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
    public void setTitle(String title) {
        this.title = title;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
