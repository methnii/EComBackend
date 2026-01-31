package edu.icet.mapper;

import edu.icet.dto.Order;
import edu.icet.dto.OrderItem;
import edu.icet.dto.Product; // Add this import
import edu.icet.entity.OrderEntity;
import edu.icet.entity.OrderItemEntity;
import com.fasterxml.jackson.databind.ObjectMapper; // Add this import
import lombok.RequiredArgsConstructor; // Add this import
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor // Add this annotation
public class OrderMapper {

    private final ObjectMapper objectMapper; // Add this field

    public Order toDTO(OrderEntity entity) {
        Order dto = new Order();
        dto.setId(entity.getId());
        dto.setCustomerName(entity.getCustomer().getName());
        dto.setCustomerId(entity.getCustomer().getId());
        dto.setStatus(entity.getStatus().name());
        dto.setTotalPrice(entity.getTotalPrice());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        dto.setOrderDate(entity.getOrderDate().format(formatter));

        List<OrderItem> items = entity.getItems().stream().map(this::toDTO).toList();
        dto.setItems(items);

        return dto;
    }

    private OrderItem toDTO(OrderItemEntity entity) {
        OrderItem dto = new OrderItem();
        dto.setProductId(entity.getProduct().getId());
        dto.setQuantity(entity.getQuantity());
        dto.setPrice(entity.getPrice());

        // Create a Product DTO with image
        Product productDto = new Product();
        productDto.setId(entity.getProduct().getId());
        productDto.setName(entity.getProduct().getName());
        productDto.setDescription(entity.getProduct().getDescription());
        productDto.setPrice(entity.getProduct().getPrice());
        productDto.setCategory(entity.getProduct().getCategory());
        productDto.setImageUrl(entity.getProduct().getImageUrl());

        // You can set the product object if needed
        // dto.setProduct(productDto);

        return dto;
    }
}