package com.example.recipefinalproject.models;

public class Recipe {
    private String id;
    private String name;
    private String image;
    private String description;
    private String category;
    private String calories;
    private String time;

    private String authorId;



    public Recipe(String name, String description, String category, String calories, String time, String image, String authorId) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.calories = calories;
        this.time = time;
        this.image = image;
        this.authorId = authorId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
}
