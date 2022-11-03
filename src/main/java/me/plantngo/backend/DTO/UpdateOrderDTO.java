package me.plantngo.backend.DTO;

import java.util.List;

import lombok.*;
import me.plantngo.backend.models.OrderStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderDTO {
    
    private Boolean isDineIn;

    private OrderStatus orderStatus;

    private List<UpdateOrderItemDTO> updateOrderItemDTOs;
}
