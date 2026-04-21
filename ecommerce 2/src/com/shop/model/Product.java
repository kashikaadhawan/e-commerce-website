package com.shop.model;

/**
 * Product — represents a single item in the store.
 * Java concept: Encapsulation (private fields, public getters/setters)
 */
public class Product {
    private int    id;
    private String name;
    private String category;
    private double price;
    private int    stock;
    private String description;

    public Product(int id, String name, String category,
                   double price, int stock, String description) {
        this.id          = id;
        this.name        = name;
        this.category    = category;
        this.price       = price;
        this.stock       = stock;
        this.description = description;
    }

    // ── Getters ────────────────────────────────────────────────────────
    public int    getId()          { return id; }
    public String getName()        { return name; }
    public String getCategory()    { return category; }
    public double getPrice()       { return price; }
    public int    getStock()       { return stock; }
    public String getDescription() { return description; }

    // Reduce stock when purchased; returns false if not enough
    public boolean reduceStock(int qty) {
        if (stock >= qty) { stock -= qty; return true; }
        return false;
    }
}
