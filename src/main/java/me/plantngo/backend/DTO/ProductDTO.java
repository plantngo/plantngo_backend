package me.plantngo.backend.DTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.*;

@Data
@AllArgsConstructor
public class ProductDTO {
    
    @NotNull
    private String name;

    @NotNull
    private Double price;

    @NotBlank
    private String description;

    @NotNull
    private Double carbonEmission;
    
    // @NotNull
    private String imageUrl;

    // @NotNull
    private String flavourType;

}
