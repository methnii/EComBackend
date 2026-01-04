package edu.icet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.icet.dto.Order;
import edu.icet.dto.OrderItem;
import edu.icet.entity.*;
import edu.icet.repository.CustomerRepository;
import edu.icet.repository.OrderItemRepository;
import edu.icet.repository.OrderRepository;
import edu.icet.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final ObjectMapper mapper;

    @Override
    public Order placeOrder(Order orderDTO) {
        // Get customer
        CustomerEntity customer = customerRepository.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Create OrderEntity
        OrderEntity order = new OrderEntity();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PROCESSING);
        order.setOrderDate(LocalDateTime.now());

        List<OrderItemEntity> orderItems = new ArrayList<>();
        double totalPrice = 0;

        for (OrderItem itemDTO : orderDTO.getItems()) {
            ProductEntity product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemDTO.getProductId()));

            // Check stock
            if (product.getStock() < itemDTO.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }

            // Reduce stock
            product.setStock(product.getStock() - itemDTO.getQuantity());
            productRepository.save(product);

            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(product.getPrice() * itemDTO.getQuantity());

            totalPrice += orderItem.getPrice();
            orderItems.add(orderItem);
        }

        order.setTotalPrice(totalPrice);
        order.setItems(orderItems);

        // Save order and items
        OrderEntity savedOrder = orderRepository.save(order);
        orderItems.forEach(orderItemRepository::save);

        return mapper.convertValue(savedOrder, Order.class);
    }

    @Override
    public Order updateOrderStatus(Integer orderId, String status) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        OrderEntity updated = orderRepository.save(order);
        return mapper.convertValue(updated, Order.class);
    }

    @Override
    public List<Order> getOrdersByCustomer(Integer customerId) {
        List<OrderEntity> orders = orderRepository.findByCustomerId(customerId);
        return orders.stream()
                .map(order -> mapper.convertValue(order, Order.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> getAllOrders() {
        List<OrderEntity> orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> mapper.convertValue(order, Order.class))
                .collect(Collectors.toList());
    }

    @Override
    public Order getOrderById(Integer orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return mapper.convertValue(order, Order.class);
    }
}
