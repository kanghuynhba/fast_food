package com.hbk.fast_food.entity;

import com.hbk.fast_food.entity.CartItem;
import com.hbk.fast_food.entity.Order;
import com.hbk.fast_food.entity.OrderItem;                                                  
import com.hbk.fast_food.entity.Product;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Cart extends LinkedHashMap<Integer, CartItem> {

    public void addProduct(Product p) {
       if (this.containsKey(p.getProductId())) {
            increment(p.getProductId());
        } else {
            this.put(p.getProductId(), new CartItem(p, 1));
        }
    }
    
    public void increment(int productId) {
        CartItem item = this.get(productId);
        if (item != null) {
            item.setQuantity(item.getQuantity() + 1);
        }
    }

    public void decrement(int productId) {
        CartItem item = this.get(productId);
        if (item != null) {
            item.setQuantity(item.getQuantity() - 1);
            
            if (item.getQuantity() <= 0) {
                this.remove(productId);
            }
        }
    }

    public void removeProduct(int productId) {
        this.remove(productId);
    }

    public double getSubtotal() {
        return this.values().stream()
                .mapToDouble(CartItem::getLineTotal)
                .sum();
    }

    public double getTotal() {
        return getSubtotal();
    }

   public Order toOrder() {
        Order order = new Order();
        order.setTotalAmount(this.getTotal());
        order.setStatus(0); // 0 = Pending
        return order;
    }

    
    public List<OrderItem> toOrderItems(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        for (CartItem item : this.values()) {
            OrderItem oi = new OrderItem(
                orderId,
                item.getProduct().getProductId(),
                item.getProduct().getName(),
                item.getQuantity()
            );
            items.add(oi);
        }
        return items;
    }
}
