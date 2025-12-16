package com.hbk.fast_food.dao;

import java.sql.*;
import com.hbk.fast_food.config.DBConnection;
import com.hbk.fast_food.entity.Order;

public class OrderDAO implements BaseDAO<Order>{
    private static final String TABLE="orders";

    // Create
    @Override
    public boolean create(Order order) {
        String sql = "INSERT INTO " + TABLE + 
                     " (customer_name, phone_number, total_amount, " +
                     "payment_status, status) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, order.getCustomerName());
            ps.setString(2, order.getPhoneNumber());
            ps.setDouble(3, order.getTotalAmount());
            ps.setInt(4, order.getPaymentStatus());
            ps.setInt(5, order.getStatus());
            
            int result = ps.executeUpdate();
            
            if (result > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    order.setOrderId(generatedKeys.getInt(1));
                }
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error creating order: " + e.getMessage());
            return false;
        }
    }
    // Read
    
    @Override
    public Order getById(int id) {
        String sql = "SELECT * FROM " + TABLE + " WHERE order_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToOrder(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting order by ID: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting all orders: " + e.getMessage());
        }
        return orders;
    }
    
    /**
     * Lấy orders theo status
     */
    public List<Order> getByStatus(int status) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE + " WHERE status = ? ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, status);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting orders by status: " + e.getMessage());
        }
        return orders;
    }
    
    // Update

    @Override
    public boolean update(Order order) {
        String sql = "UPDATE " + TABLE + 
                     " SET customer_name = ?, phone_number = ?, total_amount = ?, " +
                     "pay_method = ?, payment_status = ?, status = ?, " +
                     "updated_at = NOW() WHERE order_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, order.getCustomerName());
            ps.setString(2, order.getPhoneNumber());
            ps.setDouble(3, order.getTotalAmount());
            ps.setInt(5, order.getPaymentStatus());
            ps.setInt(6, order.getStatus());
            ps.setInt(8, order.getOrderId());
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating order: " + e.getMessage());
            return false;
        }
    }

    // Delete

}

