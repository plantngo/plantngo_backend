package me.plantngo.backend.controllers;

import java.net.MalformedURLException;
import java.util.List;

import javax.validation.Valid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import me.plantngo.backend.DTO.*;
import me.plantngo.backend.models.*;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    @GetMapping(path = "/{merchantName}/vouchers/{id}")
    public Voucher getVoucher(@PathVariable("merchantName") String merchantName, @PathVariable("id") Integer id) {
        Merchant merchant = merchantService.getMerchantByUsername(merchantName);
        return shopService.getVoucher(merchant, id);
    }

    @ApiOperation(value = "Get all Vouchers created by a Merchant")
    @GetMapping(path = "/{merchantName}/vouchers")
    public List<Voucher> getAllVouchersFromMerchantName(@PathVariable("merchantName") String merchantName) {
        Merchant merchant = merchantService.getMerchantByUsername(merchantName);
        return shopService.getAllVouchersFromMerchant(merchant);
    }

    @ApiOperation(value = "Create a Voucher for a given Merchant")
    @PostMapping(path = "/{merchantName}/vouchers")
    public ResponseEntity<String> addVoucher(@PathVariable("merchantName") String merchantName,
            @Valid @RequestBody VoucherDTO voucherDTO) {

        Merchant merchant = merchantService.getMerchantByUsername(merchantName);
        shopService.addVoucher(merchant, voucherDTO);
        return new ResponseEntity<>("Voucher Added!", HttpStatus.CREATED);

    }

    @ApiOperation(value = "Update an existing Voucher by a Merchant")
    @PutMapping(path = "/{merchantName}/vouchers/{voucherId}")
    public ResponseEntity<String> updateVoucher(@PathVariable("merchantName") String merchantName,
            @PathVariable("voucherId") Integer voucherId,
            @Valid @RequestBody UpdateVoucherDTO updateVoucherDTO) {

        Merchant merchant = merchantService.getMerchantByUsername(merchantName);
        shopService.updateVoucher(merchant, voucherId, updateVoucherDTO);
        return new ResponseEntity<>("Voucher updated!", HttpStatus.OK);

    }

    @ApiOperation(value = "Delete a Voucher by a Merchant given its Id")
    @DeleteMapping(path = "/{merchantName}/vouchers/{voucherId}")
    public ResponseEntity<String> deleteVoucher(@PathVariable("merchantName") String merchantName,
            @PathVariable("voucherId") Integer voucherId) {

        Merchant merchant = merchantService.getMerchantByUsername(merchantName);
        shopService.deleteVoucher(merchant, voucherId);
        return new ResponseEntity<>("Voucher deleted!", HttpStatus.OK);

    }

    @ApiOperation(value = "Add a food category for a Merchant")
    @PostMapping(path = "/{merchantName}")
    public ResponseEntity<String> addCategory(@PathVariable("merchantName") String merchantName,
            @Valid @RequestBody CategoryDTO categoryDTO) {

        Merchant merchant = merchantService.getMerchantByUsername(merchantName);
        shopService.addCategory(merchant, categoryDTO);
        return new ResponseEntity<>("Category Added!", HttpStatus.CREATED);

    }

    @ApiOperation(value = "Update an existing food category by a Merchant")
    @PutMapping(path = "/{merchantName}/{categoryName}")
    public ResponseEntity<String> updateCategory(@PathVariable("merchantName") String merchantName,
            @PathVariable("categoryName") String categoryName,
            @Valid @RequestBody UpdateCategoryDTO updateCategoryDTO) {

        Merchant merchant = merchantService.getMerchantByUsername(merchantName);
        shopService.updateCategory(merchant, categoryName, updateCategoryDTO);
        return new ResponseEntity<>("Category updated!", HttpStatus.OK);

    }

    @ApiOperation(value = "Delete a food category by a Merchant")
    @DeleteMapping(path = "/{merchantName}/{categoryName}")
    public ResponseEntity<String> deleteCategory(@PathVariable("merchantName") String merchantName,
            @PathVariable("categoryName") String categoryName) {

        Merchant merchant = merchantService.getMerchantByUsername(merchantName);
        shopService.deleteCategory(merchant, categoryName);
        return new ResponseEntity<>("Category deleted!", HttpStatus.OK);

    }

    @ApiOperation(value = "Add a product for a Merchant")
    @PostMapping(path = "/{merchantName}/{categoryName}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addProduct(@PathVariable("merchantName") String merchantName,
            @PathVariable("categoryName") String categoryName,
            @Valid @RequestPart("product") ProductDTO productDTO, @RequestPart("image") MultipartFile file)
            throws MalformedURLException {

        Merchant merchant = merchantService.getMerchantByUsername(merchantName);
        shopService.addProduct(merchant, categoryName, productDTO, file);

        return new ResponseEntity<>("Product Added!", HttpStatus.CREATED);

    }

    @ApiOperation(value = "Update an existing Product by a Merchant")
    @PutMapping(path = "/{merchantName}/{categoryName}/{productName}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateProduct(@PathVariable("merchantName") String merchantName,
            @PathVariable("categoryName") String categoryName,
            @PathVariable("productName") String productName,
            @Valid @RequestPart("product") UpdateProductDTO updateProductDTO,
            @RequestPart("image") MultipartFile file) throws MalformedURLException {

        Merchant merchant = merchantService.getMerchantByUsername(merchantName);
        Category category = shopService.getCategory(merchant, categoryName);

        shopService.updateProduct(category, productName, updateProductDTO, file);
        return new ResponseEntity<>("Product updated!", HttpStatus.OK);

    }

    @ApiOperation(value = "Delete an existing Product by a Merchant")
    @DeleteMapping(path = "/{merchantName}/{categoryName}/{productName}")
    public ResponseEntity<String> deleteProduct(@PathVariable("merchantName") String merchantName,
            @PathVariable("categoryName") String categoryName,
            @PathVariable("productName") String productName) {

        Merchant merchant = merchantService.getMerchantByUsername(merchantName);
        Product product = shopService.getProduct(merchant, categoryName, productName);
        shopService.deleteProduct(product);
        return new ResponseEntity<>("Product deleted!", HttpStatus.OK);

    }
}
