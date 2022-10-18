package me.plantngo.backend.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.plantngo.backend.DTO.ChangeCredentialsDTO;
import me.plantngo.backend.DTO.LoginDTO;
import me.plantngo.backend.DTO.VoucherPurchaseDTO;
import me.plantngo.backend.exceptions.UserNotFoundException;
import me.plantngo.backend.models.Customer;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.services.AuthService;
import me.plantngo.backend.services.ChangeCredentialService;
import me.plantngo.backend.services.CustomerService;
import me.plantngo.backend.services.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/v1/edit-profile")
@Api(value = "Change Credentials Controller", description = "Operations pertaining to Changing of Credentials for both Customers and Merchants")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ChangeCredentialsController {

    private CustomerService customerService;
    private MerchantService merchantService;
    private ChangeCredentialService changeCredentialService;
    private AuthService authService;

    @Autowired
    public ChangeCredentialsController(CustomerService customerService,
                                       MerchantService merchantService,
                                       ChangeCredentialService changeCredentialService,
                                       AuthService authService){
        this.customerService = customerService;
        this.merchantService = merchantService;
        this.changeCredentialService = changeCredentialService;
        this.authService = authService;
    }

    @ApiOperation(value = "Updates username of a Customer or a Merchant",
            notes = "New username must not already be used")
    @PutMapping("/username")
    public ResponseEntity<String> changeUsername(@Valid @RequestBody ChangeCredentialsDTO changeCredentialsDTO){
        verifyLogin(changeCredentialsDTO);

        String newUsername = changeCredentialsDTO.getNewUsername();
        if(newUsername == null || newUsername.isBlank()) return new ResponseEntity<>("newUsername cannot be blank", HttpStatus.BAD_REQUEST);

        String oldUsername = changeCredentialsDTO.getUsername();

        //check if username is already taken
        try{
            if(changeCredentialsDTO.getUserType() == 'C')
                customerService.getCustomerByUsername(newUsername);
            else
                merchantService.getMerchantByUsername(newUsername);

            return new ResponseEntity<>("username already taken", HttpStatus.CONFLICT);
        } catch (UserNotFoundException e){}

        //change username
        try {
            if(changeCredentialsDTO.getUserType() == 'C') {
                Customer customer = customerService.getCustomerByUsername(oldUsername);
                return changeCredentialService.replaceCustomerUsername(customer, newUsername);
            }
            else {
                Merchant merchant = merchantService.getMerchantByUsername(oldUsername);
                return changeCredentialService.replaceMerchantUsername(merchant, newUsername);
            }
        } catch(UserNotFoundException e){
            return new ResponseEntity<>("User does not exist.", HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Updates password of a Customer or a Merchant",
            notes = "New username must not already be used")
    @PutMapping("/password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangeCredentialsDTO changeCredentialsDTO){
        verifyLogin(changeCredentialsDTO);

        String newPassword = changeCredentialsDTO.getNewPassword();
        String username = changeCredentialsDTO.getUsername();
        try {
            if(changeCredentialsDTO.getUserType() == 'C') {
                Customer customer = customerService.getCustomerByUsername(username);
                return changeCredentialService.replaceCustomerPassword(customer, newPassword);
            }
            else {
                Merchant merchant = merchantService.getMerchantByUsername(username);
                return changeCredentialService.replaceMerchantPassword(merchant, newPassword);
            }
        } catch(UserNotFoundException e){
            return new ResponseEntity<>("User does not exist.", HttpStatus.BAD_REQUEST);
        }
    }

    public void verifyLogin(ChangeCredentialsDTO changeCredentialsDTO){
        LoginDTO loginDTO = new LoginDTO(changeCredentialsDTO.getUsername(), changeCredentialsDTO.getPassword());
        authService.authenticateUser(loginDTO);
    }
}
