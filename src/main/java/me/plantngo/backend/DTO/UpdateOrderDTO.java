package me.plantngo.backend.DTO;

import java.util.List;
import java.util.Set;

import lombok.*;
import me.plantngo.backend.models.OrderStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderDTO {
    
    private Boolean isDineIn;

    private OrderStatus orderStatus;

    private Set<UpdateOrderItemDTO> updateOrderItemDTOs;
}
