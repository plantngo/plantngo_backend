package me.plantngo.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class ResetPasswordDTO {

    @Email(message = "Must be a valid email")
    @NotBlank(message = "Email cannot be blank")
    private String toEmail;

    @Size(min=16, max=16)
    @NotBlank(message = "Token cannot be blank")
    private String resetPasswordToken;
}
