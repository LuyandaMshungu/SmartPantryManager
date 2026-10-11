package com.example.smartpantrymanager;

public class PantryItem {
    private int id;
    private String name;
    private String normalizedName;
    private double quantity;
    private String unit;
    private String expiryDate;
    public PantryItem(int id, String name, String normalizedName,
                      double quantity, String unit, String expiryDate) {
        this.id = id;
        this.name = name;
        this.normalizedName = normalizedName;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getNormalizedName() { return normalizedName; }
    public double getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getExpiryDate() { return expiryDate; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setNormalizedName(String normalizedName) { this.normalizedName = normalizedName; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
}