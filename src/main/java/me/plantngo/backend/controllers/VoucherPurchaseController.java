package me.plantngo.backend.controllers;

import me.plantngo.backend.DTO.VoucherPurchaseDTO;
import me.plantngo.backend.models.Customer;
import me.plantngo.backend.models.Voucher;
import me.plantngo.backend.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/store")
@Api(value = "Voucher Controller", description = "Operations pertaining to Voucher creation by Merchants and purchase by Customers")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class VoucherPurchaseController {

    private final VoucherPurchaseService voucherPurchaseService;
    private final CustomerService customerService;
    private final ShopService shopService;
    private final MerchantService merchantService;

    private final LogService logService;

    @Autowired
    public VoucherPurchaseController(VoucherPurchaseService voucherPurchaseService,
                                     CustomerService customerService, ShopService shopService,
                                     MerchantService merchantService, LogService logService) {
        this.voucherPurchaseService = voucherPurchaseService;
        this.customerService = customerService;
        this.shopService = shopService;
        this.merchantService = merchantService;
        this.logService = logService;
    }

    @ApiOperation(value = "Get all existing Vouchers")
    @GetMapping(path="")
    public List<Voucher> getAllVouchers() {
        return voucherPurchaseService.getAllVouchers();
    }

    @ApiOperation(value = "Get all Vouchers owned by a Customer")
    @GetMapping(path="/{username}/my-vouchers")
    public List<Voucher> getAllOwnedVouchers(@PathVariable("username") String customerUsername) {
        return voucherPurchaseService.getAllOwnedVouchers(customerUsername);
    }

    @ApiOperation(value = "Get all Vouchers in a Customer's cart")
    @GetMapping(path="/{username}/my-cart")
    public List<Voucher> getAllInCartVouchers(@PathVariable("username") String customerUsername) {
        return voucherPurchaseService.getAllInCartVouchers(customerUsername);
    }

    @ApiOperation(value = "Add a Voucher to Customer's cart")
    @PostMapping(path="/{username}/my-cart")
    public ResponseEntity<String> addToCart(@PathVariable("username") String customerUsername,
                                            @Valid @RequestBody VoucherPurchaseDTO voucherPurchaseDTO) {

        Customer customer = customerService.getCustomerByUsername(customerUsername);
        Voucher voucher = shopService.getVoucher(merchantService.getMerchantById(voucherPurchaseDTO.getMerchantId()), voucherPurchaseDTO.getVoucherId());
        voucherPurchaseService.addToCart(customer, voucher);
        return new ResponseEntity<>("Added to Cart.", HttpStatus.OK);

    }

    @ApiOperation(value = "Delete a Voucher from Customer's cart")
    @DeleteMapping(path="/{username}/my-cart")
    public ResponseEntity<String> deleteFromCart(@PathVariable("username") String customerUsername,
                                                 @Valid @RequestBody VoucherPurchaseDTO voucherPurchaseDTO) {

        Customer customer = customerService.getCustomerByUsername(customerUsername);
        Voucher voucher = shopService.getVoucher(merchantService.getMerchantById(voucherPurchaseDTO.getMerchantId()), voucherPurchaseDTO.getVoucherId());
        voucherPurchaseService.deleteFromCart(customer, voucher);
        return new ResponseEntity<>("Deleted from Cart.", HttpStatus.OK);

    }

    @ApiOperation(value = "Add Voucher to Customer's model")
    @PostMapping(path="/{username}/my-vouchers")
    public ResponseEntity<String> addOwnedVoucher(@PathVariable("username") String customerUsername,
                                            @Valid @RequestBody VoucherPurchaseDTO voucherPurchaseDTO) {

        Customer customer = customerService.getCustomerByUsername(customerUsername);
        Voucher voucher = shopService.getVoucher(merchantService.getMerchantById(voucherPurchaseDTO.getMerchantId()), voucherPurchaseDTO.getVoucherId());
        voucherPurchaseService.addOwnedVoucher(customer, voucher);
        logService.addLog(customerUsername, "purchase-voucher");
        return new ResponseEntity<>("Successfully added.", HttpStatus.OK);

    }

    @ApiOperation(value = "Handles Customer's Voucher purchase")
    @PostMapping(path="/{username}/purchase-voucher")
    public ResponseEntity<String> purchaseVouchers(@PathVariable("username") String customerUsername){

        Customer customer = customerService.getCustomerByUsername(customerUsername);
        voucherPurchaseService.purchaseVouchers(customer);
        return new ResponseEntity<>("Purchase successful!", HttpStatus.OK);

    }

    @ApiOperation(value = "Delete Voucher from Customer's inventory")
    @DeleteMapping(path="/{username}/my-vouchers")
    public ResponseEntity<String> deleteOwnedVoucher(@PathVariable("username") String customerUsername,
                                                 @Valid @RequestBody VoucherPurchaseDTO voucherPurchaseDTO) {

        Customer customer = customerService.getCustomerByUsername(customerUsername);
        Voucher voucher = shopService.getVoucher(merchantService.getMerchantById(voucherPurchaseDTO.getMerchantId()), voucherPurchaseDTO.getVoucherId());
        voucherPurchaseService.deleteOwnedVoucher(customer, voucher);
        return new ResponseEntity<>("Successfully removed.", HttpStatus.OK);

    }

}
