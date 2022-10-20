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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.plantngo.backend.DTO.ProductIngredientDTO;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.models.Product;
import me.plantngo.backend.models.ProductIngredient;
import me.plantngo.backend.services.MerchantService;
import me.plantngo.backend.services.ProductService;

@RestController()
@RequestMapping(path = "api/v1/product")
@Api(value = "Product Controller", description = "Allows Merchants to add Ingredients to their Product and automatically generate a carbon emission value")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductController {
    
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ApiOperation(value = "Get all registered Products")
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @ApiOperation(value = "Get registered Product given its Id")
    @GetMapping(path = "/{productId}")
    public Product getProduct(@PathVariable("productId") Integer productId) {
        return productService.getProductById(productId);
    }

    @ApiOperation(value = "Get all Product Ingredients By Merchant And Product")
    @GetMapping(path = "/{merchantName}/{productName}")
    public List<ProductIngredient> getProductIngredientsByMerchantAndProduct(@PathVariable("merchantName") String merchantName,
                                                                            @PathVariable("productName") String productName) {
        
        return productService.getProductIngredientsByMerchantAndProduct(merchantName, productName);
    }

    @ApiOperation(value = "Get all registed Products by Merchant given their name, in ascending order of carbon emissions")
    @GetMapping(path = "/merchant/{merchantName}")
    public List<Product> getAllProductsByMerchant(@PathVariable("merchantName") String merchantName) {
        return productService.getAllProductsByMerchant(merchantName);
    }

    @ApiOperation(value = "Add Ingredient to Product")
    @PostMapping(path = "/{merchantName}/{productName}")
    public ResponseEntity<ProductIngredient> addProductIngredient(@PathVariable("merchantName") String merchantName,
                                                                @PathVariable("productName") String productName, 
                                                                @Valid @RequestBody ProductIngredientDTO productIngredientDTO) {

        ProductIngredient productIngredient = productService.addProductIngredient(merchantName, productName, productIngredientDTO);
        return new ResponseEntity<>(productIngredient, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update Ingredient in Product")
    @PutMapping(path = "/{merchantName}/{productName}")
    public ResponseEntity<ProductIngredient> updateProductIngredient(@PathVariable("merchantName") String merchantName,
                                                                @PathVariable("productName") String productName, 
                                                                @Valid @RequestBody ProductIngredientDTO productIngredientDTO) {
        
        ProductIngredient productIngredient = productService.updateProductIngredient(merchantName, productName, productIngredientDTO);
        return new ResponseEntity<>(productIngredient, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete Ingredient in Product")
    @DeleteMapping(path = "/{merchantName}/{productName}/{productIngredientName}")
    public ResponseEntity<String> deleteProductIngredient(@PathVariable("merchantName") String merchantName,
                                                        @PathVariable("productName") String productName, 
                                                        @PathVariable("productIngredientName") String productIngredientName) {
        
        productService.deleteProductIngredient(merchantName, productName, productIngredientName);
        return new ResponseEntity<>("Product Ingredient deleted!", HttpStatus.OK);
    }

    @ApiOperation(value = "Delete All Ingredients in specific Product")
    @DeleteMapping(path = "/{merchantName}/{productName}")
    public ResponseEntity<String> deleteAllProductIngredients(@PathVariable("merchantName") String merchantName,
                                                        @PathVariable("productName") String productName) {
        
        productService.deleteAllProductIngredients(merchantName, productName);
        return new ResponseEntity<>("All Product Ingredient deleted!", HttpStatus.OK);
    }

}
