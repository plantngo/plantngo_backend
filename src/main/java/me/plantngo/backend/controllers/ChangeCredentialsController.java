package me.plantngo.backend.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import me.plantngo.backend.DTO.ChangeCredentialsDTO;
import me.plantngo.backend.DTO.LoginDTO;
import me.plantngo.backend.services.AuthService;
import me.plantngo.backend.services.ChangeCredentialService;
import me.plantngo.backend.services.CustomerService;
import me.plantngo.backend.services.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/v1/edit-profile")
@Api(value = "Change Credentials Controller", description = "Operations pertaining to Changing of Credentials for both Customers and Merchants")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ChangeCredentialsController {

    private ChangeCredentialService changeCredentialService;
    private AuthService authService;

    @Autowired
    public ChangeCredentialsController(CustomerService customerService,
            MerchantService merchantService,
            ChangeCredentialService changeCredentialService,
            AuthService authService) {
        this.changeCredentialService = changeCredentialService;
        this.authService = authService;
    }

    @ApiOperation(value = "Updates username of a Customer or a Merchant", notes = "New username must not already be used")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Username successfully changed to {username}"),
            @ApiResponse(code = 400, message = "Input fields invalid or username already taken") })
    @PutMapping("/username")
    public ResponseEntity<String> changeUsername(@Valid @RequestBody ChangeCredentialsDTO changeCredentialsDTO) {
        verifyLogin(changeCredentialsDTO);

        String newUsername = changeCredentialsDTO.getNewUsername();
        if (newUsername == null || newUsername.isBlank())
            throw new IllegalArgumentException("New username should not be blank.");

        changeCredentialService.validateNewUsername(newUsername, changeCredentialsDTO.getUserType());

        return changeCredentialService.replaceUsername(changeCredentialsDTO.getUsername(), newUsername,
                changeCredentialsDTO.getUserType());
    }

    @ApiOperation(value = "Updates password of a Customer or a Merchant", notes = "New username must not already be used")
    @PutMapping("/password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangeCredentialsDTO changeCredentialsDTO) {
        verifyLogin(changeCredentialsDTO);

        return changeCredentialService.replacePassword(changeCredentialsDTO.getUsername(),
                changeCredentialsDTO.getNewPassword(), changeCredentialsDTO.getUserType());
    }

    public void verifyLogin(ChangeCredentialsDTO changeCredentialsDTO) {
        LoginDTO loginDTO = new LoginDTO(changeCredentialsDTO.getUsername(), changeCredentialsDTO.getPassword());
        authService.authenticateUser(loginDTO);
    }
}
