package me.plantngo.backend.DTO;

import javax.validation.constraints.NotNull;

import lombok.*;

@Data
@AllArgsConstructor
public class RegistrationDTO {

    @NotNull
    private String username;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private Character userType;

    private String company;

    private String logoUrl;

    private String bannerUrl;

    private String address;

    private String description;

    private Double latitude;

    private Double longtitude;

    private String cuisineType;

    private Integer priceRating;

    private String operatingHours;
}
