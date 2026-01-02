package edu.icet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Integer id;
    private Integer customerId;
    private List<OrderItem> items;
    private String status; // PROCESSING, DELIVERING, DELIVERED
    private Double totalPrice;
}
