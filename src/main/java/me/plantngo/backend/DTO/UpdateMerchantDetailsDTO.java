package me.plantngo.backend.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMerchantDetailsDTO {

    private String username;

    private String email;

    private String password;

    private String company;

    private String description;

    private String cuisineType;

    private String operatingHours;
}
