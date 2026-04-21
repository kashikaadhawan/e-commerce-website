package com.shop.ui;

import com.shop.service.ShopFacade;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * MainWindow — the root JFrame and navigation controller.
 *
 * Java concepts demonstrated across the whole project:
 *   - Encapsulation          (all model classes)
 *   - Object Composition     (CartItem HAS-A Product)
 *   - ArrayList and HashMap  (catalog, cart, orders, users)
 *   - Streams and Lambdas    (filter, search, map, sum)
 *   - CardLayout             (Auth vs Shop views)
 *   - Exception handling     (input validation)
 *   - Static factory methods (Widgets class)
 *   - Facade pattern         (ShopFacade)
 *   - Swing GUI              (JFrame, JPanel, JButton, etc.)
 */
public class MainWindow extends JFrame {

    private final ShopFacade shop = new ShopFacade();

    private AuthPanel   authPanel;
    private JPanel      shopPanel;
    private StorePanel  storePanel;
    private CartPanel   cartPanel;
    private OrdersPanel ordersPanel;

    private JButton tabStore, tabCart, tabOrders;
    private JLabel  cartBadge;

    private CardLayout rootCards = new CardLayout();
    private JPanel     rootPanel;

    public MainWindow() {
        super("QuickCart — E-Commerce");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(980, 700);
        setMinimumSize(new Dimension(820, 580));
        setLocationRelativeTo(null);
        applyLightFrame();
        buildRoot();
        showAuth();
        setVisible(true);
    }

    private void applyLightFrame() {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        getContentPane().setBackground(Theme.BG_PAGE);
    }

    private void buildRoot() {
        rootPanel = new JPanel(rootCards);
        rootPanel.setBackground(Theme.BG_PAGE);

        authPanel = new AuthPanel(shop, this::showShop);
        rootPanel.add(authPanel, "auth");

        shopPanel = buildShopPanel();
        rootPanel.add(shopPanel, "shop");

        setContentPane(rootPanel);
    }

    private JPanel buildShopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BG_PAGE);

        CardLayout contentCards = new CardLayout();
        JPanel content = new JPanel(contentCards);
        content.setBackground(Theme.BG_PAGE);

        storePanel  = new StorePanel(shop, this::refreshCartBadge);
        cartPanel   = new CartPanel(shop, () -> { ordersPanel.refresh(); refreshCartBadge(); });
        ordersPanel = new OrdersPanel(shop);

        content.add(storePanel,  "store");
        content.add(cartPanel,   "cart");
        content.add(ordersPanel, "orders");

        JPanel navbar = buildNavbar(contentCards, content);

        panel.add(navbar,  BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    // ── Navbar ─────────────────────────────────────────────────────────
    private JPanel buildNavbar(CardLayout cards, JPanel content) {
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(Theme.BG_NAV);
        nav.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER));
        nav.setPreferredSize(new Dimension(0, 54));

        // Brand
        JLabel logo = Widgets.label("  QuickCart",
                new Font("SansSerif", Font.BOLD, 17), Theme.ACCENT);

        // Navigation tabs
        JPanel tabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 10));
        tabs.setOpaque(false);

        tabStore  = navTab("Store");
        tabCart   = navTab("Cart");
        tabOrders = navTab("My Orders");

        // Cart count badge
        cartBadge = new JLabel("0");
        cartBadge.setFont(new Font("SansSerif", Font.BOLD, 10));
        cartBadge.setForeground(Color.WHITE);
        cartBadge.setOpaque(true);
        cartBadge.setBackground(Theme.BADGE_BG);
        cartBadge.setBorder(BorderFactory.createEmptyBorder(1, 5, 1, 5));
        cartBadge.setVisible(false);

        JPanel cartWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        cartWrapper.setOpaque(false);
        cartWrapper.add(tabCart);
        cartWrapper.add(cartBadge);

        tabs.add(tabStore); tabs.add(cartWrapper); tabs.add(tabOrders);

        // Right side — user greeting + logout
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        right.setOpaque(false);
        JLabel userLbl = Widgets.label("", Theme.FONT_SMALL, Theme.TEXT_MUTED);
        JButton logout = Widgets.outlineBtn("Sign Out");

        tabStore.addActionListener(e  -> { cards.show(content, "store");  highlightTab(tabStore); });
        tabCart.addActionListener(e   -> { cartPanel.refresh(); cards.show(content, "cart"); highlightTab(tabCart); });
        tabOrders.addActionListener(e -> { ordersPanel.refresh(); cards.show(content, "orders"); highlightTab(tabOrders); });

        logout.addActionListener(e -> { shop.logout(); showAuth(); });

        nav.putClientProperty("userLbl", userLbl);

        right.add(userLbl); right.add(logout);
        nav.add(logo, BorderLayout.WEST);
        nav.add(tabs, BorderLayout.CENTER);
        nav.add(right, BorderLayout.EAST);

        highlightTab(tabStore);
        return nav;
    }

    private JButton navTab(String label) {
        JButton b = new JButton(label);
        b.setFont(Theme.FONT_BTN);
        b.setForeground(Theme.TEXT_MUTED);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
        return b;
    }

    private void highlightTab(JButton active) {
        for (JButton t : new JButton[]{tabStore, tabCart, tabOrders}) {
            t.setForeground(t == active ? Theme.ACCENT : Theme.TEXT_MUTED);
            t.setFont(t == active
                    ? new Font("SansSerif", Font.BOLD, 13)
                    : Theme.FONT_BTN);
        }
    }

    private void showAuth() {
        rootCards.show(rootPanel, "auth");
    }

    private void showShop() {
        Component navbar = ((BorderLayout) shopPanel.getLayout()).getLayoutComponent(BorderLayout.NORTH);
        if (navbar instanceof JPanel) {
            JLabel lbl = (JLabel) ((JPanel) navbar).getClientProperty("userLbl");
            if (lbl != null) lbl.setText("Signed in as " + shop.username() + "   |   ");
        }
        storePanel.refresh();
        refreshCartBadge();
        rootCards.show(rootPanel, "shop");
        highlightTab(tabStore);
    }

    private void refreshCartBadge() {
        int count = shop.cartCount();
        cartBadge.setText(String.valueOf(count));
        cartBadge.setVisible(count > 0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }
}
