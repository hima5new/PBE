package com.epam.restaurant.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DishPojo {

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("price")
    private String price;

    @JsonProperty("weight")
    private String weight;

    @JsonProperty("dishType")
    private String dishType;

    @JsonProperty("calories")
    private String calories;

    @JsonProperty("fats")
    private String fats;

    @JsonProperty("carbohydrates")
    private String carbohydrates;

    @JsonProperty("proteins")
    private String proteins;

    @JsonProperty("vitamins")
    private String vitamins;

    @JsonProperty("state")
    private boolean state;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDishType() {
        return dishType;
    }

    public void setDishType(String dishType) {
        this.dishType = dishType;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getFats() {
        return fats;
    }

    public void setFats(String fats) {
        this.fats = fats;
    }

    public String getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(String carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public String getProteins() {
        return proteins;
    }

    public void setProteins(String proteins) {
        this.proteins = proteins;
    }

    public String getVitamins() {
        return vitamins;
    }

    public void setVitamins(String vitamins) {
        this.vitamins = vitamins;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }


    @Override
    public String toString() {
        return "DishPojo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", weight='" + weight + '\'' +
                ", dishType='" + dishType + '\'' +
                ", calories='" + calories + '\'' +
                ", fats='" + fats + '\'' +
                ", carbohydrates='" + carbohydrates + '\'' +
                ", proteins='" + proteins + '\'' +
                ", vitamins='" + vitamins + '\'' +
                ", state=" + state +
                '}';
    }
}