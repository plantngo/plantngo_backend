package me.plantngo.backend.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class LoginDTO {
    @NonNull
    String username;

    @NonNull
    String password;
}
