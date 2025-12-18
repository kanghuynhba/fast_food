package com.hbk.fast_food.ui.cashier;

import com.hbk.fast_food.dao.OrderDAO;
import com.hbk.fast_food.dao.OrderItemDAO;
import com.hbk.fast_food.entity.Order;
import com.hbk.fast_food.entity.OrderItem;
import com.hbk.fast_food.entity.Cart;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class PaymentDialog extends JDialog {

    private Cart cart;
    private boolean isPaid = false;

    // UI Inputs
    private JTextField txtName = new JTextField();
    private JTextField txtPhone = new JTextField();
    
    // DAOs
    private OrderDAO orderDAO = new OrderDAO();
    private OrderItemDAO itemDAO = new OrderItemDAO();

    public PaymentDialog(JFrame parent, Cart cart) {
        super(parent, "Checkout", true);
        this.cart = cart;
        
        setSize(400, 400);
        setLocationRelativeTo(parent);
        initUI();
    }

    private void initUI() {
        // Main Panel
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. Form Area (Grid 5 rows, 2 cols)
        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        
        // Name
        form.add(new JLabel("Customer Name:"));
        form.add(txtName);
        
        // Phone
        form.add(new JLabel("Phone Number:"));
        form.add(txtPhone);
        
        // Total Display
        form.add(new JLabel("Total To Pay:"));
        JLabel lblTotal = new JLabel(String.format("%,.0f VND", cart.getTotal()));
        lblTotal.setForeground(Color.BLUE);
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 16));
        form.add(lblTotal);
        
        panel.add(form, BorderLayout.CENTER);

        // 2. Buttons
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());
        
        JButton btnPay = new JButton("Confirm Order");
        btnPay.setBackground(new Color(0, 150, 0));
        btnPay.setForeground(Color.WHITE);
        btnPay.addActionListener(e -> completeOrder());

        btns.add(btnCancel);
        btns.add(btnPay);
        panel.add(btns, BorderLayout.SOUTH);

        add(panel);
    }

    private void completeOrder() {
        // 1. Validation
        String name = txtName.getText().trim();
        if(name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Customer Name is required");
            return;
        }

        // 2. Create Order Object
        Order order = new Order();
        order.setCustomerName(name);
        order.setPhoneNumber(txtPhone.getText().trim());
        order.setTotalAmount(cart.getTotal());
        order.setStatus(1); // 1 = Completed/Paid

        // 3. Save to Database
        if (orderDAO.create(order)) {
            // Save Order Items
            List<OrderItem> items = cart.toOrderItems(order.getOrderId());
            for (OrderItem item : items) {
                itemDAO.create(item);
            }

            isPaid = true;
            JOptionPane.showMessageDialog(this, "Order #" + order.getOrderId() + " created successfully!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Database Error: Could not save order.");
        }
    }

    public boolean isPaid() { return isPaid; }
}
