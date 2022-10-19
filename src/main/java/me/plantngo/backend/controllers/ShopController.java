package me.plantngo.backend.controllers;

import java.util.List;

import javax.validation.Valid;

import me.plantngo.backend.DTO.*;
import me.plantngo.backend.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.plantngo.backend.exceptions.AlreadyExistsException;
import me.plantngo.backend.exceptions.NotExistException;
import me.plantngo.backend.exceptions.UserNotFoundException;
import me.plantngo.backend.services.MerchantService;
import me.plantngo.backend.services.ShopService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping(path = "api/v1/merchant")
@Api(value = "Shop Controller", description = "Operations pertaining to Merchant's Online Shop")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ShopController {

    private final ShopService shopService;

    private final MerchantService merchantService;

    @Autowired
    public ShopController(ShopService shopService, MerchantService merchantService) {
        this.shopService = shopService;
        this.merchantService = merchantService;
    }

    @ApiOperation(value = "Get a Voucher given its Id")
    @GetMapping(path="/{merchantName}/vouchers/{id}")
    public Voucher getVoucher(@PathVariable("merchantName") String merchantName, @PathVariable("id") Integer id) {
        Merchant merchant = merchantService.getMerchantByUsername(merchantName);
        return shopService.getVoucher(merchant, id);
    }

    @ApiOperation(value = "Get all Vouchers created by a Merchant")
    @GetMapping(path="/{merchantName}/vouchers")
    public List<Voucher> getAllVouchersFromMerchantName(@PathVariable("merchantName") String merchantName) {
        Merchant merchant = merchantService.getMerchantByUsername(merchantName);
        return shopService.getAllVouchersFromMerchant(merchant);
    }

    @ApiOperation(value = "Create a Voucher for a given Merchant")
    @PostMapping(path = "/{merchantName}/vouchers")
    public ResponseEntity<String> addVoucher(@PathVariable("merchantName") String merchantName,
            @Valid @RequestBody VoucherDTO voucherDTO) {

        try {
            Merchant merchant = merchantService.getMerchantByUsername(merchantName);
            shopService.addVoucher(merchant, voucherDTO);
            return new ResponseEntity<>("Voucher Added!", HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Merchant doesn't exist!", HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Update an existing Voucher by a Merchant")
    @PutMapping(path = "/{merchantName}/vouchers/{voucherId}")
    public ResponseEntity<String> updateVoucher(@PathVariable("merchantName") String merchantName,
            @PathVariable("voucherId") Integer voucherId,
            @Valid @RequestBody UpdateVoucherDTO updateVoucherDTO) {
        try {
            Merchant merchant = merchantService.getMerchantByUsername(merchantName);
            shopService.updateVoucher(merchant, voucherId, updateVoucherDTO);
            return new ResponseEntity<>("Voucher updated!", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("Merchant doesn't exist!", HttpStatus.BAD_REQUEST);
        } catch (NotExistException e) {
            return new ResponseEntity<>("Voucher doesn't exist!", HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Delete a Voucher by a Merchant given its Id")
    @DeleteMapping(path = "/{merchantName}/vouchers/{voucherId}")
    public ResponseEntity<String> deleteVoucher(@PathVariable("merchantName") String merchantName,
            @PathVariable("voucherId") Integer voucherId) {

        try {
            Merchant merchant = merchantService.getMerchantByUsername(merchantName);
            shopService.deleteVoucher(merchant, voucherId);
            return new ResponseEntity<>("Voucher deleted!", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("Merchant doesn't exist!", HttpStatus.BAD_REQUEST);
        } catch (NotExistException e) {
            return new ResponseEntity<>("Voucher under merchant doesn't exist!", HttpStatus.BAD_REQUEST);
        }
    }

    // @ApiOperation(value = "Add a food category for a Merchant")
    // @PostMapping(path = "/{merchantName}")
    // public ResponseEntity<String> addCategory(@PathVariable("merchantName") String merchantName,
    //         @Valid @RequestBody CategoryDTO categoryDTO) {

    //     try {
    //         Merchant merchant = merchantService.getMerchantByUsername(merchantName);
    //         shopService.addCategory(merchant, categoryDTO);
    //         return new ResponseEntity<>("Category Added!", HttpStatus.CREATED);
    //     } catch (UserNotFoundException e) {
    //         System.out.println(e.getMessage());
    //         return new ResponseEntity<>("Merchant doesn't exist!", HttpStatus.BAD_REQUEST);
    //     } catch (AlreadyExistsException e) {
    //         return new ResponseEntity<>("Category already exists for this merchant!", HttpStatus.BAD_REQUEST);
    //     }
    // }

    // @ApiOperation(value = "Update an existing food category by a Merchant")
    // @PutMapping(path = "/{merchantName}/{categoryName}")
    // public ResponseEntity<String> updateCategory(@PathVariable("merchantName") String merchantName,
    //         @PathVariable("categoryName") String categoryName,
    //         @Valid @RequestBody UpdateCategoryDTO updateCategoryDTO) {
    //     try {
    //         Merchant merchant = merchantService.getMerchantByUsername(merchantName);
    //         shopService.updateCategory(merchant, categoryName, updateCategoryDTO);
    //         return new ResponseEntity<>("Category updated!", HttpStatus.OK);
    //     } catch (UserNotFoundException e) {
    //         return new ResponseEntity<>("Merchant doesn't exist!", HttpStatus.BAD_REQUEST);
    //     } catch (AlreadyExistsException e) {
    //         return new ResponseEntity<>("Category with the new name already exists!", HttpStatus.BAD_REQUEST);
    //     } catch (NotExistException e) {
    //         return new ResponseEntity<>("Category doesn't exist!", HttpStatus.BAD_REQUEST);
    //     }
    // }

    // @ApiOperation(value = "Delete a food category by a Merchant")
    // @DeleteMapping(path = "/{merchantName}/{categoryName}")
    // public ResponseEntity<String> deleteCategory(@PathVariable("merchantName") String merchantName,
    //         @PathVariable("categoryName") String categoryName) {

    //     try {
    //         Merchant merchant = merchantService.getMerchantByUsername(merchantName);
    //         shopService.deleteCategory(merchant, categoryName);
    //         return new ResponseEntity<>("Category deleted!", HttpStatus.OK);
    //     } catch (UserNotFoundException e) {
    //         return new ResponseEntity<>("Merchant doesn't exist!", HttpStatus.BAD_REQUEST);
    //     } catch (NotExistException e) {
    //         return new ResponseEntity<>("Category under merchant doesn't exist!", HttpStatus.BAD_REQUEST);
    //     }
    // }

    @ApiOperation(value = "Add a Product for a Merchant")
    @PostMapping(path = "/{merchantName}")
    public ResponseEntity<Product> addProduct(@PathVariable("merchantName") String merchantName,
            @Valid @RequestBody ProductDTO productDTO) {

            Merchant merchant = merchantService.getMerchantByUsername(merchantName);
            Product product = shopService.addProduct(merchant, productDTO);
            return new ResponseEntity<>(product, HttpStatus.CREATED);
    }
    
    @ApiOperation(value = "Update an existing Product by a Merchant")
    @PutMapping(path = "/{merchantName}/{productName}")
    public ResponseEntity<Product> updateProduct(@PathVariable("merchantName") String merchantName,
            @PathVariable("productName") String productName,
            @Valid @RequestBody UpdateProductDTO updateProductDTO) {

            Merchant merchant = merchantService.getMerchantByUsername(merchantName);
            Product product = shopService.updateProduct(merchant, productName, updateProductDTO);
            return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete an existing Product by a Merchant")
    @DeleteMapping(path = "/{merchantName}/{categoryName}/{productName}")
    public ResponseEntity<String> deleteProduct(@PathVariable("merchantName") String merchantName,
            @PathVariable("categoryName") String categoryName,
            @PathVariable("productName") String productName) {

            Merchant merchant = merchantService.getMerchantByUsername(merchantName);
            shopService.deleteProduct(merchant, categoryName, productName);
            return new ResponseEntity<>("Product deleted!", HttpStatus.OK);
    }
}
