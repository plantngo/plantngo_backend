package me.plantngo.backend.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.plantngo.backend.DTO.ResetPasswordMailerDTO;
import me.plantngo.backend.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "Mail Controller", description = "Operations pertaining to sending of mail using Microsoft's SMTP service")
@RequestMapping("api/v1/mailer")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MailController {

    private MailService mailService;

    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }
    @ApiOperation("Sends reset password token to a user's email")
    @PostMapping("")
    public ResponseEntity<String> sendResetPasswordTokenByEmail(@RequestBody ResetPasswordMailerDTO resetPasswordMailerDTO) {
        mailService.sendSimpleMessage(resetPasswordMailerDTO.getToEmail(), "PlantNGo password reset", resetPasswordMailerDTO.getResetPasswordToken());
        return new ResponseEntity<>("Reset password email sent", HttpStatus.OK);
    }

}
