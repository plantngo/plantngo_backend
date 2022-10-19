package me.plantngo.backend.services;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.plantngo.backend.DTO.ProductIngredientDTO;
import me.plantngo.backend.exceptions.AlreadyExistsException;
import me.plantngo.backend.exceptions.NotExistException;
import me.plantngo.backend.models.Ingredient;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.models.Product;
import me.plantngo.backend.models.ProductIngredient;
import me.plantngo.backend.repositories.IngredientRepository;
import me.plantngo.backend.repositories.ProductIngredientRepository;
import me.plantngo.backend.repositories.ProductRepository;

@Service
public class ProductService {
    
    private final ProductRepository productRepository;

    private final ProductIngredientRepository productIngredientRepository;

    private final IngredientRepository ingredientRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductIngredientRepository productIngredientRepository, IngredientRepository ingredientRepository) {
        this.productRepository = productRepository;
        this.productIngredientRepository = productIngredientRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<ProductIngredient> getAllProductIngredients() {
        return productIngredientRepository.findAll();
    }

    public Product getProductById(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotExistException("Product"));
    }

    public Product getProductByName(String productName) {
        return productRepository.findByName(productName)
                .orElseThrow(() -> new NotExistException("Product"));
    }

    public Ingredient getIngredientByName(String name) {
        return ingredientRepository.findByName(name)
                .orElseThrow(() -> new NotExistException("Ingredient"));
    }

    public ProductIngredient getProductIngredientByIngredientAndProductAndProductCategoryMerchantUsername(Ingredient ingredient, Product product, String merchantName) {
        return productIngredientRepository.findByIngredientAndProductAndProductCategoryMerchantUsername(ingredient, product, merchantName)
                .orElseThrow(() -> new NotExistException("Product Ingredient"));
    }

    public List<Product> getAllProductsByMerchant(String merchantName) {
        return productRepository.findByCategoryMerchantUsernameOrderByCarbonEmission(merchantName);
    }

    public ProductIngredient addProductIngredient(String merchantName, String productName, @Valid ProductIngredientDTO productIngredientDTO) {
        Product product = this.getProductByName(productName);
        Ingredient ingredient = this.getIngredientByName(productIngredientDTO.getName());
        if (productIngredientRepository.existsByIngredientAndProductAndProductCategoryMerchantUsername(ingredient, product, merchantName)) {
            throw new AlreadyExistsException("Product Ingredient");
        }

        ProductIngredient productIngredient = this.productIngredientMapToEntity(productIngredientDTO, product, ingredient);

        // Update list of ingredients in Product
        Set<ProductIngredient> productIngredients = product.getProductIngredients();
        productIngredients.add(productIngredient);

        // Save all the new values in product
        product.setCarbonEmission(this.calculateTotalEmissions(productIngredients));
        product.setProductIngredients(productIngredients);
        Merchant merchant = product.getCategory().getMerchant();
        merchant.setCarbonRating(this.calculateCarbonRating(product));

        // Add ProductIngredient to Repo + Update Product in Repo
        productIngredientRepository.save(productIngredient);

        return productIngredient;
    }

    public ProductIngredient updateProductIngredient(String merchantName,
            String productName,
            @Valid ProductIngredientDTO productIngredientDTO) {

        Product product = this.getProductByName(productName);
        Ingredient ingredient = this.getIngredientByName(productIngredientDTO.getName());
        ProductIngredient productIngredient =  this.getProductIngredientByIngredientAndProductAndProductCategoryMerchantUsername(ingredient, product, merchantName);

        // Set new servingQty
        productIngredient.setServingQty(productIngredientDTO.getServingQty());

        // Update Ingredients list in Product
        Set<ProductIngredient> productIngredients = product.getProductIngredients();
        productIngredients.remove(productIngredient);
        productIngredients.add(productIngredient);
        product.setProductIngredients(productIngredients);
        product.setCarbonEmission(this.calculateTotalEmissions(productIngredients));
        Merchant merchant = product.getCategory().getMerchant();
        merchant.setCarbonRating(this.calculateCarbonRating(product));

        // Add ProductIngredient to Repo + Update Product in Repo
        productIngredientRepository.save(productIngredient);

        return productIngredient;
    }


    public void deleteProductIngredient(String merchantName, String productName,  String productIngredientName) {
        Product product = this.getProductByName(productName);
        Ingredient ingredient = this.getIngredientByName(productIngredientName);
        ProductIngredient productIngredient = this.getProductIngredientByIngredientAndProductAndProductCategoryMerchantUsername(ingredient, product, merchantName);

        Set<ProductIngredient> productIngredients = product.getProductIngredients();
        if (!productIngredients.contains(productIngredient)) {
            throw new NotExistException("Product Ingredient");
        }

        productIngredients.remove(productIngredient);
        product.setProductIngredients(productIngredients);
        product.setCarbonEmission(this.calculateTotalEmissions(productIngredients));
        Merchant merchant = product.getCategory().getMerchant();
        merchant.setCarbonRating(this.calculateCarbonRating(product));

        productRepository.save(product);
    }

    private Double calculateCarbonRating(Product product) {
        List<Product> products = product.getCategory().getProducts();
        double totalCarbonEmissions = 0.0;
        int size = products.size();

        for (Product p : products) {
            totalCarbonEmissions += p.getCarbonEmission();
        }

        return Double.valueOf(totalCarbonEmissions / size);
    }

    private Double calculateTotalEmissions(Set<ProductIngredient> productIngredients) {
        Double totalEmissions = 0.0;

        for(ProductIngredient p : productIngredients) {
            double emission = p.getIngredient().getEmissionPerGram().doubleValue() * p.getServingQty().doubleValue();
            totalEmissions += Double.valueOf(emission);
        }

        return totalEmissions;
    }

    private ProductIngredient productIngredientMapToEntity(@Valid ProductIngredientDTO productIngredientDTO,
            Product product, Ingredient ingredient) {
        
        ModelMapper mapper = new ModelMapper();
        ProductIngredient productIngredient = mapper.map(productIngredientDTO, ProductIngredient.class);

        productIngredient.setProduct(product);
        productIngredient.setIngredient(ingredient);

        return productIngredient;
    }

}
