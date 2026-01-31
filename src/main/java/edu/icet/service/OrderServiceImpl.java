package edu.icet.service;

import edu.icet.dto.Order;
import edu.icet.dto.OrderItem;
import edu.icet.dto.Product;
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

        // Convert to DTO and populate product details
        Order savedOrderDTO = orderMapper.toDTO(saved);

        // Populate product details for each item
        for (int i = 0; i < saved.getItems().size(); i++) {
            OrderItemEntity itemEntity = saved.getItems().get(i);
            OrderItem itemDTO = savedOrderDTO.getItems().get(i);

            Product productDTO = new Product();
            productDTO.setId(itemEntity.getProduct().getId());
            productDTO.setName(itemEntity.getProduct().getName());
            productDTO.setDescription(itemEntity.getProduct().getDescription());
            productDTO.setPrice(itemEntity.getProduct().getPrice());
            productDTO.setStock(itemEntity.getProduct().getStock());
            productDTO.setCategory(itemEntity.getProduct().getCategory());
            productDTO.setImageUrl(itemEntity.getProduct().getImageUrl());

            itemDTO.setProduct(productDTO);
        }

        return savedOrderDTO;
    }

    @Override
    public Order updateOrderStatus(Integer id, String status) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        OrderEntity saved = orderRepository.save(order);

        Order orderDTO = orderMapper.toDTO(saved);
        populateProductDetails(orderDTO, saved);
        return orderDTO;
    }

    @Override
    public List<Order> getOrdersByCustomer(Integer customerId) {
        List<OrderEntity> entities = orderRepository.findByCustomer_Id(customerId);
        return entities.stream()
                .map(entity -> {
                    Order orderDTO = orderMapper.toDTO(entity);
                    populateProductDetails(orderDTO, entity);
                    return orderDTO;
                })
                .toList();
    }

    @Override
    public List<Order> getAllOrders() {
        List<OrderEntity> entities = orderRepository.findAll();
        return entities.stream()
                .map(entity -> {
                    Order orderDTO = orderMapper.toDTO(entity);
                    populateProductDetails(orderDTO, entity);
                    return orderDTO;
                })
                .toList();
    }

    @Override
    public Order getOrderById(Integer id) {
        OrderEntity entity = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Order orderDTO = orderMapper.toDTO(entity);
        populateProductDetails(orderDTO, entity);
        return orderDTO;
    }

    private void populateProductDetails(Order orderDTO, OrderEntity entity) {
        for (int i = 0; i < entity.getItems().size(); i++) {
            OrderItemEntity itemEntity = entity.getItems().get(i);
            OrderItem itemDTO = orderDTO.getItems().get(i);

            Product productDTO = new Product();
            productDTO.setId(itemEntity.getProduct().getId());
            productDTO.setName(itemEntity.getProduct().getName());
            productDTO.setDescription(itemEntity.getProduct().getDescription());
            productDTO.setPrice(itemEntity.getProduct().getPrice());
            productDTO.setStock(itemEntity.getProduct().getStock());
            productDTO.setCategory(itemEntity.getProduct().getCategory());
            productDTO.setImageUrl(itemEntity.getProduct().getImageUrl());

            itemDTO.setProduct(productDTO);
        }
    }
}