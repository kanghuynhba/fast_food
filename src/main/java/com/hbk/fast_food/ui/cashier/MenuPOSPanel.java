package com.hbk.fast_food.ui.cashier;

import com.hbk.fast_food.dao.ProductDAO;
import com.hbk.fast_food.dao.OrderDAO;
import com.hbk.fast_food.dao.OrderItemDAO;
import com.hbk.fast_food.entity.Product;
import com.hbk.fast_food.entity.Order;
import com.hbk.fast_food.entity.OrderItem;
import com.hbk.fast_food.entity.CartItem;
import com.hbk.fast_food.entity.Cart;
import com.hbk.fast_food.ui.components.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MenuPOSPanel extends JPanel {
    
    // Simple Color Palette
    private static final Color PRIMARY_COLOR = new Color(37, 99, 235); // Royal Blue
    private static final Color BG_COLOR      = new Color(248, 250, 252);
    private static final Color TEXT_DARK     = new Color(33, 33, 33);    

    // Dependencies
    private CashierMainFrame mainFrame;
    private ProductDAO productDAO;
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    
    // UI Components
    private JPanel productsContainer;
    private JPanel cartPanel;
    private JLabel totalLabel;
    private JComboBox<String> customerCombo;
    
    // Data
    private List<Product> products;
    private Cart cart;
    private String selectedCategory = "All";

    public MenuPOSPanel(CashierMainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.productDAO = new ProductDAO();
        this.orderDAO = new OrderDAO();
        this.orderItemDAO = new OrderItemDAO();
        this.cart = new Cart();
        
        initComponents();
        loadProducts();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        // --- LEFT: Product Catalog ---
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setBackground(BG_COLOR);
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // 1. Category Buttons
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        categoryPanel.setBackground(BG_COLOR);
        String[] categories = {"All", "Burger", "Pizza", "Chicken", "Combo", "Sides", "Drinks"};
        
        for (String cat : categories) {
            JButton btn = new JButton(cat);
            btn.addActionListener(e -> {
                selectedCategory = cat;
                filterProducts();
            });
            categoryPanel.add(btn);
        }
        leftPanel.add(categoryPanel, BorderLayout.NORTH);

        // 2. Product Grid
        productsContainer = new JPanel(new GridLayout(0, 3, 10, 10));
        productsContainer.setBackground(BG_COLOR);
        
        JScrollPane scrollPane = new JScrollPane(productsContainer);
        scrollPane.setBorder(null);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        // ... inside initComponents() ...

        // --- RIGHT: Cart ---
        JPanel rightPanel = new JPanel(new BorderLayout());
        // FIXED: Increased width from 50 to 380 so it fits buttons and text
        rightPanel.setPreferredSize(new Dimension(380, 0)); 
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(230, 230, 230)));

        // 3. Cart Header
        JPanel header = new JPanel(new BorderLayout(5, 5));
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(20, 20, 10, 20));
        
        JLabel cartTitle = new JLabel("Current Order");
        cartTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        header.add(cartTitle, BorderLayout.NORTH);
        
        customerCombo = new JComboBox<>(new String[]{"Dine In", "Take Away"});
        customerCombo.setBackground(Color.WHITE);
        header.add(customerCombo, BorderLayout.CENTER);
        
        rightPanel.add(header, BorderLayout.NORTH);

        // 4. Cart Items List
        cartPanel = new JPanel();
        cartPanel.setLayout(new BoxLayout(cartPanel, BoxLayout.Y_AXIS));
        cartPanel.setBackground(Color.WHITE);
        
        JScrollPane cartScroll = new JScrollPane(cartPanel);
        cartScroll.setBorder(null);
        cartScroll.getVerticalScrollBar().setUnitIncrement(16);
        cartScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        rightPanel.add(cartScroll, BorderLayout.CENTER);

        // 5. Cart Footer (Total & Button)
        JPanel footer = new JPanel(new BorderLayout(10, 10));
        footer.setBackground(Color.WHITE);
        footer.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Separator line
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(230, 230, 230));
        
        // Total Text
        totalLabel = new JLabel("Total: 0 VND");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        totalLabel.setForeground(PRIMARY_COLOR);
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        JButton payBtn = new JButton("PAY NOW");
        payBtn.setBackground(PRIMARY_COLOR);
        payBtn.setForeground(Color.BLACK);
        payBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        payBtn.setPreferredSize(new Dimension(0, 50));
        payBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        payBtn.addActionListener(e -> placeOrder());
        
        JPanel footerContent = new JPanel(new GridLayout(2, 1, 10, 10));
        footerContent.setBackground(Color.WHITE);
        footerContent.add(totalLabel);
        footerContent.add(payBtn);
        
        footer.add(sep, BorderLayout.NORTH);
        footer.add(footerContent, BorderLayout.CENTER);
        
        rightPanel.add(footer, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    // ============ LOGIC METHODS ============

    private void loadProducts() {
        products = productDAO.getAllAvailable();
        displayProducts(products);
    }

    private void filterProducts() {
        if (selectedCategory.equals("All")) {
            displayProducts(products);
        } else {
            List<Product> filtered = products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(selectedCategory))
                .toList();
            displayProducts(filtered);
        }
    }

    private void displayProducts(List<Product> list) {
        productsContainer.removeAll();
        for (Product p : list) {
            productsContainer.add(createProductCard(p));
        }
        productsContainer.revalidate();
        productsContainer.repaint();
    }

    // Creates the simple visual card for a product
    private JPanel createProductCard(Product p) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setPreferredSize(new Dimension(150, 150));

        // Simple Placeholder (Colored Box with Letter)
        JLabel imagePlaceholder = new JLabel(p.getName().substring(0, 1));
        imagePlaceholder.setFont(new Font("SansSerif", Font.BOLD, 40));
        imagePlaceholder.setHorizontalAlignment(SwingConstants.CENTER);
        imagePlaceholder.setOpaque(true);
        imagePlaceholder.setBackground(new Color(230, 240, 255)); // Light Blue
        imagePlaceholder.setPreferredSize(new Dimension(0, 100));

        // Info
        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setBackground(Color.WHITE);

        info.add(new ProductLabel(p.getName(), Color.BLACK));

        JLabel priceLbl = new ProductLabel(formatMoney(p.getPrice()), Color.BLACK);
        info.add(priceLbl);

        card.add(imagePlaceholder, BorderLayout.CENTER);
        card.add(info, BorderLayout.SOUTH);

        // Click to Add
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                addToCart(p);
            }
        });

        return card;
    }

    private void addToCart(Product p) {
        cart.addProduct(p);
        updateCartUI();
    }

    private void updateCartUI() {
        cartPanel.removeAll();

        // Loop using Cart's values (LinkedHashMap preserves order)
        for (CartItem item : cart.values()) {
            cartPanel.add(createCartRow(item));
        }

        totalLabel.setText("Total: " + formatMoney(cart.getTotal()));
        cartPanel.revalidate();
        cartPanel.repaint();
    }

    private JPanel createCartRow(CartItem item) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(Color.WHITE);
        // Bottom border for separation
        row.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)),
            new EmptyBorder(10, 10, 10, 10)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));

        // --- LEFT: Name & Price ---
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel lblName = new JLabel(item.getProduct().getName());
        lblName.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblName.setForeground(TEXT_DARK);
        
        JLabel lblPrice = new JLabel(formatMoney(item.getLineTotal()));
        lblPrice.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblPrice.setForeground(PRIMARY_COLOR);
        
        infoPanel.add(lblName);
        infoPanel.add(lblPrice);
        
        // --- RIGHT: Quantity Controls ---
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        controls.setBackground(Color.WHITE);

        // Minus
        JButton btnMinus = createCircleButton("-", new Color(241, 245, 249), new Color(71, 85, 105));
        btnMinus.addActionListener(e -> {
            cart.decrement(item.getProduct().getProductId());
            updateCartUI();
        });

        // Quantity
        JLabel lblQty = new JLabel(String.valueOf(item.getQuantity()));
        lblQty.setForeground(Color.BLACK);
        lblQty.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblQty.setHorizontalAlignment(SwingConstants.CENTER);
        lblQty.setPreferredSize(new Dimension(35, 32));

        // Plus
        JButton btnPlus = createCircleButton("+", new Color(241, 245, 249), new Color(71, 85, 105));
        if (item.getQuantity() >= 20) {
            btnPlus.setEnabled(false); // Make it unclickable and gray
            btnPlus.setToolTipText("Maximum quantity reached (20)");
        } else {
            btnPlus.addActionListener(e -> {
                cart.increment(item.getProduct().getProductId());
                updateCartUI();
            });
        }

        controls.add(btnMinus);
        controls.add(lblQty);
        controls.add(btnPlus);

        row.add(infoPanel, BorderLayout.CENTER);
        row.add(controls, BorderLayout.EAST);
        
        return row;
    }

    private JButton createCircleButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 1. Determine Colors based on State
                Color paintBg = bg;
                Color paintFg = fg;
                
                if (!isEnabled()) {
                    // Disabled State: Light Gray BG, Dark Gray Text
                    paintBg = new Color(224, 224, 224); 
                    paintFg = new Color(158, 158, 158);
                } else if (getModel().isPressed()) {
                    // Pressed State: Darker BG
                    paintBg = bg.darker();
                }

                // 2. Draw Circle Background
                g2.setColor(paintBg);
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);

                // 3. Draw Text Centered
                g2.setColor(paintFg);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                String label = getText();
                
                int x = (getWidth() - fm.stringWidth(label)) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;

                g2.drawString(label, x, y);
                g2.dispose();
            }
        };

        btn.setPreferredSize(new Dimension(32, 32));
        btn.setFont(new Font("SansSerif", Font.BOLD, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorder(null);
        
        return btn;    
    }

    private void placeOrder() {
        // 1. Just check if cart is empty
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty!");
            return;
        }

        // 2. Open the Dialog passing the Cart
        PaymentDialog dialog = new PaymentDialog(mainFrame, cart);
        dialog.setVisible(true);

        // 3. Check if successful
        if (dialog.isPaid()) {
            cart.clear(); // Clear the map
            updateCartUI(); // Reset the UI
        }
    }

    private String formatMoney(double amount) {
        return NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(amount);
    }

}
