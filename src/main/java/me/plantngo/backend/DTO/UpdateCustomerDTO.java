package me.plantngo.backend.DTO;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.*;
import me.plantngo.backend.models.Preference;

@Data
@AllArgsConstructor
public class UpdateCustomerDTO {
    
    private String username;
    
    private String email;

    private String password;

    // WIP
    // private List<Preference> preferences;

    private Integer greenPoints;
}

