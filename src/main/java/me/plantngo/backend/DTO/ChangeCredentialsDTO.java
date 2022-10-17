package me.plantngo.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class ChangeCredentialsDTO {

    private String newUsername;

    private String newPassword;

    @Size(min = 5, max = 15, message = "Username must be between 5 and 15 characters long")
    @NotBlank(message = "Username cannot be blank")
    String username;

    @Email(message = "Must be a valid email")
    @NotBlank(message = "Email cannot be blank")
    String email;

    @NotBlank(message = "Password cannot be blank")
    String password;

    @NotNull(message = "Usertype cannot be blank")
    private Character userType;

//    private String company;
//
//    private String logoUrl;
//
//    private String bannerUrl;
//
//    private String address;
//
//    private String description;
//
//    private Double latitude;
//
//    private Double longtitude;
//
//    private String cuisineType;
//
//    private Integer priceRating;
//
//    private String operatingHours;
}
