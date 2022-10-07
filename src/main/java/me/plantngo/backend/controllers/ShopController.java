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

import me.plantngo.backend.DTO.CategoryDTO;
import me.plantngo.backend.DTO.ProductDTO;
import me.plantngo.backend.DTO.UpdateCategoryDTO;
import me.plantngo.backend.DTO.UpdateProductDTO;
import me.plantngo.backend.exceptions.AlreadyExistsException;
import me.plantngo.backend.exceptions.NotExistException;
import me.plantngo.backend.exceptions.UserNotFoundException;
import me.plantngo.backend.models.Category;
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
    public ResponseEntity<String> addVoucher(@PathVariable("merchantName") String merchantName,
                                              @Valid @RequestBody CategoryDTO categoryDTO) {
        
        try {
            Merchant merchant = merchantService.getMerchantByUsername(merchantName);
            //shopService.addCategory(merchant, categoryDTO);
            return new ResponseEntity<>("Category Added!", HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Merchant doesn't exist!", HttpStatus.BAD_REQUEST);
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<>("Category already exists for this merchant!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/{merchantName}")
    public ResponseEntity<String> addCategory(@PathVariable("merchantName") String merchantName,
                                              @Valid @RequestBody CategoryDTO categoryDTO) {

        try {
            Merchant merchant = merchantService.getMerchantByUsername(merchantName);
            shopService.addCategory(merchant, categoryDTO);
            return new ResponseEntity<>("Category Added!", HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>("Merchant doesn't exist!", HttpStatus.BAD_REQUEST);
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<>("Category already exists for this merchant!", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/{merchantName}/{categoryName}")
    public ResponseEntity<String> updateCategory(@PathVariable("merchantName") String merchantName,
                                                @PathVariable("categoryName") String categoryName,
                                                @Valid @RequestBody UpdateCategoryDTO updateCategoryDTO) {
        try {
            Merchant merchant = merchantService.getMerchantByUsername(merchantName);
            shopService.updateCategory(merchant, categoryName, updateCategoryDTO);
            return new ResponseEntity<>("Category updated!", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("Merchant doesn't exist!", HttpStatus.BAD_REQUEST);
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<>("Category with the new name already exists!", HttpStatus.BAD_REQUEST);
        } catch (NotExistException e) {
            return new ResponseEntity<>("Category doesn't exist!", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/{merchantName}/{categoryName}")
    public ResponseEntity<String> deleteCategory(@PathVariable("merchantName") String merchantName,
                                                @PathVariable("categoryName") String categoryName) {
    
        try {
            Merchant merchant = merchantService.getMerchantByUsername(merchantName);
            shopService.deleteCategory(merchant, categoryName);
            return new ResponseEntity<>("Category deleted!", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("Merchant doesn't exist!", HttpStatus.BAD_REQUEST);
        } catch (NotExistException e) {
            return new ResponseEntity<>("Category under merchant doesn't exist!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/{merchantName}/{categoryName}")
    public ResponseEntity<String> addProduct(@PathVariable("merchantName") String merchantName, 
                                             @PathVariable("categoryName") String categoryName, 
                                             @Valid @RequestBody ProductDTO productDTO) {
        
        try {
            Merchant merchant = merchantService.getMerchantByUsername(merchantName);
            shopService.addProduct(merchant, categoryName, productDTO);
            return new ResponseEntity<>("Product Added!", HttpStatus.CREATED);
        } catch (NotExistException e) {
            return new ResponseEntity<>("Category under merchant doesn't exist!", HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("Merchant doesn't exist!", HttpStatus.BAD_REQUEST);
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<>("Product already exists under this category for this merchant!", HttpStatus.BAD_REQUEST);
        }

    }
    
    @PutMapping(path = "/{merchantName}/{categoryName}/{productName}")
    public ResponseEntity<String> updateProduct(@PathVariable("merchantName") String merchantName,
                                                @PathVariable("categoryName") String categoryName,
                                                @PathVariable("productName") String productName,
                                                @Valid @RequestBody UpdateProductDTO updateProductDTO) {
        try {
            Merchant merchant = merchantService.getMerchantByUsername(merchantName);
            Category category = shopService.getCategory(merchant, categoryName);
            
            shopService.updateProduct(category, productName, updateProductDTO);
            return new ResponseEntity<>("Product updated!", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("Merchant doesn't exist!", HttpStatus.BAD_REQUEST);
        } catch (AlreadyExistsException e) {
            return new ResponseEntity<>("Product with the new name already exists!", HttpStatus.BAD_REQUEST);
        } catch (NotExistException e) {
            return new ResponseEntity<>("Category or Product doesn't exist!", HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping(path = "/{merchantName}/{categoryName}/{productName}")
    public ResponseEntity<String> deleteProduct(@PathVariable("merchantName") String merchantName,
                                                @PathVariable("categoryName") String categoryName,
                                                @PathVariable("productName") String productName) {
    
        try {
            Merchant merchant = merchantService.getMerchantByUsername(merchantName);
            Product product = shopService.getProduct(merchant, categoryName, productName);
            shopService.deleteProduct(product);
            return new ResponseEntity<>("Product deleted!", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("Merchant doesn't exist!", HttpStatus.BAD_REQUEST);
        } catch (NotExistException e) {
            return new ResponseEntity<>("Product under category doesn't exist!", HttpStatus.BAD_REQUEST);
        }
    }
}
