package com.shop.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Order — a completed purchase snapshot.
 * Java concept: static counter (auto-increment IDs), LocalDateTime
 */
public class Order {
    private static int counter = 1000;

    private int            orderId;
    private String         username;
    private List<CartItem> items;
    private double         total;
    private String         status;
    private String         date;
    private String         paymentMethod;

    public Order(String username, List<CartItem> items,
                 double total, String paymentMethod) {
        this.orderId        = ++counter;
        this.username       = username;
        this.items          = items;
        this.total          = total;
        this.status         = "Confirmed";
        this.paymentMethod  = paymentMethod;
        this.date           = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
    }

    public int            getOrderId()       { return orderId; }
    public String         getUsername()      { return username; }
    public List<CartItem> getItems()         { return items; }
    public double         getTotal()         { return total; }
    public String         getStatus()        { return status; }
    public String         getDate()          { return date; }
    public String         getPaymentMethod() { return paymentMethod; }
}
