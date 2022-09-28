package me.plantngo.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import me.plantngo.backend.DTO.LoginDTO;
import me.plantngo.backend.DTO.RegistrationDTO;
import me.plantngo.backend.services.AuthService;

@RestController
@RequestMapping(path = "api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    private final AuthService authService;

    private final AuthenticationManager authenticationManager;
    @Autowired
    public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationDTO registrationDTO) {

        if (registrationDTO.getUserType() == 'C') {
            return authService.registerCustomer(registrationDTO);
        } else if (registrationDTO.getUserType() == 'M') {
            return authService.registerMerchant(registrationDTO);
        }
        return new ResponseEntity<>("Invalid User Type!", HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginDTO loginDTO) {
        return authService.authenticateUser(loginDTO);
    }

}
