package me.plantngo.backend.DTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class ProductDTO {
    
    @NotNull
    private String name;

    @NotNull
    private Double price;

    @NotNull
    private String description;

    @NotNull
    private Double carbonEmission;
    
}
