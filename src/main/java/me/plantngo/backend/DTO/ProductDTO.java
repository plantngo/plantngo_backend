package me.plantngo.backend.DTO;

import java.net.URL;

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

    private Double carbonEmission;

    // @NotNull
    private URL imageUrl;

    // @NotNull
    private String flavourType;

}
