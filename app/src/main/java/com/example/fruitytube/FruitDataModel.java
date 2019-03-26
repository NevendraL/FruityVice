package com.example.fruitytube;

public class FruitDataModel {

    private String fruitName;
    private double calories;
    private double protein;
    private double carbs;
    private double fat;


    public FruitDataModel(String fruitName, Double calories, Double protein, double carbs, double fat){
        this.fruitName = fruitName;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }

    public void setFruitName(String fruitName){
        this.fruitName = fruitName;
    }

    public String getFruitName(){
        return fruitName;
    }
    public void setCalories(double calories){
        this.calories = calories;
    }

    public double getCalories() {
        return calories;
    }

    public void setProtein(double protein){
        this.protein = protein;
    }

    public double getProtein() {
        return protein;
    }

    public void setCarbs(double carbs){
        this.carbs = carbs;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getFat() {
        return fat;
    }
}
