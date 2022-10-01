package me.plantngo.backend.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.plantngo.backend.DTO.ProductDTO;
import me.plantngo.backend.DTO.UpdateProductDTO;
import me.plantngo.backend.exceptions.AlreadyExistsException;
import me.plantngo.backend.exceptions.NotExistException;
import me.plantngo.backend.exceptions.UserNotFoundException;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.models.Product;
import me.plantngo.backend.services.MerchantService;
import me.plantngo.backend.services.ShopService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping(path = "api/v1/merchant")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ShopController {
    
    private final ShopService shopService;

    private final MerchantService merchantService;

    @Autowired
    public ShopController(ShopService shopService, MerchantService merchantService) {
        this.shopService = shopService;
        this.merchantService = merchantService;
    }

    @PostMapping(path = "/{merchantName}")
    public ResponseEntity<String> addProduct(@PathVariable("merchantName") String merchantName, @Valid @RequestBody ProductDTO productDTO) {
        
        try {
            Merchant merchant = merchantService.getMerchantByUsername(merchantName);
            shopService.addProduct(merchant, productDTO);
            return new ResponseEntity<>("Product Added!", HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Merchant doesn't exist!", HttpStatus.BAD_REQUEST);
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<>("Product already exists for this merchant!", HttpStatus.BAD_REQUEST);
        }

    }
    
    @PutMapping(path = "/{merchantName}/{productName}")
    public ResponseEntity<String> updateProduct(@PathVariable("merchantName") String merchantName,
                                                @PathVariable("productName") String productName,
                                                @Valid @RequestBody UpdateProductDTO updateProductDTO) {
        try {
            Merchant merchant = merchantService.getMerchantByUsername(merchantName);
            shopService.updateProduct(merchant, productName, updateProductDTO);
            return new ResponseEntity<>("Product updated!", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("Merchant doesn't exist!", HttpStatus.BAD_REQUEST);
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<>("Product with the new name already exists!", HttpStatus.BAD_REQUEST);
        } catch (NotExistException e) {
            return new ResponseEntity<>("Product doesn't exist!", HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping(path = "/{merchantName}/{productName}")
    public ResponseEntity<String> deleteProduct(@PathVariable("merchantName") String merchantName,
                                                @PathVariable("productName") String productName) {
    
        try {
            Merchant merchant = merchantService.getMerchantByUsername(merchantName);
            shopService.deleteProduct(merchant, productName);
            return new ResponseEntity<>("Product deleted!", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("Merchant doesn't exist!", HttpStatus.BAD_REQUEST);
        } catch (NotExistException e) {
            return new ResponseEntity<>("Product under merchant doesn't exist!", HttpStatus.BAD_REQUEST);
        }
    }
}
