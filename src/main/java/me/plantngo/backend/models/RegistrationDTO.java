package me.plantngo.backend.models;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class RegistrationDTO {

    @NonNull
    String name;

    @NonNull
    String email;

    @NonNull
    String password;

    @NonNull
    Character userType;

    String company;
}
