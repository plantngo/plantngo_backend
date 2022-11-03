package me.plantngo.backend.controllers;

import javax.validation.Valid;

import me.plantngo.backend.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import me.plantngo.backend.DTO.LoginDTO;
import me.plantngo.backend.DTO.RegistrationDTO;
import me.plantngo.backend.models.Customer;
import me.plantngo.backend.services.AuthService;

@RestController
@RequestMapping(path = "api/v1")
@Api(value = "Authentication Controller", description = "Operations pertaining to Customer/Merchant Registration & Login")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    private final AuthService authService;

    private final LogService logService;

    @Autowired
    public AuthController(AuthService authService, LogService logService) {
        this.authService = authService;
        this.logService = logService;
    }

    @ApiOperation(value = "Register a Customer or Merchant account")
    @ApiResponses(value = {
                    @ApiResponse(code = 201, message = "Account Successfully Created"),
                    @ApiResponse(code = 400, message = "Input fields invalid or username already taken")
    })
    @PostMapping(path = "/register")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody RegistrationDTO registrationDTO) {

        Object responseObject = authService.registerUser(registrationDTO);
        
        return new ResponseEntity<>(responseObject, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Login with a Customer or Merchant account")
    @ApiResponses(value = {
                    @ApiResponse(code = 200, message = "Account Logged in Successfully"),
                    @ApiResponse(code = 400, message = "Incorrect Username or Password")
    })
    @PostMapping(path = "/login")
    public ResponseEntity<String> authenticateUser(@Valid @RequestBody LoginDTO loginDTO) {
        logService.addLog(loginDTO.getUsername(), "login");
        return authService.authenticateUser(loginDTO);
    }

}
