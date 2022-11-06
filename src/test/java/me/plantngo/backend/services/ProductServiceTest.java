package me.plantngo.backend.services;

import org.junit.jupiter.api.BeforeEach;
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
    public void initEach() {

    }
}
