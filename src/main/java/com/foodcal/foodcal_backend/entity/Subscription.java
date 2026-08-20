package com.foodcal.foodcal_backend.entity;

import java.security.Timestamp;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Subscription")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id" , nullable = false)
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
    private Timestamp createdAt;
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public Long getId() {
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
    public Timestamp getCreatedAt() {
        return createdAt;
    }   
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    public void setId(Long id) {
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
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
