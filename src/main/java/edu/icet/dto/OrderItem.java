package edu.icet.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private Integer productId;
    private Integer quantity;
    private Double price; // product price * quantity


}
