
# QuickCart — E-Commerce Desktop Application

A simple desktop e-commerce application built with Java and Java Swing.
No external libraries or frameworks required.

---

## Project Files

| File | Description |
|---|---|
| `Models.java` | Data classes — Product, CartItem, Order, User, and custom exception |
| `ShopService.java` | All business logic — catalog, cart, order processing, background thread |
| `QuickCart.java` | Full Swing GUI — main entry point |

---

## How to Compile and Run

Make sure all three files are in the same folder.

**Compile:**
```
javac Models.java ShopService.java QuickCart.java
```

**Run:**
```
java QuickCart
```

Requires **Java 16 or higher** (uses switch expressions).

---

## Features

- Browse a catalog of 7 products
- Add items to cart with quantity selection
- Remove items from cart
- View running total
- Checkout with payment method selection
- Order receipt displayed after purchase

---

## Java Concepts Used

| Concept | Where |
|---|---|
| Interface | `Purchasable` interface in Models.java |
| Dynamic Polymorphism | `Product implements Purchasable`; cart uses `Purchasable` type |
| Custom Exception | `CartException` — thrown on invalid cart/order operations |
| try / catch / throw / throws / finally | `addToCart()` and `placeOrder()` in ShopService.java |
| Multithreading (Producer-Consumer) | `OrderProcessor implements Runnable`; orders queued and processed in background |
| ArrayList / HashMap | Product catalog, cart, order list, user store |
| Streams & Lambdas | Cart total, order filtering |
| Java Swing | JFrame, JPanel, JButton, CardLayout, JDialog, JScrollPane |

---

## How It Works

1. App opens directly on the Store screen
2. Click **Add to Cart** on any product — enter quantity in the dialog
3. Click the **Cart** tab to review items
4. Click **Checkout** — select a payment method — receipt is shown
5. Background thread processes the order and logs it to the console

---

*Single-module project. All data is stored in memory — no database needed.*
