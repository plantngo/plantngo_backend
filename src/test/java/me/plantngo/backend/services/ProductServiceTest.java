package me.plantngo.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import me.plantngo.backend.models.Ingredient;
import me.plantngo.backend.models.Product;
import me.plantngo.backend.models.ProductIngredient;
import me.plantngo.backend.repositories.IngredientRepository;
import me.plantngo.backend.repositories.ProductIngredientRepository;
import me.plantngo.backend.repositories.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    
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

        ingredient = new Ingredient(null, null, "Beef", null, 10.0, null);
        Ingredient ingredient2 = new Ingredient(null, null, "Coffee", null, 20.0, null);

        product = new Product(null, "Steak", 10.0, null, 10.0, null, null, null, null, null);
        productIngredient = new ProductIngredient(null, null, ingredient2, product);

        product.setProductIngredients(Set.of(productIngredient));

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
}
