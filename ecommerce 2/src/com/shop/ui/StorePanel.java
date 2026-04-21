package com.shop.ui;

import com.shop.model.Product;
import com.shop.service.ShopFacade;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.List;

/**
 * StorePanel — product grid with category filter and search.
 * Java concept: JPanel grid, dynamic component creation, Swing layout
 */
public class StorePanel extends JPanel {

    private final ShopFacade shop;
    private final Runnable   refreshCart;

    private JTextField        searchField;
    private JComboBox<String> categoryBox;
    private JPanel            grid;

    public StorePanel(ShopFacade shop, Runnable refreshCart) {
        this.shop        = shop;
        this.refreshCart = refreshCart;
        setBackground(Theme.BG_PAGE);
        setLayout(new BorderLayout());
        buildUI();
    }

    private void buildUI() {
        // ── Toolbar ────────────────────────────────────────────────────
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        toolbar.setBackground(Theme.BG_CARD);
        toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER));

        JLabel lbl = Widgets.label("Products", Theme.FONT_CARD, Theme.TEXT_DARK);
        searchField = Widgets.textField("Search products...", 18);
        searchField.setPreferredSize(new Dimension(200, 36));

        List<String> cats = shop.products().getCategories();
        categoryBox = new JComboBox<>(cats.toArray(new String[0]));
        categoryBox.setBackground(Theme.BG_CARD);
        categoryBox.setForeground(Theme.TEXT_DARK);
        categoryBox.setFont(Theme.FONT_BODY);
        categoryBox.setPreferredSize(new Dimension(140, 36));

        JButton btnSearch = Widgets.primaryBtn("Search");
        btnSearch.addActionListener(e -> refresh());
        categoryBox.addActionListener(e -> refresh());

        toolbar.add(lbl);
        toolbar.add(Box.createHorizontalStrut(16));
        toolbar.add(searchField);
        toolbar.add(categoryBox);
        toolbar.add(btnSearch);

        // ── Product grid ───────────────────────────────────────────────
        grid = new JPanel(new GridLayout(0, 3, 14, 14));
        grid.setBackground(Theme.BG_PAGE);
        grid.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBackground(Theme.BG_PAGE);
        scroll.getViewport().setBackground(Theme.BG_PAGE);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        add(toolbar, BorderLayout.NORTH);
        add(scroll,  BorderLayout.CENTER);
        refresh();
    }

    public void refresh() {
        grid.removeAll();
        String kw  = searchField.getText().trim();
        String cat = (String) categoryBox.getSelectedItem();

        List<Product> list;
        if (!kw.isEmpty() && !kw.equals("Search products...")) {
            list = shop.products().search(kw);
        } else {
            list = shop.products().filterByCategory(cat == null ? "All" : cat);
        }

        if (list.isEmpty()) {
            JLabel none = Widgets.label("No products found.", Theme.FONT_BODY, Theme.TEXT_MUTED);
            none.setHorizontalAlignment(SwingConstants.CENTER);
            grid.add(none);
        } else {
            for (Product p : list) grid.add(buildProductCard(p));
        }

        grid.revalidate();
        grid.repaint();
    }

    // ── Single product card ────────────────────────────────────────────
    private JPanel buildProductCard(Product p) {
        JPanel card = Widgets.card(10);
        card.setLayout(new BorderLayout(0, 8));
        card.setBorder(BorderFactory.createEmptyBorder(18, 16, 14, 16));
        card.setPreferredSize(new Dimension(200, 230));

        // Category badge at top
        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topRow.setOpaque(false);
        JLabel catBadge = new JLabel(p.getCategory()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xEFF6FF));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        catBadge.setFont(Theme.FONT_SMALL);
        catBadge.setForeground(Theme.ACCENT);
        catBadge.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        catBadge.setOpaque(false);
        topRow.add(catBadge);

        // Info block
        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));

        JLabel name = Widgets.label(p.getName(), Theme.FONT_CARD, Theme.TEXT_DARK);
        JLabel desc = Widgets.label(
            "<html><body style='width:160px;color:#6B7280'>" + p.getDescription() + "</body></html>",
            Theme.FONT_SMALL, Theme.TEXT_MUTED);
        JLabel price = Widgets.label(
            "Rs. " + String.format("%,.0f", p.getPrice()),
            new Font("SansSerif", Font.BOLD, 16), Theme.ACCENT);
        JLabel stock = Widgets.label(
            p.getStock() > 0 ? "In Stock (" + p.getStock() + ")" : "Out of Stock",
            Theme.FONT_SMALL,
            p.getStock() > 0 ? Theme.SUCCESS : Theme.DANGER);

        info.add(name);  info.add(Box.createVerticalStrut(4));
        info.add(desc);  info.add(Box.createVerticalStrut(8));
        info.add(price); info.add(Box.createVerticalStrut(2));
        info.add(stock);

        // Button
        JButton btn = p.getStock() > 0
                ? Widgets.primaryBtn("Add to Cart")
                : Widgets.ghostBtn("Out of Stock");
        btn.setEnabled(p.getStock() > 0);
        btn.addActionListener(e -> showAddDialog(p));

        card.add(topRow, BorderLayout.NORTH);
        card.add(info,   BorderLayout.CENTER);
        card.add(btn,    BorderLayout.SOUTH);
        return card;
    }

    // ── Add-to-cart dialog ─────────────────────────────────────────────
    private void showAddDialog(Product p) {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add to Cart", true);
        dlg.getContentPane().setBackground(Theme.BG_CARD);
        dlg.setLayout(new BoxLayout(dlg.getContentPane(), BoxLayout.Y_AXIS));
        dlg.getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        dlg.setSize(320, 230);
        dlg.setLocationRelativeTo(this);

        JLabel title  = Widgets.label(p.getName(), Theme.FONT_CARD, Theme.TEXT_DARK);
        JLabel priceL = Widgets.label("Rs. " + String.format("%,.0f", p.getPrice()),
                                      Theme.FONT_BODY, Theme.ACCENT);

        SpinnerNumberModel spinModel = new SpinnerNumberModel(1, 1, p.getStock(), 1);
        JSpinner spinner = new JSpinner(spinModel);
        spinner.setBackground(Theme.BG_INPUT);
        spinner.setFont(Theme.FONT_BODY);
        spinner.setMaximumSize(new Dimension(100, 36));

        JLabel errL = Widgets.label("", Theme.FONT_SMALL, Theme.DANGER);

        JButton confirm = Widgets.primaryBtn("Add to Cart");
        confirm.addActionListener(ev -> {
            int qty = (int) spinner.getValue();
            String err = shop.addToCart(p, qty);
            if (err == null) {
                refreshCart.run();
                dlg.dispose();
                JOptionPane.showMessageDialog(this,
                        p.getName() + " (x" + qty + ") added to cart.",
                        "Cart Updated", JOptionPane.INFORMATION_MESSAGE);
            } else {
                errL.setText(err);
            }
        });

        dlg.add(title);  dlg.add(Box.createVerticalStrut(4));
        dlg.add(priceL); dlg.add(Box.createVerticalStrut(14));
        dlg.add(Widgets.label("Quantity:", Theme.FONT_SMALL, Theme.TEXT_MUTED));
        dlg.add(Box.createVerticalStrut(4));
        dlg.add(spinner); dlg.add(Box.createVerticalStrut(6));
        dlg.add(errL);    dlg.add(Box.createVerticalStrut(14));
        dlg.add(confirm);
        dlg.setVisible(true);
    }
}
