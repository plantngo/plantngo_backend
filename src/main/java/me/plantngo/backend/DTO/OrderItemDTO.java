package me.plantngo.backend.DTO;

import javax.validation.constraints.NotNull;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {

    @NotNull
    private Integer productId;

    @NotNull
    private Integer quantity;

}
