package me.plantngo.backend.DTO;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class LoginDTO {
    @NonNull
    String username;

    @NonNull
    String password;
}
