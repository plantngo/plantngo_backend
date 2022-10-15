package me.plantngo.backend.DTO;

import javax.validation.constraints.NotNull;

import lombok.*;

@Data
@AllArgsConstructor
public class UpdateOrderItemDTO {

    @NotNull
    private Integer productId;

    private Integer quantity;
}
