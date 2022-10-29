package me.plantngo.backend.DTO;

import lombok.*;
import me.plantngo.backend.models.OrderStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderDTO {
    
    private Boolean isDineIn;

    private OrderStatus orderStatus;
}
