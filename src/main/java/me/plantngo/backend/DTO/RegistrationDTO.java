package me.plantngo.backend.DTO;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.*;

@Data
@AllArgsConstructor
public class RegistrationDTO {

    @Size(min = 5, max = 15, message = "Username must be between 5 and 15 characters long")
    @NotBlank(message = "Username cannot be blank")
    String username;

    @Email(message = "Must be a valid email")
    @NotBlank(message = "Email cannot be blank")
    String email;

    @NotBlank(message = "Password cannot be blank")
    String password;

    @NotNull(message = "Usertype cannot be blank")
    Character userType;

    String company;
}
