package me.plantngo.backend.controllers;

import me.plantngo.backend.services.CustomerService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/store")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VoucherOwnershipController {

    //VoucherOwnershipService voucherOwnershipService;
    CustomerService customerService;

}
