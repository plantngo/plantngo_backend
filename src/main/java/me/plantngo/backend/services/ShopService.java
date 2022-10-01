package me.plantngo.backend.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import me.plantngo.backend.DTO.ProductDTO;
import me.plantngo.backend.DTO.UpdateProductDTO;
import me.plantngo.backend.exceptions.AlreadyExistsException;
import me.plantngo.backend.exceptions.NotExistException;
import me.plantngo.backend.exceptions.UserNotFoundException;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.models.Product;
import me.plantngo.backend.repositories.MerchantRepository;
import me.plantngo.backend.repositories.ProductRepository;

@Service
public class ShopService {
    
    private ProductRepository productRepository;
    private MerchantRepository merchantRepository;

    @Autowired
    public ShopService(ProductRepository productRepository, MerchantRepository merchantRepository) {
        this.productRepository = productRepository;
        this.merchantRepository = merchantRepository;
    }

    public Product addProduct(Merchant merchant, @Valid ProductDTO productDTO) {

        // Check to see if same product under merchant already exists
        if (productRepository.findByNameAndMerchant(productDTO.getName(), merchant).isPresent()) {
            throw new AlreadyExistsException();
        }

        // Creating  & Saving Product object
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setCarbonEmission(productDTO.getCarbonEmission());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setMerchant(merchant);

        productRepository.save(product);

        return product;
    }

    public Product updateProduct(Merchant merchant, String productName, UpdateProductDTO updateProductDTO) {
        
        // Check to see if product exists under merchant
        Optional<Product> tempProduct = productRepository.findByNameAndMerchant(productName, merchant);
        if (tempProduct.isEmpty()) {
            throw new NotExistException();
        }

        // If changing product name, check to see if another product with that name already exists
        if (!updateProductDTO.getName().equals(productName) && productRepository.existsByName(updateProductDTO.getName())) {
            throw new AlreadyExistsException();
        }

        // Updating product
        Product product = tempProduct.get();

        if (updateProductDTO.getCarbonEmission() != null) {
            product.setCarbonEmission(updateProductDTO.getCarbonEmission());
        }
        if (updateProductDTO.getDescription() != null) {
            product.setDescription(updateProductDTO.getDescription());
        }
        if (updateProductDTO.getName() != null) {
            product.setName(updateProductDTO.getName());
        }
        if (updateProductDTO.getPrice() != null) {
            product.setPrice(updateProductDTO.getPrice());
        }

        // In case we need to call it before method ends
        productRepository.saveAndFlush(product);

        return product;
    }

    public void deleteProduct(Merchant merchant, String productName) {
        
        // Check to see if same product under merchant already exists
        if (productRepository.findByNameAndMerchant(productName, merchant).isEmpty()) {
            throw new NotExistException();
        }

        Product product = productRepository.findByNameAndMerchant(productName, merchant).get();
        productRepository.delete(product);
    }

    public List<Product> getAllProductsByMerchant(Merchant merchant) {
        return productRepository.findByMerchant(merchant);
    }
}
