package me.plantngo.backend.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.plantngo.backend.DTO.ResetPasswordDTO;
import me.plantngo.backend.services.MailService;
import me.plantngo.backend.services.ResetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@RestController
@Api(value = "Mail Controller", description = "Operations pertaining to sending of mail using Microsoft's SMTP service")
@RequestMapping("api/v1/mailer")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MailController {

    private MailService mailService;
    private ResetPasswordService resetPasswordService;

    @Autowired
    public MailController(MailService mailService,
                          ResetPasswordService resetPasswordService) {
        this.mailService = mailService;
        this.resetPasswordService = resetPasswordService;
    }

    @ApiOperation("Sends resetPasswordToken to a user's email")
    @PostMapping("")
    public ResponseEntity<String> sendResetPasswordTokenByEmail(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        mailService.sendSimpleMessage(resetPasswordDTO.getToEmail(), "PlantNGo password reset", resetPasswordDTO.getResetPasswordToken());
        return new ResponseEntity<>("Reset password email sent", HttpStatus.OK);
    }


}
