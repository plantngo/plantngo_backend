package me.plantngo.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import me.plantngo.backend.DTO.CategoryDTO;
import me.plantngo.backend.models.Category;
import me.plantngo.backend.models.Merchant;
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

    @InjectMocks
    private ShopService shopService;

    private Merchant merchant;

    private Category category;

    @BeforeEach
    public void initEach() {
        merchant = new Merchant();
        merchant.setUsername("Daniel");
        merchant.setAddress("42 Spring Crescent");
        merchant.setCategories(List.of(new Category(1, "Dessert", null, merchant)));

        category = new Category();
        category.setName("Food");
        category.setMerchant(merchant);

    }

    @Test
    public void testAddCategory_NewCategory_ReturnCategory() {
        
        // Arrange
        CategoryDTO categoryDTO = new CategoryDTO("Food");
        when(categoryRepository.existsByNameAndMerchant(any(String.class), any(Merchant.class)))
            .thenReturn(false);
        when(categoryRepository.save(any(Category.class)))
            .thenReturn(category);

        // Act
        Category savedCategory = shopService.addCategory(merchant, categoryDTO);

        // Assert
        assertEquals(category, savedCategory);
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
        assertEquals("Category already exists", exceptionMsg);
        verify(categoryRepository, times(1)).existsByNameAndMerchant(categoryDTO.getName(), merchant);

    }

    @Test
    public void testGetCategory_CategoryExists_ReturnCategory() {

        // Arrange
        
    }
}
