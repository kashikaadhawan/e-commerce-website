package com.shop.model;

/**
 * CartItem — one line in the shopping cart.
 * Java concept: Object Composition (CartItem HAS-A Product)
 */
public class CartItem {
    private Product product;
    private int     quantity;

    public CartItem(Product product, int quantity) {
        this.product  = product;
        this.quantity = quantity;
    }

    public Product getProduct()           { return product; }
    public int     getQuantity()          { return quantity; }
    public void    setQuantity(int qty)   { quantity = qty; }
    public double  getSubtotal()          { return product.getPrice() * quantity; }
}
