# 🛍️ JavaShop — E-Commerce GUI Mini Project

A full GUI e-commerce desktop app built with **Java Swing** (no external libraries needed).

---

## 📁 Project Structure

```
ecommerce/
└── src/
    └── com/shop/
        ├── model/
        │   ├── Product.java       ← Store item (encapsulation)
        │   ├── User.java          ← Customer account
        │   ├── CartItem.java      ← Cart line item (composition)
        │   └── Order.java         ← Completed purchase
        ├── service/
        │   └── ShopFacade.java    ← All business logic in one place
        │       (contains UserService, CartService, OrderService, ProductService)
        └── ui/
            ├── MainWindow.java    ← JFrame root + navigation ← RUN THIS
            ├── AuthPanel.java     ← Login & Register forms
            ├── StorePanel.java    ← Product grid + search/filter
            ├── CartPanel.java     ← Cart view + checkout
            ├── OrdersPanel.java   ← Order history
            ├── Theme.java         ← Colours & fonts
            └── Widgets.java       ← Reusable styled components
```

---

## ▶️ How to Compile & Run

### Compile
```bash
# From inside the ecommerce/ folder:
javac -d out $(find src -name "*.java")
```

On **Windows** (Command Prompt):
```cmd
dir /s /b src\*.java > sources.txt
javac -d out @sources.txt
```

### Run
```bash
java -cp out com.shop.ui.MainWindow
```

---

## 🔑 Demo Account
| Field    | Value     |
|----------|-----------|
| Username | `demo`    |
| Password | `demo123` |

---

## 🖥️ Screens

| Screen        | Description                                        |
|---------------|----------------------------------------------------|
| Login         | Sign in with username + password                   |
| Register      | Create new account with validation                 |
| Store         | Browse 14 products, filter by category, search     |
| Cart          | View items, remove items, see running total        |
| Checkout      | Choose payment method, get order receipt           |
| Order History | View all past orders with items and totals         |

---

## 🧠 Java Concepts Used

| Concept                    | Where Used                                              |
|----------------------------|---------------------------------------------------------|
| **Encapsulation**          | All model classes — private fields + getters/setters    |
| **Object Composition**     | `CartItem` HAS-A `Product`; `Order` HAS-A `List<CartItem>` |
| **ArrayList**              | Product catalog, cart, order history                    |
| **HashMap**                | User database (username → User)                        |
| **Streams & Lambdas**      | `filter()`, `map()`, `mapToDouble()`, `sum()` in services |
| **Iterator**               | Safe cart item removal                                  |
| **try-catch**              | Input validation, number parsing                        |
| **Static factory methods** | `Widgets.primaryBtn()`, `Widgets.card()`, etc.         |
| **Facade Pattern**         | `ShopFacade` unifies all services                       |
| **CardLayout**             | Switch between Auth and Shop views                      |
| **Swing GUI**              | `JFrame`, `JPanel`, `JButton`, `JLabel`, `JScrollPane` |
| **SwingUtilities.invokeLater** | Correct EDT thread usage                           |
| **LocalDateTime**          | Order timestamps                                        |

---

*All data is stored in-memory — no external database required. Pure Java, zero dependencies.*
