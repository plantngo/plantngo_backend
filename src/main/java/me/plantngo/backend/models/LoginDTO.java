package me.plantngo.backend.models;

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

    @NonNull
    Character userType;
}
