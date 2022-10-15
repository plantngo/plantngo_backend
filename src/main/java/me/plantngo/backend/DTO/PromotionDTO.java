package me.plantngo.backend.DTO;

import java.net.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.*;

@Data
@AllArgsConstructor
public class PromotionDTO {

    @NotBlank
    private String promocode;

    @NotNull
    private Double promoValue;

    private URL url;
}
