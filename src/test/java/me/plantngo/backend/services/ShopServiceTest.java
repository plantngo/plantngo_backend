package me.plantngo.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import me.plantngo.backend.DTO.CategoryDTO;
import me.plantngo.backend.DTO.UpdateCategoryDTO;
import me.plantngo.backend.exceptions.AlreadyExistsException;
import me.plantngo.backend.exceptions.NotExistException;
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

        category = new Category();
        category.setName("Food");
        category.setMerchant(merchant);

        List<Category> categoryList = new ArrayList<>();
        categoryList.add(category);
        
        Category category2 = new Category();
        category2.setName("Dessert");
        category2.setMerchant(merchant);

        categoryList.add(category2);

        merchant.setCategories(categoryList);
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
            exceptionMsg = "Category already exists";
        }

        // Assert
        assertEquals("Category already exists", exceptionMsg);
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
        Category expectedCategory = new Category();
        expectedCategory.setMerchant(merchant);
        expectedCategory.setName("Drinks");

        // Act
        Category responseCategory = shopService.updateCategory(merchant, categoryName, updateCategoryDTO);

        // Assert
        assertEquals(expectedCategory, responseCategory);
        verify(categoryRepository, times(1)).existsByName(updateCategoryDTO.getName());
        verify(categoryRepository, times(1)).findByNameAndMerchant(categoryName, merchant);
        verify(categoryRepository, times(1)).saveAndFlush(responseCategory);
    }
}
