package com.foodcal.foodcal_backend.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "fitness_details")
public class FitnessDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserDetail user;

    @Column(name = "age", nullable = false)
    private int age;
    @Column(name = "height", nullable = false)
    private BigDecimal height;
    @Column(name = "weight", nullable = false)
    private BigDecimal weight;
    @Column(name = "activity_level", nullable = false)
    private String activityLevel;
    @Column(name = "target_weight_kg", nullable = false)
    private BigDecimal targetWeightKg;
    @Column(name = "target_date", nullable = false)
    private LocalDate targetDate;
    @Column(name = "daily_calorie_target", nullable = false)
    private int dailyCalorieTarget;
    @Column(name = "daily_protein_target_g", nullable = false)
    private BigDecimal dailyProteinTargetG;
    @Column(name = "daily_carbs_target_g", nullable = false)
    private BigDecimal dailyCarbsTargetG;
    @Column(name = "daily_fat_target_g", nullable = false)
    private BigDecimal dailyFatTargetG;
    @JdbcTypeCode(SqlTypes.LONG32VARCHAR)
    @Column(name = "ai_coach_advice", nullable = false)
    private String aiCoachAdvice;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserDetail getUser() {
        return user;
    }

    public void setUser(UserDetail user) {
        this.user = user;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }

    public BigDecimal getTargetWeightKg() {
        return targetWeightKg;
    }

    public void setTargetWeightKg(BigDecimal targetWeightKg) {
        this.targetWeightKg = targetWeightKg;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public int getDailyCalorieTarget() {
        return dailyCalorieTarget;
    }

    public void setDailyCalorieTarget(int dailyCalorieTarget) {
        this.dailyCalorieTarget = dailyCalorieTarget;
    }

    public BigDecimal getDailyProteinTargetG() {
        return dailyProteinTargetG;
    }

    public void setDailyProteinTargetG(BigDecimal dailyProteinTargetG) {
        this.dailyProteinTargetG = dailyProteinTargetG;
    }

    public BigDecimal getDailyCarbsTargetG() {
        return dailyCarbsTargetG;
    }

    public void setDailyCarbsTargetG(BigDecimal dailyCarbsTargetG) {
        this.dailyCarbsTargetG = dailyCarbsTargetG;
    }

    public BigDecimal getDailyFatTargetG() {
        return dailyFatTargetG;
    }

    public void setDailyFatTargetG(BigDecimal dailyFatTargetG) {
        this.dailyFatTargetG = dailyFatTargetG;
    }

    public String getAiCoachAdvice() {
        return aiCoachAdvice;
    }

    public void setAiCoachAdvice(String aiCoachAdvice) {
        this.aiCoachAdvice = aiCoachAdvice;
    }
}
