package com.foodcal.foodcal_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "fitness_details")
public class FitnessDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @JsonIgnore
    private UserDetail user;

    @Column(name = "gender")
    private String gender;

    @Column(name = "age", nullable = true)
    private Integer age;

    @Column(name = "height", nullable = true)
    private BigDecimal height;

    @Column(name = "weight", nullable = true)
    private BigDecimal weight;

    @Column(name = "activity_level", nullable = true)
    private String activityLevel;

    @Column(name = "target_weight_kg", nullable = true)
    private BigDecimal targetWeightKg;

    @Column(name = "objective", nullable = true)
    private String objective;

    @Column(name = "target_date", nullable = true)
    private LocalDate targetDate;

    @Column(name = "daily_calorie_target", nullable = true)
    private Integer dailyCalorieTarget;

    @Column(name = "daily_protein_target_g", nullable = true)
    private BigDecimal dailyProteinTargetG;

    @Column(name = "daily_carbs_target_g", nullable = true)
    private BigDecimal dailyCarbsTargetG;

    @Column(name = "daily_fat_target_g", nullable = true)
    private BigDecimal dailyFatTargetG;

    @JdbcTypeCode(SqlTypes.LONG32VARCHAR)
    @Column(name = "ai_coach_advice", nullable = true)
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
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

    public Integer getDailyCalorieTarget() {
        return dailyCalorieTarget;
    }

    public void setDailyCalorieTarget(Integer dailyCalorieTarget) {
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

    public void setObjective(String objective){
        this.objective = objective;
    }

    public String getObjective(){
        return objective;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }
}
