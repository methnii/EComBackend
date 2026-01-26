package edu.icet.service;

import edu.icet.dto.Order;
import edu.icet.dto.OrderItem;
import edu.icet.entity.*;
import edu.icet.mapper.OrderMapper;
import edu.icet.repository.CustomerRepository;
import edu.icet.repository.OrderRepository;
import edu.icet.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    @Override
    public Order placeOrder(Order orderDTO) {

        CustomerEntity customer = customerRepository.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        OrderEntity order = new OrderEntity();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PROCESSING);
        order.setOrderDate(LocalDateTime.now());

        List<OrderItemEntity> items = new ArrayList<>();
        double total = 0;

        for (OrderItem dtoItem : orderDTO.getItems()) {

            ProductEntity product = productRepository.findById(dtoItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getStock() < dtoItem.getQuantity()) {
                throw new RuntimeException("Not enough stock");
            }

            product.setStock(product.getStock() - dtoItem.getQuantity());
            productRepository.save(product);

            OrderItemEntity item = new OrderItemEntity();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(dtoItem.getQuantity());
            item.setPrice(product.getPrice() * dtoItem.getQuantity());

            total += item.getPrice();
            items.add(item);
        }

        order.setTotalPrice(total);
        order.setItems(items);

        OrderEntity saved = orderRepository.save(order);
        return orderMapper.toDTO(saved);
    }

    @Override
    public Order updateOrderStatus(Integer id, String status) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        return orderMapper.toDTO(orderRepository.save(order));
    }

    @Override
    public List<Order> getOrdersByCustomer(Integer customerId) {
        return orderRepository.findByCustomer_Id(customerId)
                .stream()
                .map(orderMapper::toDTO)
                .toList();
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toDTO)
                .toList();
    }

    @Override
    public Order getOrderById(Integer id) {
        return orderMapper.toDTO(
                orderRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Order not found"))
        );
    }
}
