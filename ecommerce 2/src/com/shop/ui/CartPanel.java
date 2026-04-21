package com.shop.ui;

import com.shop.model.*;
import com.shop.service.ShopFacade;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

/**
 * CartPanel — displays cart items and handles checkout.
 * Java concept: iterating lists, dialog boxes, dynamic UI updates
 */
public class CartPanel extends JPanel {

    private final ShopFacade shop;
    private final Runnable   onOrderPlaced;

    private JPanel itemsPanel;
    private JLabel totalLabel;

    public CartPanel(ShopFacade shop, Runnable onOrderPlaced) {
        this.shop          = shop;
        this.onOrderPlaced = onOrderPlaced;
        setBackground(Theme.BG_PAGE);
        setLayout(new BorderLayout());
        buildUI();
    }

    private void buildUI() {
        // ── Header ─────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BG_CARD);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)));
        header.add(Widgets.label("Shopping Cart", Theme.FONT_CARD, Theme.TEXT_DARK), BorderLayout.WEST);

        // ── Items list ─────────────────────────────────────────────────
        itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBackground(Theme.BG_PAGE);
        itemsPanel.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JScrollPane scroll = new JScrollPane(itemsPanel);
        scroll.getViewport().setBackground(Theme.BG_PAGE);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        // ── Footer ─────────────────────────────────────────────────────
        JPanel footer = new JPanel(new BorderLayout(12, 0));
        footer.setBackground(Theme.BG_CARD);
        footer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER),
            BorderFactory.createEmptyBorder(14, 16, 14, 16)));

        totalLabel = Widgets.label("Total: Rs. 0.00",
                new Font("SansSerif", Font.BOLD, 16), Theme.TEXT_DARK);
        JButton btnCheckout = Widgets.primaryBtn("Proceed to Checkout");
        btnCheckout.addActionListener(e -> showCheckout());

        footer.add(totalLabel,  BorderLayout.WEST);
        footer.add(btnCheckout, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    public void refresh() {
        itemsPanel.removeAll();
        List<CartItem> items = shop.cartItems();

        if (items.isEmpty()) {
            itemsPanel.add(Box.createVerticalStrut(40));
            JLabel empty = Widgets.label(
                    "Your cart is empty. Browse the store to add items.",
                    Theme.FONT_BODY, Theme.TEXT_MUTED);
            empty.setAlignmentX(CENTER_ALIGNMENT);
            itemsPanel.add(empty);
        } else {
            for (CartItem ci : items) {
                itemsPanel.add(buildRow(ci));
                itemsPanel.add(Box.createVerticalStrut(8));
            }
        }

        totalLabel.setText("Total: Rs. " + String.format("%,.2f", shop.cartTotal()));
        itemsPanel.revalidate();
        itemsPanel.repaint();
    }

    // ── Cart row ───────────────────────────────────────────────────────
    private JPanel buildRow(CartItem ci) {
        JPanel row = Widgets.card(8);
        row.setLayout(new BorderLayout(12, 0));
        row.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));

        // Left — product name and qty/price info
        JPanel textBlock = new JPanel();
        textBlock.setOpaque(false);
        textBlock.setLayout(new BoxLayout(textBlock, BoxLayout.Y_AXIS));
        textBlock.add(Widgets.label(ci.getProduct().getName(), Theme.FONT_CARD, Theme.TEXT_DARK));
        textBlock.add(Widgets.label(
                "Qty: " + ci.getQuantity() + "   x   Rs. " +
                String.format("%,.0f", ci.getProduct().getPrice()),
                Theme.FONT_SMALL, Theme.TEXT_MUTED));

        // Right — subtotal + remove button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(Widgets.label(
                "Rs. " + String.format("%,.2f", ci.getSubtotal()),
                new Font("SansSerif", Font.BOLD, 14), Theme.TEXT_DARK));
        JButton rm = Widgets.dangerBtn("Remove");
        rm.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        rm.addActionListener(e -> { shop.removeFromCart(ci.getProduct().getId()); refresh(); });
        rightPanel.add(rm);

        row.add(textBlock,  BorderLayout.CENTER);
        row.add(rightPanel, BorderLayout.EAST);
        return row;
    }

    // ── Checkout dialog ────────────────────────────────────────────────
    private void showCheckout() {
        if (shop.cartIsEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Your cart is empty.", "Cannot Checkout", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] methods = {"Cash on Delivery", "UPI", "Credit / Debit Card", "Net Banking"};
        String payment = (String) JOptionPane.showInputDialog(
            this, "Select Payment Method:", "Checkout",
            JOptionPane.PLAIN_MESSAGE, null, methods, methods[0]);
        if (payment == null) return;

        Order order = shop.placeOrder(payment);
        if (order == null) {
            JOptionPane.showMessageDialog(this,
                    "Order could not be placed — insufficient stock.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Build receipt
        StringBuilder sb = new StringBuilder();
        sb.append("  Order #").append(order.getOrderId()).append("  —  Confirmed\n\n");
        sb.append("  Date        : ").append(order.getDate()).append("\n");
        sb.append("  Payment     : ").append(order.getPaymentMethod()).append("\n\n");
        sb.append("  ").append("-".repeat(44)).append("\n");
        sb.append("  Items:\n");
        for (CartItem ci : order.getItems()) {
            sb.append(String.format("    %-24s x%d   Rs.%.2f%n",
                    ci.getProduct().getName(), ci.getQuantity(), ci.getSubtotal()));
        }
        sb.append("  ").append("-".repeat(44)).append("\n");
        sb.append(String.format("  Total       :  Rs. %.2f%n", order.getTotal()));
        sb.append("  Status      : ").append(order.getStatus()).append("\n");

        JTextArea receipt = new JTextArea(sb.toString());
        receipt.setEditable(false);
        receipt.setBackground(Color.WHITE);
        receipt.setForeground(Theme.TEXT_DARK);
        receipt.setFont(new Font("Monospaced", Font.PLAIN, 13));
        receipt.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JOptionPane.showMessageDialog(this, new JScrollPane(receipt),
                "Order Placed Successfully", JOptionPane.INFORMATION_MESSAGE);

        shop.clearCart();
        refresh();
        onOrderPlaced.run();
    }
}
