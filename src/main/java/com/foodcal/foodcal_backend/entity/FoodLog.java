package com.foodcal.foodcal_backend.entity;

import java.security.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "food_log")
public class FoodLog {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id" , nullable = false)
    private UserDetail user;

    @Column(name = "food_name")
    private String foodName;
    @Column(name = "calories")
    private Double calories;
    @Column(name = "protein_g")
    private Double proteinG;
    @Column(name = "fat_g")
    private Double fatG;
    @Column(name = "carbohydrate_g")
    private Double carbohydrateG;
    @Column(name = "ai_confidence")
    private Double aiConfidence;
    @Column(name = "image_path")
    private String imagePath;
    @Column(name = "meal_type")
    private String mealType;
    @Column(name = "is_manual")
    private Boolean isManual;
    @Column(name = "created_at")
    private Timestamp createdAt;
    

    public Long getId() {
        return id;
    }
    public UserDetail getUser() {
        return user;
    }
    public String getFoodName() {
        return foodName;
    }
    public Double getCalories() {
        return calories;
    }
    public Double getProteinG() {
        return proteinG;
    }
    public Double getFatG() {
        return fatG;
    }
    public Double getCarbohydrateG() {
        return carbohydrateG;
    }
    public Double getAiConfidence() {
        return aiConfidence;
    }
    public String getImagePath() {
        return imagePath;
    }
    public String getMealType() {
        return mealType;
    }
    public Boolean getIsManual() {
        return isManual;
    }
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setUser(UserDetail user) {
        this.user = user;
    }
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }
    public void setProteinG(Double proteinG) {
        this.proteinG = proteinG;
    }
    public void setFatG(Double fatG) {
        this.fatG = fatG;
    }
    public void setCarbohydrateG(Double carbohydrateG) {
        this.carbohydrateG = carbohydrateG;
    }

    public void setAiConfidence(Double aiConfidence) {
        this.aiConfidence = aiConfidence;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public void setMealType(String mealType) {
        this.mealType = mealType;
    }
    public void setIsManual(Boolean isManual) {
        this.isManual = isManual;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}