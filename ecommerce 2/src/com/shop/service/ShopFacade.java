package com.shop.service;

import com.shop.model.*;
import java.util.*;
import java.util.stream.Collectors;

// ─────────────────────────────────────────────────────────────────────────────
// UserService — registration & authentication
// Java concept: HashMap (username → User), basic validation
// ─────────────────────────────────────────────────────────────────────────────
class UserService {
    private Map<String, User> db = new HashMap<>();
    private User current = null;

    UserService() {
        db.put("demo", new User("demo", "demo123", "demo@shop.com", "123 MG Road, Delhi"));
    }

    /** Returns null on success, or an error message string */
    public String register(String username, String password, String email, String address) {
        if (username.length() < 3)       return "Username must be at least 3 characters.";
        if (password.length() < 6)       return "Password must be at least 6 characters.";
        if (db.containsKey(username))    return "Username already taken.";
        if (!email.contains("@"))        return "Enter a valid email address.";
        db.put(username, new User(username, password, email, address));
        return null; // null = success
    }

    /** Returns null on success, or an error message string */
    public String login(String username, String password) {
        User u = db.get(username);
        if (u == null || !u.getPassword().equals(password))
            return "Invalid username or password.";
        current = u;
        return null;
    }

    public void   logout()        { current = null; }
    public boolean isLoggedIn()   { return current != null; }
    public User   getCurrent()    { return current; }
    public String getUsername()   { return current != null ? current.getUsername() : "Guest"; }
}

// ─────────────────────────────────────────────────────────────────────────────
// CartService — in-memory shopping cart
// Java concept: ArrayList, Iterator for safe removal
// ─────────────────────────────────────────────────────────────────────────────
class CartService {
    private List<CartItem> cart = new ArrayList<>();

    public String add(Product p, int qty) {
        if (qty < 1)             return "Quantity must be at least 1.";
        if (qty > p.getStock())  return "Only " + p.getStock() + " in stock.";
        for (CartItem item : cart) {
            if (item.getProduct().getId() == p.getId()) {
                int newQty = item.getQuantity() + qty;
                if (newQty > p.getStock()) return "Total would exceed stock (" + p.getStock() + ").";
                item.setQuantity(newQty);
                return null;
            }
        }
        cart.add(new CartItem(p, qty));
        return null;
    }

    public void remove(int productId) {
        var it = cart.iterator();
        while (it.hasNext()) { if (it.next().getProduct().getId() == productId) { it.remove(); return; } }
    }

    public void clear()                   { cart.clear(); }
    public boolean isEmpty()              { return cart.isEmpty(); }
    public List<CartItem> getItems()      { return cart; }
    public double getTotal() {
        return cart.stream().mapToDouble(CartItem::getSubtotal).sum();
    }
    public int getCount() {
        return cart.stream().mapToInt(CartItem::getQuantity).sum();
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// OrderService — place orders and maintain history
// Java concept: ArrayList of Orders, Streams for filtering by user
// ─────────────────────────────────────────────────────────────────────────────
class OrderService {
    private List<Order> history = new ArrayList<>();

    public Order place(String username, List<CartItem> items, double total, String payment) {
        // Deep-copy items so the order snapshot is frozen
        List<CartItem> snapshot = new ArrayList<>(items);
        for (CartItem ci : snapshot) {
            if (!ci.getProduct().reduceStock(ci.getQuantity())) return null;
        }
        Order o = new Order(username, snapshot, total, payment);
        history.add(o);
        return o;
    }

    public List<Order> getByUser(String username) {
        return history.stream()
                .filter(o -> o.getUsername().equals(username))
                .collect(Collectors.toList());
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// ShopFacade — single access point for all services (Facade pattern)
// Java concept: Composition, encapsulation of subsystems
// ─────────────────────────────────────────────────────────────────────────────
public class ShopFacade {
    private final ProductService products = new ProductService();
    private final UserService    users    = new UserService();
    private final CartService    cart     = new CartService();
    private final OrderService   orders   = new OrderService();

    // ── Products ──────────────────────────────────────────────────────
    public ProductService products() { return products; }

    // ── Auth ──────────────────────────────────────────────────────────
    public String  login(String u, String p)                               { return users.login(u, p); }
    public String  register(String u, String p, String e, String a)        { return users.register(u, p, e, a); }
    public void    logout()                                                 { users.logout(); cart.clear(); }
    public boolean isLoggedIn()                                             { return users.isLoggedIn(); }
    public User    currentUser()                                            { return users.getCurrent(); }
    public String  username()                                               { return users.getUsername(); }

    // ── Cart ──────────────────────────────────────────────────────────
    public String        addToCart(Product p, int qty) { return cart.add(p, qty); }
    public void          removeFromCart(int pid)       { cart.remove(pid); }
    public void          clearCart()                   { cart.clear(); }
    public List<CartItem> cartItems()                  { return cart.getItems(); }
    public double        cartTotal()                   { return cart.getTotal(); }
    public int           cartCount()                   { return cart.getCount(); }
    public boolean       cartIsEmpty()                 { return cart.isEmpty(); }

    // ── Orders ────────────────────────────────────────────────────────
    public Order       placeOrder(String payment)      { return orders.place(username(), cart.getItems(), cart.getTotal(), payment); }
    public List<Order> myOrders()                      { return orders.getByUser(username()); }
}
