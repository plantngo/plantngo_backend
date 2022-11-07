package me.plantngo.backend.DTO;

import java.net.URL;

import lombok.*;

@Data
@AllArgsConstructor
public class UpdateProductDTO {

    private String name;

    private Double price;

    private String description;

    private Double carbonEmission;

    private URL imageUrl;

    private String flavourType;
}
