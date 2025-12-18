package com.hbk.fast_food.ui.cashier;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class CashierMainFrame extends JFrame {
    
    // UI Constants
    private static final Color SIDEBAR_BG = new Color(33, 33, 33); // Dark Sidebar
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color ACTIVE_BTN_BG = new Color(255, 152, 0); // Orange
    
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private Map<String, JButton> navButtons;

    public CashierMainFrame() {
        // Window Setup
        setTitle("Fast Food POS System - Cashier");
        setSize(1280, 800);
        setMinimumSize(new Dimension(1024, 768));
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Initialize UI
        initComponent();
    }

    public void initComponent() {
        // 1. Main Container
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // 2. Setup Content Area (CardLayout)
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(new Color(245, 247, 250)); // Match POS BG
        
        // --- ADD PANELS HERE ---
        // POS Panel (The one we fixed earlier)
        contentPanel.add(new MenuPOSPanel(this), "pos");
        
        // Placeholder for Order History (To demonstrate navigation)
        contentPanel.add(new OrderHistoryPanel(), "orders");
        
        // 3. Setup Sidebar
        JPanel sidebar = createSidebar();
        
        // 4. Assemble
        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        
        // Select default view
        showPanel("pos");
    }
    
    // Sidebar logic 

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));
        
        // Brand Title
        JLabel title = new JLabel("FAST FOOD POS");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        sidebar.add(title);
        sidebar.add(Box.createVerticalStrut(40));
        
        // Navigation Buttons
        navButtons = new HashMap<>();
        
        sidebar.add(createNavButton("POS Menu", "pos"));
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(createNavButton("Order History", "orders"));
        
        // Push logout to bottom
        sidebar.add(Box.createVerticalGlue());
        
        JButton logoutBtn = createNavButton("Logout", "logout");
        logoutBtn.setBackground(new Color(198, 40, 40)); // Red
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose(); // Close window
                // new LoginFrame().setVisible(true); // Redirect to login
            }
        });
        sidebar.add(logoutBtn);
        
        return sidebar;
    }
    
    private JButton createNavButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(Color.GRAY);
        btn.setBackground(SIDEBAR_BG);
        btn.setBorder(new EmptyBorder(12, 15, 12, 15));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Remove default Swing look
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        
        if (!cardName.equals("logout")) {
            btn.addActionListener(e -> showPanel(cardName));
            navButtons.put(cardName, btn);
        }
        
        return btn;
    }
    
    // Navigation
    
    public void showPanel(String panelName) {
        // 1. Switch Card
        cardLayout.show(contentPanel, panelName);
        
        // 2. Update Sidebar Visuals (Active State)
        if (navButtons != null) {
            for (Map.Entry<String, JButton> entry : navButtons.entrySet()) {
                JButton btn = entry.getValue();
                if (entry.getKey().equals(panelName)) {
                    btn.setBackground(ACTIVE_BTN_BG);
                    btn.setForeground(Color.WHITE);
                } else {
                    btn.setBackground(SIDEBAR_BG);
                    btn.setForeground(Color.LIGHT_GRAY);
                }
            }
        }
    }
    
    // Main

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            CashierMainFrame frame = new CashierMainFrame();
            frame.setVisible(true);
        });
    }
}
