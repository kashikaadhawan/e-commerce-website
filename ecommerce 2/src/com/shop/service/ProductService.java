package com.shop.service;

import com.shop.model.Product;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ProductService — manages the product catalog.
 * Java concepts: ArrayList, Streams, Lambda expressions
 */
public class ProductService {

    private List<Product> catalog = new ArrayList<>();

    public ProductService() { loadProducts(); }

    private void loadProducts() {
        catalog.add(new Product(1,  "Wireless Earbuds",    "Electronics", 1999, 15, "Bluetooth 5.0 • 20hr battery • IPX5"));
        catalog.add(new Product(2,  "USB-C Hub 7-in-1",    "Electronics", 1499, 20, "HDMI • USB 3.0 • SD Card • PD 100W"));
        catalog.add(new Product(3,  "Mechanical Keyboard", "Electronics", 3499,  8, "RGB backlit • Tactile switches • TKL"));
        catalog.add(new Product(4,  "Smart Watch",         "Electronics", 4999, 10, "Heart rate • SpO2 • 7-day battery"));
        catalog.add(new Product(5,  "Running Shoes",       "Footwear",    2799, 25, "Lightweight mesh • Cushioned sole"));
        catalog.add(new Product(6,  "Leather Loafers",     "Footwear",    2199, 18, "Genuine leather • Slip-on comfort"));
        catalog.add(new Product(7,  "Leather Wallet",      "Accessories",  799, 40, "Slim RFID-blocking • 8 card slots"));
        catalog.add(new Product(8,  "Backpack 30L",        "Accessories", 2199, 12, "Water-resistant • Laptop sleeve 15\""));
        catalog.add(new Product(9,  "Graphic T-Shirt",     "Clothing",     499, 50, "100% cotton • Unisex • 5 colors"));
        catalog.add(new Product(10, "Denim Jacket",        "Clothing",    1899, 18, "Classic fit • Stonewash finish"));
        catalog.add(new Product(11, "Yoga Mat",            "Sports",       999, 22, "Anti-slip • 6mm thick • Carry strap"));
        catalog.add(new Product(12, "Resistance Bands",    "Sports",       599, 35, "Set of 5 • 10-50 lbs resistance"));
        catalog.add(new Product(13, "Notebook Set",        "Stationery",   299, 60, "Pack of 3 ruled notebooks • A5"));
        catalog.add(new Product(14, "Desk Organiser",      "Stationery",   849, 30, "Bamboo • 6 compartments"));
    }

    public List<Product> getAll()    { return catalog; }

    public Product findById(int id) {
        return catalog.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    public List<Product> filterByCategory(String cat) {
        if (cat.equals("All")) return catalog;
        return catalog.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(cat))
                .collect(Collectors.toList());
    }

    public List<Product> search(String kw) {
        String q = kw.toLowerCase();
        return catalog.stream()
                .filter(p -> p.getName().toLowerCase().contains(q)
                          || p.getCategory().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    public List<String> getCategories() {
        List<String> cats = catalog.stream()
                .map(Product::getCategory).distinct().sorted()
                .collect(Collectors.toList());
        cats.add(0, "All");
        return cats;
    }
}
