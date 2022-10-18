package me.plantngo.backend.DTO;

import javax.validation.constraints.NotNull;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductIngredientDTO {

    @NotNull
    private String name;

    @NotNull
    private Double servingQty;
}
