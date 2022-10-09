package me.plantngo.backend.controllers;

import me.plantngo.backend.services.CustomerService;
import me.plantngo.backend.services.MerchantService;
import me.plantngo.backend.services.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/store")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VoucherPurchaseController {

//    private final VoucherPurchaseService voucherPurchaseService;
//    private final CustomerService customerService;
//
//    @Autowired
//    public VoucherPurchaseController(VoucherPurchaseService voucherPurchaseService, CustomerService customerService) {
//        this.voucherPurchaseService = voucherPurchaseService;
//        this.customerService = customerService;
//    }

}
