package me.plantngo.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import me.plantngo.backend.models.Account;
import me.plantngo.backend.models.RegistrationDTO;
import me.plantngo.backend.repositories.AccountRepository;

@Service
public class AccountService {
    
    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccountByUsername(String username) {
        if (accountRepository.findByUsername(username).isEmpty()) {
            System.out.println("User doesn't exist");
            return null;
        }
        return accountRepository.findByUsername(username).get();
    }
    
    public Account getAccountByEmail(String email) {
        if (accountRepository.findByEmail(email).isEmpty()) {
            System.out.println("Email doesn't exist");
            return null;
        }
        return accountRepository.findByEmail(email).get();
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public ResponseEntity<String> registerAccount(RegistrationDTO registrationDTO) {

        // Check if email is already in use
        if (getAccountByEmail(registrationDTO.getEmail()) != null) {
            return new ResponseEntity<>("Email already taken!", HttpStatus.BAD_REQUEST);
        }

        // Check if username is already in use
        if (getAccountByUsername(registrationDTO.getUsername()) != null) {
            return new ResponseEntity<>("Username already taken!", HttpStatus.BAD_REQUEST);
        }
        
        Account account = new Account();
        account.setEmail(registrationDTO.getEmail());
        account.setUsername(registrationDTO.getUsername());
        account.setPassword(registrationDTO.getPassword());
        account.setGreenPts(0);

        accountRepository.save(account);

        return new ResponseEntity<>("User registered!", HttpStatus.OK);
    }

}
