package me.plantngo.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class ResetPasswordDTO {

    @NotBlank
    String email;

    /*
    To be included in the checking method; i.e. only after customer has typed in their reset password token
     */
    @Size(min = 16, max = 16, message = "Token must be of size 16")
    String resetPasswordToken;

    String newPassword;
}
