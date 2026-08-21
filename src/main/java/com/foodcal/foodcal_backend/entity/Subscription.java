package com.foodcal.foodcal_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Subscription")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserDetail user;

    @Column(name = "plan")
    private String plan;

    @Column(name = "status")
    private String status;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "payment_provider")
    private String paymentProvider;

    @Column(name = "payment_subscription_id")
    private String paymentSubscriptionId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public UUID getId() {
        return id;
    }

    public UserDetail getUser() {
        return user;
    }

    public String getPlan() {
        return plan;
    }

    public String getStatus() {
        return status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getPaymentProvider() {
        return paymentProvider;
    }

    public String getPaymentSubscriptionId() {
        return paymentSubscriptionId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUser(UserDetail user) {
        this.user = user;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setPaymentProvider(String paymentProvider) {
        this.paymentProvider = paymentProvider;
    }

    public void setPaymentSubscriptionId(String paymentSubscriptionId) {
        this.paymentSubscriptionId = paymentSubscriptionId;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
