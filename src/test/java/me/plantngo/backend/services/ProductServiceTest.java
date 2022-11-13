package me.plantngo.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import me.plantngo.backend.DTO.ProductIngredientDTO;
import me.plantngo.backend.exceptions.AlreadyExistsException;
import me.plantngo.backend.exceptions.NotExistException;
import me.plantngo.backend.models.Category;
import me.plantngo.backend.models.Ingredient;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.models.Product;
import me.plantngo.backend.models.ProductIngredient;
import me.plantngo.backend.repositories.IngredientRepository;
import me.plantngo.backend.repositories.ProductIngredientRepository;
import me.plantngo.backend.repositories.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductIngredientRepository productIngredientRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    private ProductIngredient productIngredient;

    private Ingredient ingredient;

    @BeforeEach
    void initEach() {

        Merchant merchant = new Merchant();
        merchant.setUsername("Daniel");

        Category category = new Category();
        category.setMerchant(merchant);
        merchant.setCategories(List.of(category));

        ingredient = new Ingredient(null, null, "Beef", null, 10.0, null);
        Ingredient ingredient2 = new Ingredient(null, null, "Coffee", null, 20.0, null);

        product = new Product(1, "Steak", 10.0, null, 10.0, null, null, category, null, new HashSet<>());
        productIngredient = new ProductIngredient(null, 1.0, ingredient, product);

        List<ProductIngredient> productIngredientList = new ArrayList<>();
        productIngredientList.add(productIngredient);

        List<Product> productList = new ArrayList<>();
        productList.add(product);
        category.setProducts(productList);

        ingredient.setProductIngredients(productIngredientList);
    }

    @Test
    void testGetAllProducts_AllOrders_ReturnAllOrders() {
        
        // Arrange
        Product product2 = new Product();
        product2.setName("Sprite");
        Product product3 = new Product();
        product3.setName("Broccoli");
        Product product4 = new Product();
        product4.setName("Lettuce");
        List<Product> expectedProducts = new ArrayList<>();
        expectedProducts.add(product);
        expectedProducts.add(product2);
        expectedProducts.add(product3);
        expectedProducts.add(product4);

        when(productRepository.findAll())
            .thenReturn(expectedProducts);

        // Act
        List<Product> responseProducts = productService.getAllProducts();

        // Assert
        assertEquals(expectedProducts, responseProducts);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetAllProducts_NoOrders_ReturnEmptyList() {

        // Arrange
        List<Product> expectedProducts = new ArrayList<>();
        when(productRepository.findAll())
            .thenReturn(expectedProducts);

        // Act
        List<Product> responseProducts = productService.getAllProducts();

        // Assert
        assertEquals(expectedProducts, responseProducts);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetAllProductIngredients_AllProductIngredients_ReturnAllProductIngredients() {

        // Arrange
        ProductIngredient productIngredient2 = new ProductIngredient();
        productIngredient2.setId(1);
        ProductIngredient productIngredient3 = new ProductIngredient();
        productIngredient2.setId(2);
        ProductIngredient productIngredient4 = new ProductIngredient();
        productIngredient2.setId(3);
        List<ProductIngredient> expectedProductIngredients = new ArrayList<>();
        expectedProductIngredients.add(productIngredient);
        expectedProductIngredients.add(productIngredient2);
        expectedProductIngredients.add(productIngredient3);
        expectedProductIngredients.add(productIngredient4);

        when(productIngredientRepository.findAll())
            .thenReturn(expectedProductIngredients);

        // Act
        List<ProductIngredient> responseProductIngredients = productService.getAllProductIngredients();

        // Assert
        assertEquals(expectedProductIngredients, responseProductIngredients);
        verify(productIngredientRepository, times(1)).findAll();
    }

    @Test
    void testGetAllProductIngredients_NoProductIngredients_ReturnEmptyList() {

        // Arrange
        List<ProductIngredient> expectedProductIngredients = new ArrayList<>();

        when(productIngredientRepository.findAll())
            .thenReturn(expectedProductIngredients);

        // Act
        List<ProductIngredient> responseProductIngredients = productService.getAllProductIngredients();

        // Assert
        assertEquals(expectedProductIngredients, responseProductIngredients);
        verify(productIngredientRepository, times(1)).findAll();
    }

    @Test
    void testGetProductIngredientByMerchantAndProduct_ProductIngredientsExists_ReturnProductIngredients() {

        // Arrange
        String productName = "Steak";
        String merchantName = "Daniel";
        List<ProductIngredient> expectedProductIngredients = List.of(productIngredient);

        when(productIngredientRepository.findByProductNameAndProductCategoryMerchantUsername(any(String.class), any(String.class)))
            .thenReturn(expectedProductIngredients);
        
        // Act
        List<ProductIngredient> responseProductIngredients = productService.getProductIngredientsByMerchantAndProduct(merchantName, productName);

        // Assert
        assertEquals(expectedProductIngredients, responseProductIngredients);
        verify(productIngredientRepository, times(1)).findByProductNameAndProductCategoryMerchantUsername(productName, merchantName);
    }

    @Test
    void testGetProductById_InvalidId_ThrowNotExistsException() {

        // Arrange
        Integer id = 5;
        String exceptionMsg = "";

        when(productRepository.findById(any(Integer.class)))
            .thenReturn(Optional.empty());

        // Act
        try {
            Product responseProduct = productService.getProductById(id);
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Product doesn't exist!", exceptionMsg);
        verify(productRepository, times(1)).findById(id);
    }

    @Test
    void testGetProductByName_ProductExists_ReturnProduct() {

        // Arrange
        String productName = "Steak";
        Product expectedProduct = product;

        when(productRepository.findByName(any(String.class)))
            .thenReturn(Optional.of(expectedProduct));

        // Act
        Product responseProduct = productService.getProductByName(productName);

        // Assert
        assertEquals(expectedProduct, responseProduct);
        verify(productRepository, times(1)).findByName(productName);
    }

    @Test
    void testGetProductByNameAndMerchantName_ProductExists_ReturnProduct() {

        // Arrange
        String productName = "Steak";
        String merchantName = "Daniel";
        Product expectedProduct = product;

        when(productRepository.findByNameAndCategoryMerchantUsername(any(String.class), any(String.class)))
            .thenReturn(Optional.of(expectedProduct));

        // Act
        Product responseProduct = productService.getProductByNameAndMerchantName(productName, merchantName);

        // Assert
        assertEquals(expectedProduct, responseProduct);
        verify(productRepository, times(1)).findByNameAndCategoryMerchantUsername(productName, merchantName);
    }

    @Test
    void testGetIngredientByName_IngredientExists_ReturnIngredient() {

        // Arrange
        String ingredientName = "Beef";
        Ingredient expectedIngredient = ingredient;

        when(ingredientRepository.findByName(any(String.class)))
            .thenReturn(Optional.of(expectedIngredient));
        
        // Act
        Ingredient responseIngredient = productService.getIngredientByName(ingredientName);

        // Assert
        assertEquals(expectedIngredient, responseIngredient);
        verify(ingredientRepository, times(1)).findByName(ingredientName);
    }

    @Test
    void testGetProductIngredientByIngredientAndProductAndProductCategoryMerchantUsername_ProductIngredientExists_ReturnProductIngredient() {

        // Arrange
        String merchantName = "Daniel";
        ProductIngredient expectedProductIngredient = productIngredient;

        when(productIngredientRepository.findByIngredientAndProductAndProductCategoryMerchantUsername(any(Ingredient.class), any(Product.class), any(String.class)))
            .thenReturn(Optional.of(expectedProductIngredient));

        // Act
        ProductIngredient responseProductIngredient = productService.getProductIngredientByIngredientAndProductAndProductCategoryMerchantUsername(ingredient, product, merchantName);

        // Assert
        assertEquals(expectedProductIngredient, responseProductIngredient);
        verify(productIngredientRepository, times(1)).findByIngredientAndProductAndProductCategoryMerchantUsername(ingredient, product, merchantName);
    }

    @Test
    void testAddProductIngredient_ValidIngredient_ReturnProductIngredient() {

        // Arrange
        String merchantName = "Daniel";
        String productName = "Steak";
        ProductIngredientDTO productIngredientDTO = new ProductIngredientDTO("Beef", 1.0);
        ProductIngredient expectedProductIngredient = productIngredient;
        
        when(productRepository.findByName(any(String.class)))
            .thenReturn(Optional.of(product));
        when(ingredientRepository.findByName(any(String.class)))
            .thenReturn(Optional.of(ingredient));
        when(productIngredientRepository.existsByIngredientAndProductAndProductCategoryMerchantUsername
            (any(Ingredient.class), any(Product.class), any(String.class)))
            .thenReturn(false);
        when(productIngredientRepository.save(any(ProductIngredient.class)))
            .thenReturn(expectedProductIngredient);

        // Act
        ProductIngredient responseProductIngredient = productService.addProductIngredient(merchantName, productName, productIngredientDTO);

        // Assert
        assertEquals(expectedProductIngredient, responseProductIngredient);
        verify(productRepository, times(1)).findByName(productName);
        verify(ingredientRepository, times(1)).findByName(productIngredientDTO.getName());
        verify(productIngredientRepository, times(1)).existsByIngredientAndProductAndProductCategoryMerchantUsername(ingredient, product, merchantName);
        verify(productIngredientRepository, times(1)).save(expectedProductIngredient);
    }

    @Test
    void testAddProductIngredient_ProductIngredientAlreadyExists_ThrowAlreadyExistsException() {

        // Arrange
        String exceptionMsg = "";
        String merchantName = "Daniel";
        String productName = "Steak";
        ProductIngredientDTO productIngredientDTO = new ProductIngredientDTO("Beef", 1.0);
        
        when(productRepository.findByName(any(String.class)))
            .thenReturn(Optional.of(product));
        when(ingredientRepository.findByName(any(String.class)))
            .thenReturn(Optional.of(ingredient));
        when(productIngredientRepository.existsByIngredientAndProductAndProductCategoryMerchantUsername
            (any(Ingredient.class), any(Product.class), any(String.class)))
            .thenReturn(true);

        // Act
        try {
            ProductIngredient responseProduct = productService.addProductIngredient(merchantName, productName, productIngredientDTO);
        } catch (AlreadyExistsException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Product Ingredient already exists!", exceptionMsg);
        verify(productRepository, times(1)).findByName(productName);
        verify(ingredientRepository, times(1)).findByName(productIngredientDTO.getName());
        verify(productIngredientRepository, times(1)).existsByIngredientAndProductAndProductCategoryMerchantUsername(ingredient, product, merchantName);

    }

    @Test
    void testUpdateProductIngredient_ValidProductIngredientDTO_ReturnProductIngredient() {

        // Arrange
        String productName = "Steak";
        String merchantName = "Daniel";
        ProductIngredientDTO productIngredientDTO = new ProductIngredientDTO("Beef", 2.0);
        ProductIngredient expectedProductIngredient = productIngredient;
        expectedProductIngredient.setServingQty(2.0);

        when(productRepository.findByName(any(String.class)))
            .thenReturn(Optional.of(product));
        when(ingredientRepository.findByName(any(String.class)))
            .thenReturn(Optional.of(ingredient));
        when(productIngredientRepository.findByIngredientAndProductAndProductCategoryMerchantUsername(any(Ingredient.class), any(Product.class), any(String.class)))
            .thenReturn(Optional.of(productIngredient));
        when(productIngredientRepository.save(any(ProductIngredient.class)))
            .thenReturn(expectedProductIngredient);

        
        // Act
        ProductIngredient responseProductIngredient = productService.updateProductIngredient(merchantName, productName, productIngredientDTO);

        // Assert
        assertEquals(expectedProductIngredient, responseProductIngredient);
        verify(productRepository, times(1)).findByName(productName);
        verify(ingredientRepository, times(1)).findByName(productIngredientDTO.getName());
        verify(productIngredientRepository, times(1)).findByIngredientAndProductAndProductCategoryMerchantUsername(ingredient, product, merchantName);
        verify(productIngredientRepository, times(1)).save(responseProductIngredient);
    }

    @Test
    void testUpdateProductIngredient_ProductIngredientNotExist_ThrowNotExistException() {

        // Arrange
        String productName = "Steak";
        String merchantName = "Daniel";
        ProductIngredientDTO productIngredientDTO = new ProductIngredientDTO("Beeffffff", 2.0);
        String exceptionMsg = "";

        when(productRepository.findByName(any(String.class)))
            .thenReturn(Optional.of(product));
        when(ingredientRepository.findByName(any(String.class)))
            .thenReturn(Optional.of(ingredient));
        when(productIngredientRepository.findByIngredientAndProductAndProductCategoryMerchantUsername(any(Ingredient.class), any(Product.class), any(String.class)))
            .thenReturn(Optional.empty());

        // Act
        try {
            ProductIngredient responseProductIngredient = productService.updateProductIngredient(merchantName, productName, productIngredientDTO);
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Product Ingredient doesn't exist!", exceptionMsg);
        verify(productRepository, times(1)).findByName(productName);
        verify(ingredientRepository, times(1)).findByName(productIngredientDTO.getName());
        verify(productIngredientRepository, times(1)).findByIngredientAndProductAndProductCategoryMerchantUsername(ingredient, product, merchantName);
    }

    @Test
    void testDeleteAllProductIngredients_ProductExists_ReturnSuccess() {

        // Arrange
        String productName = "Steak";
        String merchantName = "Daniel";

        Merchant merchant = new Merchant();
        merchant.setUsername("Daniel");

        Category category = new Category();
        category.setMerchant(merchant);
        merchant.setCategories(List.of(category));
        
        Product expectedProduct = new Product(1, "Steak", 10.0, null, 0.0, null, null, category, null, new HashSet<>());
        
        Set<ProductIngredient> productIngredients = new HashSet<>();
        productIngredients.add(productIngredient);
        product.setProductIngredients(productIngredients);

        when(productRepository.findByNameAndCategoryMerchantUsername(any(String.class), any(String.class)))
            .thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class)))
            .thenReturn(expectedProduct);

        // Act
        productService.deleteAllProductIngredients(merchantName, productName);

        // Assert
        verify(productRepository, times(1)).findByNameAndCategoryMerchantUsername(productName, merchantName);
        verify(productRepository, times(1)).save(expectedProduct);
    }

    @Test
    void testDeleteAllProductIngredients_ProductNotExists_ThrowNotExistException() {

        // Arrange
        String productName = "Steak";
        String merchantName = "Daniel";
        String exceptionMsg = "";

        when(productRepository.findByNameAndCategoryMerchantUsername(any(String.class), any(String.class)))
            .thenReturn(Optional.empty());

        // Act
        try {
            productService.deleteAllProductIngredients(merchantName, productName);
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Product doesn't exist!", exceptionMsg);
        verify(productRepository, times(1)).findByNameAndCategoryMerchantUsername(productName, merchantName);
    }

    @Test
    void testDeleteProductIngredient_ProductIngredientExist_ReturnSucess() {

        // Arrange
        String productName = "Steak";
        String merchantName = "Daniel";
        String productIngredientName = "Beef";

        Merchant merchant = new Merchant();
        merchant.setUsername("Daniel");

        Category category = new Category();
        category.setMerchant(merchant);
        merchant.setCategories(List.of(category));

        Product expectedProduct = new Product(1, "Steak", 10.0, null, 0.0, null, null, category, null, new HashSet<>());

        Set<ProductIngredient> productIngredients = new HashSet<>();
        productIngredients.add(productIngredient);
        product.setProductIngredients(productIngredients);

        when(productRepository.findByName(any(String.class)))
            .thenReturn(Optional.of(product));
        when(ingredientRepository.findByName(any(String.class)))
            .thenReturn(Optional.of(ingredient));
        when(productIngredientRepository.findByIngredientAndProductAndProductCategoryMerchantUsername(any(Ingredient.class), any(Product.class), any(String.class)))
            .thenReturn(Optional.of(productIngredient));
        when(productRepository.save(any(Product.class)))
            .thenReturn(expectedProduct);
        
        // Act
        productService.deleteProductIngredient(merchantName, productName, productIngredientName);

        // Assert
        verify(productRepository, times(1)).findByName(productName);
        verify(ingredientRepository, times(1)).findByName(productIngredientName);
        verify(productIngredientRepository, times(1)).findByIngredientAndProductAndProductCategoryMerchantUsername(ingredient, expectedProduct, merchantName);
        verify(productRepository, times(1)).save(expectedProduct);
    }    
    
    @Test
    void testDeleteProductIngredient_ProductIngredientNotExist_ThrowNotExistException() {

        // Arrange
        String productName = "Steak";
        String merchantName = "Daniel";
        String productIngredientName = "Fish";
        String exceptionMsg = "";

        Set<ProductIngredient> productIngredients = new HashSet<>();
        productIngredients.add(productIngredient);
        product.setProductIngredients(productIngredients);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("Fish");

        ProductIngredient productIngredient2 = new ProductIngredient();
        productIngredient2.setIngredient(ingredient2);

        when(productRepository.findByName(any(String.class)))
            .thenReturn(Optional.of(product));
        when(ingredientRepository.findByName(any(String.class)))
            .thenReturn(Optional.of(ingredient2));
        when(productIngredientRepository.findByIngredientAndProductAndProductCategoryMerchantUsername(any(Ingredient.class), any(Product.class), any(String.class)))
            .thenReturn(Optional.of(productIngredient2));
        
        // Act
        try {
            productService.deleteProductIngredient(merchantName, productName, productIngredientName);
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Product Ingredient doesn't exist!", exceptionMsg);
        verify(productRepository, times(1)).findByName(productName);
        verify(ingredientRepository, times(1)).findByName(productIngredientName);
        verify(productIngredientRepository, times(1)).findByIngredientAndProductAndProductCategoryMerchantUsername(ingredient2, product, merchantName);
    }
}
