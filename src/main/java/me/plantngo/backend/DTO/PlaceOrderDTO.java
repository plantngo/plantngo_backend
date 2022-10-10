package me.plantngo.backend.DTO;

import javax.validation.constraints.NotNull;

import lombok.*;

@Data
@AllArgsConstructor
public class PlaceOrderDTO {
    
    // Should this be customerName or customerId?
    @NotNull
    private String customerName;

    @NotNull
    private Integer productId;

    @NotNull
    private Integer quantity;
}
