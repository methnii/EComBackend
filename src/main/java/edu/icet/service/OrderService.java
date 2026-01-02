package edu.icet.service;

import edu.icet.dto.Order;

import java.util.List;

public interface OrderService {
    Order placeOrder(Order order);
    Order updateOrderStatus(Integer orderId, String status);
    List<Order> getOrdersByCustomer(Integer customerId);
    List<Order> getAllOrders();
    Order getOrderById(Integer orderId);
}
