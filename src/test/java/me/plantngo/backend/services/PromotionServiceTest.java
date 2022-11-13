package me.plantngo.backend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import me.plantngo.backend.DTO.PromotionDTO;
import me.plantngo.backend.exceptions.NotExistException;
import me.plantngo.backend.exceptions.PromotionNotFoundException;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.models.Promotion;
import me.plantngo.backend.repositories.PromotionRepository;

@ExtendWith(MockitoExtension.class)
public class PromotionServiceTest {
    
    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private ProductService productService;

    @Mock
    private MinioService minioService;

    @InjectMocks
    private PromotionService promotionService;

    private Promotion promotion;

    private MultipartFile file;

    @BeforeEach
    void initEach() {
        
        file = mock(MultipartFile.class);

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

    @Test
    void testGetPromotionsByMerchant_ValidMerchant_ReturnAllMerchants() {
        
        // Arrange
        String merchantName = "Daniel";

        Merchant merchant = new Merchant();
        merchant.setUsername(merchantName);

        promotion.setMerchant(merchant);

        Promotion promotion2 = new Promotion();
        promotion2.setId(2);
        promotion2.setDescription("It's a promotion");
        promotion2.setMerchant(merchant);

        List<Promotion> expectedPromotions = new ArrayList<>();
        expectedPromotions.add(promotion);
        expectedPromotions.add(promotion2);

        when(promotionRepository.findByMerchant(any(Merchant.class)))
            .thenReturn(expectedPromotions);

        // Act
        List<Promotion> responsePromotions = promotionService.getPromotionsByMerchant(merchant);

        // Assert
        assertEquals(expectedPromotions, responsePromotions);
        verify(promotionRepository, times(1)).findByMerchant(merchant);
    }

    @Test
    void testAddPromotion_ValidPromotionDTO_ReturnPromotion() throws MalformedURLException {

        // Arrange
        PromotionDTO promotionDTO = new PromotionDTO("It's a promotion!", new URL("https://www.google.com.sg"), null, null);

        Merchant merchant = new Merchant();
        merchant.setUsername("Daniel");

        Promotion expectedPromotion = new Promotion();
        expectedPromotion.setDescription("It's a promotion!");
        expectedPromotion.setBannerUrl(new URL("https://www.google.com.sg"));
        expectedPromotion.setMerchant(merchant);
        expectedPromotion.setClicks(0);

        when(promotionRepository.save(any(Promotion.class)))
            .thenReturn(expectedPromotion);

        // Act
        Promotion responsePromotion = promotionService.addPromotion(promotionDTO, merchant);

        // Assert
        assertEquals(expectedPromotion, responsePromotion);
        verify(promotionRepository, times(1)).save(expectedPromotion);
    }

    @Test
    void testAddPromotionWithImage_ValidPromotionDTO_ReturnPromotion() throws Exception {

        // Arrange
        PromotionDTO promotionDTO = new PromotionDTO("It's a promotion!", null, null, null);
        String imageUrl = "https://www.google.com.sg";

        Merchant merchant = new Merchant();
        merchant.setUsername("Daniel");

        Promotion expectedPromotion = new Promotion();
        expectedPromotion.setDescription("It's a promotion!");
        expectedPromotion.setBannerUrl(new URL("https://www.google.com.sg"));
        expectedPromotion.setMerchant(merchant);
        expectedPromotion.setClicks(0);

        when(minioService.uploadFile(any(MultipartFile.class), anyString(), anyString()))
            .thenReturn(imageUrl);
        
        // Act
        Promotion responsePromotion = promotionService.addPromotion(promotionDTO, merchant, file);

        // Assert
        assertEquals(expectedPromotion, responsePromotion);
        verify(minioService, times(1)).uploadFile(file, "promotion", merchant.getUsername());
        verify(promotionRepository, times(1)).save(responsePromotion);
    }

    @Test
    void testUpdatePromotionWithImage_ValidPromotionDTO_ReturnPromotion() throws Exception {

        // Arrange
        Integer promotionId = 1;
        String imageUrl = "https://yahoo.com.sg";

        Merchant merchant = new Merchant();
        merchant.setUsername("Jacky");

        PromotionDTO promotionDTO = new PromotionDTO("Hello", null, null, null);
        Promotion expectedPromotion = new Promotion();
        expectedPromotion.setId(1);
        expectedPromotion.setMerchant(merchant);
        expectedPromotion.setDescription("Hello");
        expectedPromotion.setBannerUrl(new URL("https://yahoo.com.sg"));

        promotion.setMerchant(merchant);

        when(promotionRepository.findById(any(Integer.class)))
            .thenReturn(Optional.of(promotion));
        when(minioService.uploadFile(any(MultipartFile.class), anyString(), anyString()))
            .thenReturn(imageUrl);

        // Act
        Promotion responsePromotion = promotionService.updatePromotion(promotionDTO, promotionId, file);

        // Assert
        assertEquals(expectedPromotion, responsePromotion);
        verify(minioService, times(1)).uploadFile(file, "promotion", merchant.getUsername());
        verify(promotionRepository, times(1)).save(responsePromotion);

    }

    @Test
    void testDeletePromotion_PromotionExists_ReturnSuccess() {

        // Arrange
        Integer promotionId = 1;
        Promotion expectedPromotion = promotion;
        
        when(promotionRepository.existsById(any(Integer.class)))
            .thenReturn(true);

        // Act
        promotionService.deletePromotion(promotionId);

        // Assert
        verify(promotionRepository, times(1)).existsById(promotionId);
        verify(promotionRepository, times(1)).deleteById(promotionId);

    }

    @Test
    void testDeletePromotion_PromotionNotExist_ThrowNotExistException() {

        // Arrange
        Integer promotionId = 1;
        String exceptionMsg = "";

        when(promotionRepository.existsById(any(Integer.class)))
            .thenReturn(false);

        // Act
        try {
            promotionService.deletePromotion(promotionId);
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Promotion doesn't exist!", exceptionMsg);
        verify(promotionRepository, times(1)).existsById(promotionId);
    }

    @Test
    void testAddClicksToPromotion_PromotionExists_ReturnSuccess() {

        // Arrange
        Integer promotionId = 1;
        promotion.setClicks(0);
        Promotion expectedPromotion = new Promotion();
        expectedPromotion.setId(1);
        expectedPromotion.setDescription("It's a promotion");
        expectedPromotion.setClicks(1);

        when(promotionRepository.existsById(any(Integer.class)))
            .thenReturn(true);
        when(promotionRepository.findById(any(Integer.class)))
            .thenReturn(Optional.of(promotion));
        when(promotionRepository.saveAndFlush(any(Promotion.class)))
            .thenReturn(expectedPromotion);
        
        // Act
        promotionService.addClicksToPromotion(promotionId);

        // Assert
        assertEquals(1, promotion.getClicks());
        verify(promotionRepository, times(1)).existsById(promotionId);
        verify(promotionRepository, times(2)).findById(promotionId);
        verify(promotionRepository, times(1)).saveAndFlush(expectedPromotion);
    }

    @Test
    void testAddClicksToPromotion_PromotionNotExist_ThrowNotExistException() {

        // Arrange
        Integer promotionId = 1;
        String exceptionMsg = "";

        when(promotionRepository.existsById(any(Integer.class)))
            .thenReturn(false);

        // Act
        try {
            promotionService.addClicksToPromotion(promotionId);
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Promotion ID: 1 doesn't exist!", exceptionMsg);
        verify(promotionRepository, times(1)).existsById(promotionId);
    }

    @Test
    void testUpdatePromotion_ValidPromotionDTO_ReturnPromotion() throws MalformedURLException {

        // Arrange
        Integer promotionId = 1;
        PromotionDTO promotionDTO = new PromotionDTO("Hello", new URL("https://yahoo.com.sg"), null, null);
        Promotion expectedPromotion = new Promotion();
        expectedPromotion.setId(1);
        expectedPromotion.setDescription("Hello");
        expectedPromotion.setBannerUrl(new URL("https://yahoo.com.sg"));

        when(promotionRepository.findById(any(Integer.class)))
            .thenReturn(Optional.of(promotion));
        when(promotionRepository.save(any(Promotion.class)))
            .thenReturn(expectedPromotion);

        // Act
        Promotion responsePromotion = promotionService.updatePromotion(promotionDTO, promotionId);

        // Assert
        assertEquals(expectedPromotion, responsePromotion);
        verify(promotionRepository, times(1)).findById(promotionId);
        verify(promotionRepository, times(1)).save(expectedPromotion);
    }

    @Test
    void testUpdatePromotion_PromotionNotExist_ThrowNotExistException() throws MalformedURLException {

        // Arrange
        Integer promotionId = 1;
        PromotionDTO promotionDTO = new PromotionDTO("Hello", new URL("https://yahoo.com.sg"), null, null);
        String exceptionMsg = "";

        when(promotionRepository.findById(any(Integer.class)))
            .thenReturn(Optional.empty());

        // Act
        try {
            Promotion responsePromotion = promotionService.updatePromotion(promotionDTO, promotionId);
        } catch (NotExistException e) {
            exceptionMsg = e.getMessage();
        }

        // Assert
        assertEquals("Promotion doesn't exist!", exceptionMsg);
        verify(promotionRepository, times(1)).findById(promotionId);
    }
}
