package me.plantngo.backend.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.plantngo.backend.DTO.LoginDTO;
import me.plantngo.backend.DTO.ResetPasswordDTO;
import me.plantngo.backend.services.ResetPasswordService;
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
    @GetMapping("/")
    public String getAndSetResetPassWordToken(@RequestBody ResetPasswordDTO resetPasswordDTO){
        return resetPasswordService.getAndSetResetPassWordToken(resetPasswordDTO.getUsername(), resetPasswordDTO.getEmail(), resetPasswordDTO.getUserType());
    }

    @ApiOperation("checks the reset password token for a user, deleting if a match is found")
    @PostMapping("/")
    public ResponseEntity<String> checkAndDeleteIfCorrectResetPasswordToken(@RequestBody ResetPasswordDTO resetPasswordDTO){
        return resetPasswordService.checkAndDeleteIfCorrectResetPasswordToken(resetPasswordDTO.getUsername(), resetPasswordDTO.getUserType(), resetPasswordDTO.getResetPasswordToken());
    }
}
