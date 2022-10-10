package me.plantngo.backend.DTO;

import javax.validation.constraints.NotBlank;

import lombok.*;

@Data
@AllArgsConstructor
public class LoginDTO {
    @NotBlank
    String username;

    @NotBlank
    String password;
}
