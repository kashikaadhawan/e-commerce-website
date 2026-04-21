package com.shop.ui;

import java.awt.*;

/**
 * Theme — central colour and font constants for the whole app.
 * Java concept: constants (static final), reusable utility class
 */
public class Theme {
    // Light mode colour palette — clean white + slate blue accent
    public static final Color BG_PAGE    = new Color(0xF4F6F9);  // page background
    public static final Color BG_CARD    = new Color(0xFFFFFF);  // card / panel
    public static final Color BG_NAV     = new Color(0xFFFFFF);  // top navbar
    public static final Color BG_INPUT   = new Color(0xFFFFFF);  // input fields
    public static final Color ACCENT     = new Color(0x1D4ED8);  // navy blue
    public static final Color ACCENT_HVR = new Color(0x1E40AF);  // darker on hover
    public static final Color TEXT_DARK  = new Color(0x111827);  // primary text
    public static final Color TEXT_MUTED = new Color(0x6B7280);  // secondary text
    public static final Color SUCCESS    = new Color(0x16A34A);  // green
    public static final Color DANGER     = new Color(0xDC2626);  // red
    public static final Color BORDER     = new Color(0xE5E7EB);  // dividers
    public static final Color BADGE_BG   = new Color(0x1D4ED8);  // cart badge

    // Aliases kept for backward compatibility
    public static final Color BG_DARK    = BG_PAGE;
    public static final Color BG_INPUT_ALIAS = BG_INPUT;
    public static final Color TEXT_WHITE = TEXT_DARK;
    public static final Color ACCENT2    = ACCENT;

    // Fonts
    public static final Font FONT_TITLE  = new Font("SansSerif", Font.BOLD,  22);
    public static final Font FONT_BODY   = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONT_SMALL  = new Font("SansSerif", Font.PLAIN, 11);
    public static final Font FONT_BTN    = new Font("SansSerif", Font.BOLD,  13);
    public static final Font FONT_CARD   = new Font("SansSerif", Font.BOLD,  14);

    private Theme() {} // prevent instantiation
}
