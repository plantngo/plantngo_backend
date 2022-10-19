package me.plantngo.backend.DTO;

import lombok.*;

@Data
@AllArgsConstructor
public class UpdateMerchantDetailsDTO {

    private String username;

    private String email;

    private String password;

    private String company;

}

