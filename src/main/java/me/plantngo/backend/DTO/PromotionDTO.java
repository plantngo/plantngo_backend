package me.plantngo.backend.DTO;

import java.net.URL;

import javax.validation.constraints.NotBlank;

import lombok.*;

@Data
@AllArgsConstructor
public class PromotionDTO {

    @NotBlank
    private String description;

    // @NotBlank
    private URL bannerUrl;

    @NotBlank
    private String startDate;

    @NotBlank
    private String endDate;

}
