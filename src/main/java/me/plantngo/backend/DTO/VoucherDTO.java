package me.plantngo.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class VoucherDTO {

    @NotNull
    private double value;

    @NotNull(message = "description ")
    private String description;
}
