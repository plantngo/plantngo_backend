package me.plantngo.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.services.MerchantService;

@RestController()
@RequestMapping(path = "api/v1/merchant")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MerchantController {
    
    private final MerchantService merchantService;

    @Autowired
    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }
    

    @GetMapping(path="/{username}")
    @PreAuthorize("authentication.principal.username == #username || hasRole('ADMIN')")
    public Merchant getMerchantByUsername(@PathVariable("username") String username) {
        return merchantService.getMerchantByUsername(username);
    }

    @GetMapping
    public List<Merchant> getAllUsers() {
        return merchantService.findAll();
    }

  

}

