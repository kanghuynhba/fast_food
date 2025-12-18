package com.hbk.fast_food.ui.cashier;

import com.hbk.fast_food.dao.OrderDAO;
import com.hbk.fast_food.entity.Order;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderHistoryPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private OrderDAO orderDAO;

    public OrderHistoryPanel() {
        this.orderDAO = new OrderDAO();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(248, 250, 252)); // Light Gray BG
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(248, 250, 252));
        
        JLabel title = new JLabel("Order History");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(37, 99, 235)); // Primary Blue
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadData());
        
        header.add(title, BorderLayout.WEST);
        header.add(refreshBtn, BorderLayout.EAST);
        
        add(header, BorderLayout.NORTH);

        // 2. Table Setup
        String[] columns = {"ID", "Date", "Customer", "Phone", "Total", "Status"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };

        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(Color.WHITE);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        // Center align ID, Phone, Total
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        add(scrollPane, BorderLayout.CENTER);
    }

    public void loadData() {
        model.setRowCount(0); // Clear existing data
        List<Order> orders = orderDAO.getAll(); // Ensure your DAO has getAll()

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        for (Order o : orders) {
            model.addRow(new Object[]{
                o.getOrderId(),
                o.getCreatedAt() != null ? dateFormat.format(o.getCreatedAt()) : "N/A",
                o.getCustomerName(),
                o.getPhoneNumber(),
                currency.format(o.getTotalAmount()),
                getStatusText(o.getStatus())
            });
        }
    }

    private String getStatusText(int status) {
        return switch (status) {
            case 0 -> "Pending";
            case 1 -> "Paid";
            case 2 -> "Cancelled";
            default -> "Unknown";
        };
    }
}
