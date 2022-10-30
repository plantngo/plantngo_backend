package me.plantngo.backend.DTO;

import javax.validation.constraints.NotNull;

import lombok.*;

@Data
@AllArgsConstructor
public class OrderItemDTO {
    
    @NotNull
    private Integer productId;

    @NotNull
    private Integer quantity;
    
}
