package com.example.smartpantrymanager;

public class RecipeIngredient {
    private int id;
    private int recipeId;
    private String normalizedName;
    private double quantity;
    private String unit;

    public RecipeIngredient(int id, int recipeId, String normalizedName,
                            double quantity, String unit) {
        this.id = id;
        this.recipeId = recipeId;
        this.normalizedName = normalizedName;
        this.quantity = quantity;
        this.unit = unit;
    }

    public int getId() { return id; }
    public int getRecipeId() { return recipeId; }
    public String getNormalizedName() { return normalizedName; }
    public double getQuantity() { return quantity; }
    public String getUnit() { return unit; }
}
