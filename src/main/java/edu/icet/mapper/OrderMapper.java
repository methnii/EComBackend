package edu.icet.mapper;

import edu.icet.dto.Order;
import edu.icet.dto.OrderItem;
import edu.icet.entity.OrderEntity;
import edu.icet.entity.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class OrderMapper {

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
        return dto;
    }
}
