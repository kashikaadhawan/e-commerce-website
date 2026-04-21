package com.shop.ui;

import com.shop.model.*;
import com.shop.service.ShopFacade;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

/**
 * OrdersPanel — shows all past orders for the logged-in user.
 * Java concept: JPanel list rendering, iterating collections
 */
public class OrdersPanel extends JPanel {

    private final ShopFacade shop;
    private JPanel listPanel;

    public OrdersPanel(ShopFacade shop) {
        this.shop = shop;
        setBackground(Theme.BG_PAGE);
        setLayout(new BorderLayout());
        buildUI();
    }

    private void buildUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BG_CARD);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)));
        header.add(Widgets.label("Order History", Theme.FONT_CARD, Theme.TEXT_DARK));

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Theme.BG_PAGE);
        listPanel.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.getViewport().setBackground(Theme.BG_PAGE);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        add(header, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    public void refresh() {
        listPanel.removeAll();
        List<Order> orders = shop.myOrders();

        if (orders.isEmpty()) {
            listPanel.add(Box.createVerticalStrut(40));
            JLabel none = Widgets.label("No orders placed yet.",
                    Theme.FONT_BODY, Theme.TEXT_MUTED);
            none.setAlignmentX(CENTER_ALIGNMENT);
            listPanel.add(none);
        } else {
            for (int i = orders.size() - 1; i >= 0; i--) {
                listPanel.add(buildOrderCard(orders.get(i)));
                listPanel.add(Box.createVerticalStrut(12));
            }
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel buildOrderCard(Order o) {
        JPanel card = Widgets.card(10);
        card.setLayout(new BorderLayout(0, 10));
        card.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 999));

        // Top row: order ID, status, date
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        JLabel orderId = Widgets.label("Order #" + o.getOrderId(), Theme.FONT_CARD, Theme.TEXT_DARK);

        JLabel status = new JLabel(o.getStatus()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xDCFCE7));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        status.setFont(Theme.FONT_SMALL);
        status.setForeground(Theme.SUCCESS);
        status.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        status.setOpaque(false);

        JLabel date = Widgets.label(o.getDate(), Theme.FONT_SMALL, Theme.TEXT_MUTED);
        topRow.add(orderId, BorderLayout.WEST);
        topRow.add(status,  BorderLayout.CENTER);
        topRow.add(date,    BorderLayout.EAST);

        // Divider
        JSeparator sep = Widgets.divider();

        // Items
        JPanel items = new JPanel();
        items.setOpaque(false);
        items.setLayout(new BoxLayout(items, BoxLayout.Y_AXIS));
        for (CartItem ci : o.getItems()) {
            String line = String.format("%-26s x%d     Rs. %,.2f",
                    ci.getProduct().getName(), ci.getQuantity(), ci.getSubtotal());
            items.add(Widgets.label(line, Theme.FONT_BODY, Theme.TEXT_DARK));
            items.add(Box.createVerticalStrut(3));
        }

        // Bottom row: payment + total
        JPanel botRow = new JPanel(new BorderLayout());
        botRow.setOpaque(false);
        botRow.add(Widgets.label("Payment: " + o.getPaymentMethod(),
                Theme.FONT_SMALL, Theme.TEXT_MUTED), BorderLayout.WEST);
        botRow.add(Widgets.label("Total: Rs. " + String.format("%,.2f", o.getTotal()),
                new Font("SansSerif", Font.BOLD, 14), Theme.TEXT_DARK), BorderLayout.EAST);

        card.add(topRow, BorderLayout.NORTH);
        card.add(items,  BorderLayout.CENTER);
        card.add(botRow, BorderLayout.SOUTH);
        return card;
    }
}
