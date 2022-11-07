package me.plantngo.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.mail.Multipart;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import me.plantngo.backend.DTO.CategoryDTO;
import me.plantngo.backend.DTO.ProductDTO;
import me.plantngo.backend.DTO.UpdateCategoryDTO;
import me.plantngo.backend.DTO.UpdateProductDTO;
import me.plantngo.backend.exceptions.AlreadyExistsException;
import me.plantngo.backend.exceptions.NotExistException;
import me.plantngo.backend.models.Category;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.models.Product;
import me.plantngo.backend.repositories.CategoryRepository;
import me.plantngo.backend.repositories.MerchantRepository;
import me.plantngo.backend.repositories.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ShopServiceTest {
    
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private AWSS3Service awss3Service;

    @InjectMocks
    private ShopService shopService;

    private Merchant merchant;

    private Category category;

    private Product product;

    private MultipartFile file;

    @BeforeEach
    public void initEach() {

        file = mock(MultipartFile.class);

        merchant = new Merchant();
        merchant.setUsername("Daniel");
        merchant.setAddress("42 Spring Crescent");

        category = new Category();
        category.setName("Food");
        
        Category category2 = new Category();
        category2.setName("Dessert");

        product = new Product();
        product.setName("Laksa");
        product.setDescription("It's Laksa");
        product.setPrice(6.1);
        product.setCarbonEmission(0.0);
        product.setCategory(category);

        category.setMerchant(merchant);
        category2.setMerchant(merchant);

        category.setProducts(List.of(product));
        merchant.setCategories(List.of(category, category2));
    }

    @Test
    public void testAddCategory_NewCategory_ReturnCategory() {
        

        // Arrange
        CategoryDTO categoryDTO = new CategoryDTO("Food");
        category.setProducts(null);
        when(categoryRepository.existsByNameAndMerchant(any(String.class), any(Merchant.class)))
            .thenReturn(false);
        when(categoryRepository.save(any(Category.class)))
            .thenReturn(category);

        // Act
        Category responseCategory = shopService.addCategory(merchant, categoryDTO);

        // Assert
        assertEquals(category, responseCategory);
        verify(categoryRepository, times(1)).existsByNameAndMerchant(categoryDTO.getName(), merchant);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    public void testAddCategory_ExistingCategory_ThrowAlreadyExistException() {

        // Arrange
        CategoryDTO categoryDTO = new CategoryDTO("Dessert");
        when(categoryRepository.existsByNameAndMerchant(any(String.class), any(Merchant.class)))
            .thenReturn(true);
        String exceptionMsg = "";

        // Act
        try {
            shopService.addCategory(merchant, categoryDTO);
        } catch (Exception e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Category already exists!", exceptionMsg);
        verify(categoryRepository, times(1)).existsByNameAndMerchant(categoryDTO.getName(), merchant);

    }

    @Test
    public void testGetCategory_CategoryExists_ReturnCategory() {

        // Arrange
        Optional<Category> optionalCategory = Optional.of(category);
        when(categoryRepository.findByNameAndMerchant(any(String.class), any(Merchant.class)))
            .thenReturn(optionalCategory);

        // Act
        Category responseCategory = shopService.getCategory(merchant, category.getName());

        // Assert
        assertEquals(category, responseCategory);
        verify(categoryRepository, times(1)).findByNameAndMerchant(category.getName(), merchant);
    }

    @Test
    public void testGetCategory_CategoryNotExist_ThrowNotExistException() {

        // Arrange
        when(categoryRepository.findByNameAndMerchant(any(String.class), any(Merchant.class)))
            .thenReturn(Optional.empty());
        String exceptionMsg = "";

        // Act
        try {
            Category responseCategory = shopService.getCategory(merchant, category.getName());
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Category doesn't exist!", exceptionMsg);
        verify(categoryRepository, times(1)).findByNameAndMerchant(category.getName(), merchant);
    }

    @Test
    public void testDeleteCategory_CategoryExists_Success() {

        // Arrange
        when(categoryRepository.findByNameAndMerchant(any(String.class), any(Merchant.class)))
            .thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).delete(any(Category.class));
        String exceptionMsg = "";

        // Act
        try {
            shopService.deleteCategory(merchant, category.getName());
        } catch (Exception e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("", exceptionMsg);
        verify(categoryRepository, times(2)).findByNameAndMerchant(category.getName(), merchant);
        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    public void testDeleteCategory_CategoryNotExist_ThrowNotExistException() {

        // Arrange
        when(categoryRepository.findByNameAndMerchant(any(String.class), any(Merchant.class)))
            .thenReturn(Optional.empty());
        String exceptionMsg = "";

        // Act
        try {
            shopService.deleteCategory(merchant, category.getName());
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Category doesn't exist!", exceptionMsg);
        verify(categoryRepository, times(1)).findByNameAndMerchant(category.getName(), merchant);

    }

    @Test
    public void testUpdateCategory_CategoryNameAlreadyExists_ThrowAlreadyExistException() {

        // Arrange
        String exceptionMsg = "";
        String categoryName = "Food";
        when(categoryRepository.existsByName(any(String.class)))
            .thenReturn(true);
        when(categoryRepository.findByNameAndMerchant(any(String.class), any(Merchant.class)))
            .thenReturn(Optional.of(category));
        UpdateCategoryDTO updateCategoryDTO = new UpdateCategoryDTO("Dessert");

        // Act
        try {
            shopService.updateCategory(merchant, categoryName, updateCategoryDTO);
        } catch (Exception e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Category already exists!", exceptionMsg);
        verify(categoryRepository, times(1)).existsByName(updateCategoryDTO.getName());
        verify(categoryRepository, times(1)).findByNameAndMerchant(categoryName, merchant);
    }

    @Test
    public void testUpdateCategory_CategoryNameValid_Success() {

        // Arrange
        String categoryName = "Food";
        when(categoryRepository.existsByName(any(String.class)))
            .thenReturn(false);
        when(categoryRepository.findByNameAndMerchant(any(String.class), any(Merchant.class)))
            .thenReturn(Optional.of(category));
        UpdateCategoryDTO updateCategoryDTO = new UpdateCategoryDTO("Drinks");

        // Act
        Category responseCategory = shopService.updateCategory(merchant, categoryName, updateCategoryDTO);

        // Assert
        assertEquals(category, responseCategory);
        verify(categoryRepository, times(1)).existsByName(updateCategoryDTO.getName());
        verify(categoryRepository, times(1)).findByNameAndMerchant(categoryName, merchant);
        verify(categoryRepository, times(1)).saveAndFlush(category);
    }

    @Test
    public void testGetProduct_ProductNotExist_ThrowNotExistException() {

        // Arrange
        String exceptionMsg = "";
        String categoryName = "Food";
        String productName = "Sprite";
        when(categoryRepository.findByNameAndMerchant(any(String.class), any(Merchant.class)))
            .thenReturn(Optional.of(category));
        when(productRepository.findByNameAndCategory(any(String.class), any(Category.class)))
            .thenReturn(Optional.empty());

        // Act
        try {
            shopService.getProduct(merchant, categoryName, productName);
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Product doesn't exist!", exceptionMsg);
        verify(productRepository, times(1)).findByNameAndCategory(productName, category);
        verify(categoryRepository, times(1)).findByNameAndMerchant(categoryName, merchant);
    }

    @Test
    public void testGetProduct_ProductExists_ReturnProduct() {

        // Arrange
        String categoryName = "Food";
        String productName = "Laksa";
        when(categoryRepository.findByNameAndMerchant(any(String.class), any(Merchant.class)))
            .thenReturn(Optional.of(category));
        when(productRepository.findByNameAndCategory(any(String.class), any(Category.class)))
            .thenReturn(Optional.of(product));

        // Act
        Product responseProduct = shopService.getProduct(merchant, categoryName, productName);

        // Assert
        assertEquals(product, responseProduct);
        verify(productRepository, times(1)).findByNameAndCategory(productName, category);
        verify(categoryRepository, times(1)).findByNameAndMerchant(categoryName, merchant);
    }

    @Test
    public void testAddProduct_ValidProduct_ReturnProduct() throws MalformedURLException {

        // Arrange
        String categoryName = "Food";
        String imageUrl = "https://google.com.sg";
        ProductDTO productDTO = new ProductDTO("Bee Hoon", 5.5, "Yellow Noodles", null, null, null);
        Product expectedProduct = new Product(null, "Bee Hoon", 5.5, "Yellow Noodles", 0.0, new URL("https://google.com.sg"), null, category, null, null);

        when(categoryRepository.existsByNameAndMerchant(any(String.class), any(Merchant.class)))
            .thenReturn(true);
        when(categoryRepository.findByNameAndMerchant(any(String.class), any(Merchant.class)))
            .thenReturn(Optional.of(category));
        when(awss3Service.uploadFile(any(MultipartFile.class)))
            .thenReturn(imageUrl);
        when(productRepository.save(any(Product.class)))
            .thenReturn(expectedProduct);

        // Act
        Product responseProduct = null;
        try {
            responseProduct = shopService.addProduct(merchant, categoryName, productDTO, file);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Assert
        assertEquals(expectedProduct, responseProduct);
        verify(categoryRepository, times(1)).existsByNameAndMerchant(categoryName, merchant);
        verify(categoryRepository, times(1)).findByNameAndMerchant(categoryName, merchant);
        verify(awss3Service, times(1)).uploadFile(file);
        verify(productRepository, times(1)).save(expectedProduct);
    }

    @Test
    public void testAddProduct_InvalidCategory_ThrowNotExistException() {

        // Arrange
        String categoryName = "Appetizer";
        String exceptionMsg = "";
        ProductDTO productDTO = new ProductDTO("Bee Hoon", 5.5, "Yellow Noodles", null, null, null);
        when(categoryRepository.existsByNameAndMerchant(any(String.class), any(Merchant.class)))
            .thenReturn(false);
            
        // Act
        try {
            Product responseProduct = shopService.addProduct(merchant, categoryName, productDTO, file);
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Assert
        assertEquals("Category doesn't exist!", exceptionMsg);
        verify(categoryRepository, times(1)).existsByNameAndMerchant(categoryName, merchant);
    }

    @Test
    public void testAddProduct_ProductAlreadyExists_ThrowAlreadyExistsException() {

        // Arrange
        String categoryName = "Food";
        String exceptionMsg = "";
        ProductDTO productDTO = new ProductDTO("Laksa", 6.1, "It's Laksa", null, null, null);
        when(categoryRepository.existsByNameAndMerchant(any(String.class), any(Merchant.class)))
            .thenReturn(true);
        when(categoryRepository.findByNameAndMerchant(any(String.class), any(Merchant.class)))
            .thenReturn(Optional.of(category));

        // Act
        try {
            Product responseProduct = shopService.addProduct(merchant, categoryName, productDTO, file);
        } catch (AlreadyExistsException e) {
            exceptionMsg = e.getMessage();
        } catch (Exception e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Product already exists!", exceptionMsg);
        verify(categoryRepository, times(1)).existsByNameAndMerchant(categoryName, merchant);
        verify(categoryRepository, times(1)).findByNameAndMerchant(categoryName, merchant);
    }

    @Test
    public void testUpdateProduct_ValidUpdateProduct_ReturnUpdatedProduct() {

        // Arrange
        Product expectedProduct = product;
        expectedProduct.setPrice(10.11);
        String productName = "Laksa";
        UpdateProductDTO updateProductDTO = new UpdateProductDTO(null, 10.11, null, null, null, null);
        when(productRepository.findByNameAndCategory(any(String.class), any(Category.class)))
            .thenReturn(Optional.of(product));
        when(productRepository.saveAndFlush(any(Product.class)))
            .thenReturn(expectedProduct);

        // Act
        Product responseProduct = shopService.updateProduct(category, productName, updateProductDTO);

        // Assert
        assertEquals(expectedProduct, responseProduct);
        verify(productRepository, times(1)).findByNameAndCategory(productName, category);
        verify(productRepository, times(1)).saveAndFlush(expectedProduct);
        
    }

    @Test
    public void testUpdateProduct_ProductNotExist_ThrowNotExistException() {

        // Arrange
        String productName = "Bee Hiang";
        String exceptionMsg = "";
        UpdateProductDTO updateProductDTO = new UpdateProductDTO(null, 10.11, null, null, null, null);
        when(productRepository.findByNameAndCategory(any(String.class), any(Category.class)))
            .thenReturn(Optional.empty());
            
        // Act
        try {
            Product responseProduct = shopService.updateProduct(category, productName, updateProductDTO);
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }   

        // Assert
        assertEquals("Product doesn't exist!", exceptionMsg);
        verify(productRepository, times(1)).findByNameAndCategory(productName, category);
        
    }

    @Test
    public void testUpdateProduct_ProductNameAlreadyExists_ThrowAlreadyExistsException() {

        // Arrange
        Product product2 = new Product();
        product2.setName("Bee Hoon");
        category.setProducts(List.of(product, product2));
        String productName = "Laksa";
        String exceptionMsg = "";
        UpdateProductDTO updateProductDTO = new UpdateProductDTO("Bee Hoon", null, null, null, null, null);
        when(productRepository.findByNameAndCategory(any(String.class), any(Category.class)))
            .thenReturn(Optional.of(product));
            
        // Act
        try {
            Product responseProduct = shopService.updateProduct(category, productName, updateProductDTO);
        } catch (AlreadyExistsException e) {
            exceptionMsg = e.getMessage();
        }   

        // Assert
        assertEquals("Product Name already exists!", exceptionMsg);
        verify(productRepository, times(1)).findByNameAndCategory(productName, category);
        
    }
}
