package me.plantngo.backend.DTO;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.*;
import me.plantngo.backend.models.Product;

@Data
@AllArgsConstructor
public class PromotionDTO {

    @NotBlank
    private String description;

    // @NotBlank
    private String bannerUrl;

    @NotNull
    private Double percentageDiscount;

    @NotBlank
    private String startDate;

    @NotBlank
    private String endDate;

    
    List<Product> products;
}
