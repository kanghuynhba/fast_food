package com.hbk.fast_food.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * RoundedButton - Modern button with rounded corners and hover effects
 * 
 * Features:
 * - Rounded corners
 * - Smooth hover animation
 * - Color transitions
 * - Icon support
 * 
 * Usage:
 * RoundedButton btn = new RoundedButton("Click Me");
 * btn.setBackground(Color.ORANGE);
 */
public class RoundedButton extends JButton {
    
    private int cornerRadius;
    private Color hoverColor;
    private Color pressedColor;
    private boolean isHovered;
    private boolean isPressed;
    
    // ============ CONSTRUCTORS ============
    
    public RoundedButton(String text) {
        this(text, 10);
    }
    
    public RoundedButton(String text, int cornerRadius) {
        super(text);
        this.cornerRadius = cornerRadius;
        
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setForeground(Color.WHITE);
        
        setupHoverEffect();
    }
    
    // ============ HOVER EFFECT ============
    
    private void setupHoverEffect() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
            }
        });
    }
    
    // ============ CUSTOM PAINTING ============
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Determine color
        Color bgColor = getBackground();
        
        if (isPressed) {
            if (pressedColor != null) {
                bgColor = pressedColor;
            } else {
                bgColor = darken(bgColor, 0.2f);
            }
        } else if (isHovered) {
            if (hoverColor != null) {
                bgColor = hoverColor;
            } else {
                bgColor = brighten(bgColor, 0.1f);
            }
        }
        
        // Draw background
        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);
        
        g2.dispose();
        super.paintComponent(g);
    }
    
    // ============ COLOR UTILS ============
    
    private Color brighten(Color color, float factor) {
        int r = Math.min(255, (int) (color.getRed() + 255 * factor));
        int g = Math.min(255, (int) (color.getGreen() + 255 * factor));
        int b = Math.min(255, (int) (color.getBlue() + 255 * factor));
        return new Color(r, g, b);
    }
    
    private Color darken(Color color, float factor) {
        int r = (int) (color.getRed() * (1 - factor));
        int g = (int) (color.getGreen() * (1 - factor));
        int b = (int) (color.getBlue() * (1 - factor));
        return new Color(r, g, b);
    }
    
    // ============ SETTERS ============
    
    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        repaint();
    }
    
    public void setHoverColor(Color hoverColor) {
        this.hoverColor = hoverColor;
    }
    
    public void setPressedColor(Color pressedColor) {
        this.pressedColor = pressedColor;
    }
}
