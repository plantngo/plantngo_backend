package me.plantngo.backend.controllers;

import java.util.ArrayList;
import java.util.List;

import me.plantngo.backend.models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.plantngo.backend.models.Account;
import me.plantngo.backend.models.RegistrationDTO;
import me.plantngo.backend.services.AccountService;

@RestController()
@RequestMapping(path = "api/v1/account")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    @GetMapping(path="{username}")
    public Account getUserByUsername(@PathVariable("username") String username) {
        return accountService.getAccountByUsername(username);
    }

    @GetMapping
    public List<Account> getAllUsers() {
        return accountService.findAll();
    }

    @GetMapping(path="test")
    public List<Account> getAllAccounts() {
        List<Account> accountList = new ArrayList<>();

        accountList.add(new Account(1, "Jack", "ojh1@gmail.com",
                        "password1", false, null, null, null ));
        accountList.add(new Account(1, "Jane", "ojane1@gmail.com",
                "password1", false, null, null, null ));

        return accountList;
    }

    @PostMapping(path="register")
    public ResponseEntity<String> registerAccount(@RequestBody RegistrationDTO registrationDTO) {
        return accountService.registerAccount(registrationDTO);
    }
}

