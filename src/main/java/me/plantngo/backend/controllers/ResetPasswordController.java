package me.plantngo.backend.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.plantngo.backend.DTO.LoginDTO;
import me.plantngo.backend.DTO.ResetPasswordDTO;
import me.plantngo.backend.services.ResetPasswordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "Mail Controller", description = "Operations pertaining to sending of mail using Microsoft's SMTP service")
@RequestMapping("api/v1/forgot-password")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ResetPasswordController {
    ResetPasswordService resetPasswordService;

    public ResetPasswordController(ResetPasswordService resetPasswordService){
        this.resetPasswordService = resetPasswordService;
    }

    @ApiOperation("Gets the reset password token for a user")
    @PostMapping("/token")
    public ResponseEntity<String> setResetPasswordTokenAndSendEmail(@RequestBody ResetPasswordDTO resetPasswordDTO){
        return resetPasswordService.setResetPasswordTokenAndSendEmail(resetPasswordDTO.getEmail());
    }

    @ApiOperation("checks the reset password token for a user, deleting if a match is found")
    @PostMapping("/")
    public ResponseEntity<String> checkAndDeleteTokenAndChangePasswordIfCorrectResetPasswordToken(@RequestBody ResetPasswordDTO resetPasswordDTO){

        /*
        will throw exceptions if the check fails
        if check passes, it will clear the resetPasswordToken field and then reset the password
         */
        return resetPasswordService.checkAndDeleteTokenAndChangePasswordIfCorrectResetPasswordToken(resetPasswordDTO.getEmail(),
                resetPasswordDTO.getResetPasswordToken(), resetPasswordDTO.getNewPassword());
    }
}
