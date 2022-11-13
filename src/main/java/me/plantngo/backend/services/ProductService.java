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

    private static final String PRODUCT_STRING = "Product";

    private static final String PRODUCT_INGREDIENT_STRING = "Product Ingredient";

    @Autowired
    public ProductService(ProductRepository productRepository, ProductIngredientRepository productIngredientRepository, IngredientRepository ingredientRepository) {
        this.productRepository = productRepository;
        this.productIngredientRepository = productIngredientRepository;
        this.ingredientRepository = ingredientRepository;
    }

    /**
     * Gets all products
     * 
     * @return
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Gets all product ingredients
     * 
     * @return
     */
    public List<ProductIngredient> getAllProductIngredients() {
        return productIngredientRepository.findAll();
    }

    /**
     * Gets all product ingredients given Merchant and Product's name
     * 
     * @param merchantName
     * @param productName
     * @return
     */
    public List<ProductIngredient> getProductIngredientsByMerchantAndProduct(String merchantName, String productName) {
        return productIngredientRepository.findByProductNameAndProductCategoryMerchantUsername(productName, merchantName);
    }

    /**
     * Gets a product given it's Id
     * 
     * @param productId
     * @return
     */
    public Product getProductById(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotExistException(PRODUCT_STRING));
    }

    /**
     * Gets a product given its name
     * 
     * @param productName
     * @return
     */
    public Product getProductByName(String productName) {
        return productRepository.findByName(productName)
                .orElseThrow(() -> new NotExistException(PRODUCT_STRING));
    }

    /**
     * Gets a product given its name and Merchant
     * 
     * @param productName
     * @param merchantName
     * @return
     */
    public Product getProductByNameAndMerchantName(String productName, String merchantName) {
        return productRepository.findByNameAndCategoryMerchantUsername(productName, merchantName)
                .orElseThrow(() -> new NotExistException(PRODUCT_STRING));
    }

    /**
     * Gets an ingredient given its name
     * 
     * @param name
     * @return
     */
    public Ingredient getIngredientByName(String name) {
        return ingredientRepository.findByName(name)
                .orElseThrow(() -> new NotExistException("Ingredient"));
    }

    /**
     * Gets all product ingredients given the ingredient it's based off, the Product and Merchant
     * 
     * @param ingredient
     * @param product
     * @param merchantName
     * @return
     */
    public ProductIngredient getProductIngredientByIngredientAndProductAndProductCategoryMerchantUsername(Ingredient ingredient, Product product, String merchantName) {
        return productIngredientRepository.findByIngredientAndProductAndProductCategoryMerchantUsername(ingredient, product, merchantName)
                .orElseThrow(() -> new NotExistException(PRODUCT_INGREDIENT_STRING));
    }

    /**
     * Gets all products by a given Merchant
     * 
     * @param merchantName
     * @return
     */
    public List<Product> getAllProductsByMerchant(String merchantName) {
        return productRepository.findByCategoryMerchantUsernameOrderByCarbonEmission(merchantName);
    }

    /**
     * Adds a product ingredient to a given Product by a Merchant
     * 
     * @param merchantName
     * @param productName
     * @param productIngredientDTO
     * @return
     */
    public ProductIngredient addProductIngredient(String merchantName, String productName, ProductIngredientDTO productIngredientDTO) {
        Product product = this.getProductByName(productName);
        Ingredient ingredient = this.getIngredientByName(productIngredientDTO.getName());
        if (productIngredientRepository.existsByIngredientAndProductAndProductCategoryMerchantUsername(ingredient, product, merchantName)) {
            throw new AlreadyExistsException(PRODUCT_INGREDIENT_STRING);
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

    /**
     * Updates a product ingredient in a given Product by a Merchant
     * 
     * @param merchantName
     * @param productName
     * @param productIngredientDTO
     * @return
     */
    public ProductIngredient updateProductIngredient(String merchantName,
            String productName,
            ProductIngredientDTO productIngredientDTO) {

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

    /**
     * Deletes all product ingredients from a given Product by a Merchant
     * 
     * @param merchantName
     * @param productName
     */
    public void deleteAllProductIngredients(String merchantName, String productName) {
        Product product = this.getProductByNameAndMerchantName(productName, merchantName);

        Set<ProductIngredient> productIngredients = product.getProductIngredients();
        productIngredients.clear();

        product.setProductIngredients(productIngredients);
        product.setCarbonEmission(this.calculateTotalEmissions(productIngredients));
        Merchant merchant = product.getCategory().getMerchant();
        merchant.setCarbonRating(this.calculateCarbonRating(product));

        productRepository.save(product);
    }

    /**
     * Deletes a specified product ingredient in a given Product by a Merchant
     * 
     * @param merchantName
     * @param productName
     * @param productIngredientName
     */
    public void deleteProductIngredient(String merchantName, String productName,  String productIngredientName) {
        Product product = this.getProductByName(productName);
        Ingredient ingredient = this.getIngredientByName(productIngredientName);
        ProductIngredient productIngredient = this.getProductIngredientByIngredientAndProductAndProductCategoryMerchantUsername(ingredient, product, merchantName);

        Set<ProductIngredient> productIngredients = product.getProductIngredients();
        if (!productIngredients.contains(productIngredient)) {
            throw new NotExistException(PRODUCT_INGREDIENT_STRING);
        }

        productIngredients.remove(productIngredient);
        product.setProductIngredients(productIngredients);
        product.setCarbonEmission(this.calculateTotalEmissions(productIngredients));
        Merchant merchant = product.getCategory().getMerchant();
        merchant.setCarbonRating(this.calculateCarbonRating(product));

        productRepository.save(product);
    }

    /*
     * 
     * Helper Methods
     * 
     */

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
