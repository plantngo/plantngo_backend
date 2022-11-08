package me.plantngo.backend.DTO;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.*;
import me.plantngo.backend.models.OrderStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderDTO {

    private Boolean isDineIn;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private Set<UpdateOrderItemDTO> updateOrderItemDTOs;

    private LocalDateTime orderTime;
}
