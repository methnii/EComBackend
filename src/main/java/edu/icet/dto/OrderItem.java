package edu.icet.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private Integer productId;
    private Integer quantity;
    private Double price;
    private Product product; // Add this field to include full product details
}