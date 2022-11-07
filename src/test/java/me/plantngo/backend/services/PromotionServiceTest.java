package me.plantngo.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import me.plantngo.backend.exceptions.PromotionNotFoundException;
import me.plantngo.backend.models.Product;
import me.plantngo.backend.models.Promotion;
import me.plantngo.backend.repositories.PromotionRepository;

@ExtendWith(MockitoExtension.class)
public class PromotionServiceTest {
    
    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private PromotionService promotionService;

    private Promotion promotion;

    @BeforeEach
    void initEach() {
        
        promotion = new Promotion();
        promotion.setId(1);
        promotion.setDescription("It's a promotion");

    }

    @Test
    void testGetPromotionById_PromotionExists_ReturnPromotion() {

        // Arrange
        Integer id = 1;
        Promotion expectedPromotion = new Promotion();
        expectedPromotion.setId(1);
        expectedPromotion.setDescription("It's a promotion");

        when(promotionRepository.findById(any(Integer.class)))
            .thenReturn(Optional.of(promotion));

        // Act
        Promotion responsePromotion = promotionService.getPromotionById(id);

        // Assert
        assertEquals(expectedPromotion, responsePromotion);
        verify(promotionRepository, times(2)).findById(id);
    }

    @Test
    void testGetPromotionById_PromotionNotExist_ThrowPromotionNotFoundException() {

        // Arrange
        Integer id = 1;
        String exceptionMsg = "";

        when(promotionRepository.findById(any(Integer.class)))
            .thenReturn(Optional.empty());

        // Act
        try {
            Promotion responsePromotion = promotionService.getPromotionById(id);
        } catch (PromotionNotFoundException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Promotion Not Found:PromotionId 1 does not exist.", exceptionMsg);
        verify(promotionRepository, times(1)).findById(id);
    }

    @Test
    void testGetAllPromotions_AllPromotions_ReturnAllPromotions() {

        // Arrange
        Promotion promotion2 = new Promotion();
        promotion2.setId(2);
        promotion2.setDescription("It's a promotion");
        Promotion promotion3 = new Promotion();
        promotion3.setId(3);
        promotion3.setDescription("It's a promotion");
        Promotion promotion4 = new Promotion();
        promotion4.setId(4);
        promotion4.setDescription("It's a promotion");

        List<Promotion> expectedPromotions = new ArrayList<>();
        expectedPromotions.add(promotion);
        expectedPromotions.add(promotion2);
        expectedPromotions.add(promotion3);
        expectedPromotions.add(promotion4);

        when(promotionRepository.findAll())
            .thenReturn(expectedPromotions);

        // Act
        List<Promotion> responsePromotions = promotionService.getAllPromotions();

        // Assert
        assertEquals(expectedPromotions, responsePromotions);
        verify(promotionRepository, times(1)).findAll();
    }

    @Test
    void testGetAllPromotions_NoPromotions_ReturnEmptyList() {

        // Arrange
        List<Promotion> expectedPromotions = new ArrayList<>();

        when(promotionRepository.findAll())
            .thenReturn(expectedPromotions);

        // Act
        List<Promotion> responsePromotions = promotionService.getAllPromotions();

        // Assert
        assertEquals(expectedPromotions, responsePromotions);
        verify(promotionRepository, times(1)).findAll();
    }

    @Test
    void testGetAllPromotionsSorted_AllPromotions_ReturnAllPromotionsSorted() {

        // Arrange
        promotion.setClicks(4);
        Promotion promotion2 = new Promotion();
        promotion2.setId(2);
        promotion2.setClicks(3);
        promotion2.setDescription("It's a promotion");
        Promotion promotion3 = new Promotion();
        promotion3.setId(3);
        promotion3.setClicks(2);
        promotion3.setDescription("It's a promotion");
        Promotion promotion4 = new Promotion();
        promotion4.setId(4);
        promotion4.setClicks(1);
        promotion4.setDescription("It's a promotion");

        List<Promotion> expectedPromotions = new ArrayList<>();
        expectedPromotions.add(promotion);
        expectedPromotions.add(promotion2);
        expectedPromotions.add(promotion3);
        expectedPromotions.add(promotion4);

        when(promotionRepository.findAll())
            .thenReturn(expectedPromotions);

        // Act
        List<Promotion> responsePromotions = promotionService.getAllPromotionsSorted();

        // Assert
        assertEquals(expectedPromotions, responsePromotions);
        verify(promotionRepository, times(1)).findAll();
    }

    
}
