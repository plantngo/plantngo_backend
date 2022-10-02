package me.plantngo.backend.DTO;

import javax.validation.constraints.NotNull;

import lombok.*;

@Data
@AllArgsConstructor
public class LoginDTO {
    @NotNull
    String username;

    @NotNull
    String password;
}
