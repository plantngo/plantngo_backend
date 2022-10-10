package me.plantngo.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UpdateVoucherDTO {

    @NotNull
    private double value;

    /*
    type can be P (percentage-discount) or F (flat-discount)
     */
    @NotNull
    private Character type;

    /*
    with type P, discount can be between 0.00 and 1.00
    with type F, discount can be any number greater than 0.00
     */
    @NotNull
    private Double discount;

    @NotNull(message = "description ")
    private String description;

}
