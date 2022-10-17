package me.plantngo.backend.controllers;

import java.util.List;

import org.aspectj.apache.bcel.classfile.Module.Require;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.services.MerchantService;

@RestController()
@RequestMapping(path = "api/v1/merchant")
@Api(value = "Merchant Controller", description = "Operations pertaining to Merchant model")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MerchantController {
    
    private final MerchantService merchantService;

    @Autowired
    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }
    

    @ApiOperation(value = "Get a Merchant given their Username")
    @GetMapping(path="/{username}")
    //@PreAuthorize("authentication.principal.username == #username || hasRole('ADMIN')")
    public Merchant getMerchantByUsername(@PathVariable("username") String username) {
        return merchantService.getMerchantByUsername(username);
    }

    @ApiOperation(value = "Get all registered Merchants")
    @GetMapping
    public List<Merchant> getAllUsers() {
        return merchantService.findAll();
    }

    @ApiOperation(value = "Get a list of all Merchants in proximity to App User")
    @GetMapping(path = "/search")
    public List<Merchant> getAllMerchantsInRange() {
        double latitude = 0;
        double longitude = 0;
        return merchantService.findMerchantsInRange(latitude, longitude);
    }

}

