package me.plantngo.backend.DTO;

import lombok.*;

@Data
@AllArgsConstructor
public class UpdateCustomerDetailsDTO {

    private String username;

    private String email;

    private String password;

    // WIP
    // private List<Preference> preferences;
    private Integer greenPoints;

}

