package me.plantngo.backend.DTO;

import java.util.List;
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
}
