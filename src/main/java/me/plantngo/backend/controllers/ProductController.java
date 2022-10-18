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
import me.plantngo.backend.models.Product;
import me.plantngo.backend.models.ProductIngredient;
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

    @ApiOperation(value = "Add Ingredient to Product")
    @PostMapping(path = "/{productId}")
    public ResponseEntity<ProductIngredient> addProductIngredient(@PathVariable("productId") Integer productId, 
                                                                @Valid @RequestBody ProductIngredientDTO productIngredientDTO) {
        
        ProductIngredient productIngredient = productService.addProductIngredient(productId, productIngredientDTO);
        return new ResponseEntity<>(productIngredient, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update Ingredient in Product")
    @PutMapping(path = "/{productId}")
    public ResponseEntity<ProductIngredient> updateProductIngredient(@PathVariable("productId") Integer productId, 
                                                                @Valid @RequestBody ProductIngredientDTO productIngredientDTO) {
        
        ProductIngredient productIngredient = productService.updateProductIngredient(productId, productIngredientDTO);
        return new ResponseEntity<>(productIngredient, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete Ingredient in Product")
    @DeleteMapping(path = "/{productId}/{productIngredientName}")
    public ResponseEntity<String> deleteProductIngredient(@PathVariable("productId") Integer productId, 
                                                                    @PathVariable("productIngredientName") String productIngredientName) {
        
        productService.deleteProductIngredient(productId, productIngredientName);
        return new ResponseEntity<>("Product Ingredient deleted!", HttpStatus.OK);
    }

    @GetMapping(path = "/test")
    public List<ProductIngredient> getAllProductIngredients() {
        return productService.getAllProductIngredients();
    }
}
