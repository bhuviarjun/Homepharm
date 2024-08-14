package com.example.homepharm;

import java.util.HashMap;
import java.util.Map;

public class OrderItem {
    private String name;
    private double basePrice;
    private String description;
    private int imageResId;
    private int quantity;

    public OrderItem() {
    }

    public OrderItem(String name, double basePrice, String description, int imageResId, int quantity) {
        this.name = name;
        this.basePrice = basePrice;
        this.description = description;
        this.imageResId = imageResId;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResId() {
        return imageResId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return basePrice * quantity;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("basePrice", basePrice);
        map.put("description", description);
        map.put("imageResId", imageResId);
        map.put("quantity", quantity);
        return map;
    }
}
