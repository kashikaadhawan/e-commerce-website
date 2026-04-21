package com.shop.ui;

import com.shop.service.ShopFacade;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * AuthPanel — login and registration forms.
 * Java concept: CardLayout for switching between Login/Register views
 */
public class AuthPanel extends JPanel {

    private final ShopFacade shop;
    private final Runnable   onLoginSuccess;

    private JTextField     loginUser, regUser, regEmail, regAddress;
    private JPasswordField loginPass, regPass;
    private JLabel         loginError, regError;
    private CardLayout     cards = new CardLayout();
    private JPanel         cardPanel;

    public AuthPanel(ShopFacade shop, Runnable onLoginSuccess) {
        this.shop           = shop;
        this.onLoginSuccess = onLoginSuccess;
        setBackground(Theme.BG_PAGE);
        setLayout(new GridBagLayout());
        buildUI();
    }

    private void buildUI() {
        cardPanel = new JPanel(cards);
        cardPanel.setOpaque(false);
        cardPanel.add(buildLoginCard(),    "login");
        cardPanel.add(buildRegisterCard(), "register");
        add(cardPanel);
    }

    // ── Login card ────────────────────────────────────────────────────
    private JPanel buildLoginCard() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setOpaque(false);

        JPanel card = Widgets.card(12);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(36, 40, 36, 40));
        card.setPreferredSize(new Dimension(400, 460));

        JLabel logo = Widgets.label("QuickCart", new Font("SansSerif", Font.BOLD, 26), Theme.ACCENT);
        logo.setAlignmentX(CENTER_ALIGNMENT);
        JLabel sub = Widgets.label("Sign in to your account", Theme.FONT_BODY, Theme.TEXT_MUTED);
        sub.setAlignmentX(CENTER_ALIGNMENT);

        loginUser  = Widgets.textField("Username", 20);
        loginPass  = Widgets.passwordField("Password", 20);
        loginError = Widgets.label("", Theme.FONT_SMALL, Theme.DANGER);
        loginError.setAlignmentX(CENTER_ALIGNMENT);

        JButton btnLogin = Widgets.primaryBtn("Sign In");
        btnLogin.setAlignmentX(CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnLogin.addActionListener(e -> doLogin());

        JButton btnSwitch = Widgets.ghostBtn("Create new account");
        btnSwitch.setAlignmentX(CENTER_ALIGNMENT);
        btnSwitch.addActionListener(e -> cards.show(cardPanel, "register"));

        JLabel hint = Widgets.label("Demo account — username: demo   password: demo123",
                                    Theme.FONT_SMALL, Theme.TEXT_MUTED);
        hint.setAlignmentX(CENTER_ALIGNMENT);

        card.add(logo); card.add(Box.createVerticalStrut(4));
        card.add(sub);  card.add(Box.createVerticalStrut(28));
        card.add(fieldBlock("Username", loginUser));
        card.add(Box.createVerticalStrut(14));
        card.add(fieldBlock("Password", loginPass));
        card.add(Box.createVerticalStrut(6));
        card.add(loginError);
        card.add(Box.createVerticalStrut(18));
        card.add(btnLogin);
        card.add(Box.createVerticalStrut(10));
        card.add(btnSwitch);
        card.add(Box.createVerticalStrut(20));
        card.add(Widgets.divider());
        card.add(Box.createVerticalStrut(12));
        card.add(hint);

        outer.add(card);
        return outer;
    }

    // ── Register card ─────────────────────────────────────────────────
    private JPanel buildRegisterCard() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setOpaque(false);

        JPanel card = Widgets.card(12);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(32, 40, 32, 40));
        card.setPreferredSize(new Dimension(400, 520));

        JLabel title = Widgets.label("Create Account", Theme.FONT_TITLE, Theme.TEXT_DARK);
        title.setAlignmentX(CENTER_ALIGNMENT);

        regUser    = Widgets.textField("Choose username", 20);
        regPass    = Widgets.passwordField("Choose password (min 6 chars)", 20);
        regEmail   = Widgets.textField("Email address", 20);
        regAddress = Widgets.textField("Delivery address", 20);
        regError   = Widgets.label("", Theme.FONT_SMALL, Theme.DANGER);
        regError.setAlignmentX(CENTER_ALIGNMENT);

        JButton btnReg = Widgets.primaryBtn("Register");
        btnReg.setAlignmentX(CENTER_ALIGNMENT);
        btnReg.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnReg.addActionListener(e -> doRegister());

        JButton btnBack = Widgets.ghostBtn("Back to Login");
        btnBack.setAlignmentX(CENTER_ALIGNMENT);
        btnBack.addActionListener(e -> cards.show(cardPanel, "login"));

        card.add(title); card.add(Box.createVerticalStrut(24));
        card.add(fieldBlock("Username", regUser));     card.add(Box.createVerticalStrut(12));
        card.add(fieldBlock("Password", regPass));     card.add(Box.createVerticalStrut(12));
        card.add(fieldBlock("Email", regEmail));       card.add(Box.createVerticalStrut(12));
        card.add(fieldBlock("Address", regAddress));   card.add(Box.createVerticalStrut(6));
        card.add(regError);                            card.add(Box.createVerticalStrut(18));
        card.add(btnReg);  card.add(Box.createVerticalStrut(10));
        card.add(btnBack);

        outer.add(card);
        return outer;
    }

    private JPanel fieldBlock(String labelText, JTextField field) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        JLabel lbl = Widgets.label(labelText, Theme.FONT_SMALL, Theme.TEXT_MUTED);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        p.add(lbl); p.add(field);
        return p;
    }

    private void doLogin() {
        String u = loginUser.getText().trim();
        String p = new String(loginPass.getPassword());
        String err = shop.login(u, p);
        if (err == null) { loginError.setText(""); onLoginSuccess.run(); }
        else             { loginError.setText(err); }
    }

    private void doRegister() {
        String u = regUser.getText().trim();
        String p = new String(regPass.getPassword());
        String e = regEmail.getText().trim();
        String a = regAddress.getText().trim();
        String err = shop.register(u, p, e, a);
        if (err == null) {
            regError.setForeground(Theme.SUCCESS);
            regError.setText("Account created. You can now log in.");
            cards.show(cardPanel, "login");
        } else {
            regError.setForeground(Theme.DANGER);
            regError.setText(err);
        }
    }
}
