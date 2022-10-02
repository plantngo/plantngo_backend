package me.plantngo.backend.DTO;

import javax.validation.constraints.NotNull;

import lombok.*;

@Data
@AllArgsConstructor
public class RegistrationDTO {

    @NotNull
    String username;

    @NotNull
    String email;

    @NotNull
    String password;

    @NotNull
    Character userType;

    String company;
}
