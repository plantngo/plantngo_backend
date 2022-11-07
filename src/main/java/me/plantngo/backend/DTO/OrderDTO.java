package me.plantngo.backend.DTO;

import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

import lombok.*;
import me.plantngo.backend.models.OrderStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    @NotNull
    private Boolean isDineIn;

    @NotNull
    private String merchantName;

    @NotNull
    private OrderStatus orderStatus;

    @NotNull
    private List<OrderItemDTO> orderItems;

    private LocalDateTime orderTime;
}
