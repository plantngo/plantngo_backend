package me.plantngo.backend.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.plantngo.backend.DTO.PromotionDTO;
import me.plantngo.backend.exceptions.NotExistException;
import me.plantngo.backend.exceptions.PromotionNotFoundException;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.models.Product;
import me.plantngo.backend.models.Promotion;
import me.plantngo.backend.repositories.PromotionRepository;

@Service
public class PromotionService {
    private PromotionRepository promotionRepository;
    private final ProductService productService;

    @Autowired
    public PromotionService(PromotionRepository promotionRepository, ProductService productService) {
        this.promotionRepository = promotionRepository;
        this.productService = productService;
    }

    public Promotion getPromotionById(Integer id) {
        if (!promotionRepository.findById(id).isEmpty()) {
            throw new PromotionNotFoundException("PromotionId " + id + " does not exist.");
        }
        return promotionRepository.findById(id).get();  
    }

    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }

    public List<Promotion> getPromotionsByMerchant(Merchant merchant) {
        return promotionRepository.findByMerchant(merchant);
    }

    public Promotion addPromotion(PromotionDTO promotionDTO, Merchant merchant) {

        Promotion promotion = this.promotionMapToEntity(promotionDTO, merchant);
        
        // promotion.setMerchantId(merchant.getId());
        promotionRepository.save(promotion);

        return promotion;
    }

    private List<Product> returnProductList(List<Integer> productIds){
        List<Product> result = new ArrayList<>();
        for(Integer productId : productIds){
            result.add(productService.getProductById(productId));
        }
        return result;
    }

    public void deletePromotion(Integer promotionId) {
        if (!promotionRepository.existsById(promotionId)) {
            throw new NotExistException("Promotion");
        }
        promotionRepository.deleteById(promotionId);
    }

    private Promotion promotionMapToEntity(PromotionDTO promotionDTO, Merchant merchant) {
        ModelMapper mapper = new ModelMapper();
        // Converter<String, LocalDate> toDate = new AbstractConverter<String,
        // LocalDate>() {
        // protected LocalDate convert(String source) {
        // return LocalDate.parse(source);
        // }
        // };
        // mapper.addConverter(toDate);
        Promotion promotion = mapper.map(promotionDTO, Promotion.class);
        promotion.setMerchant(merchant);

        return promotion;
    }

    public Promotion updatePromotion(PromotionDTO promotionDTO, Integer promotionId) {
       
        Promotion promotion = promotionRepository.findById(promotionId).orElseThrow(() -> new NotExistException("Promotion"));
    
        promotion.setDescription(promotionDTO.getDescription());
        promotion.setBannerUrl(promotionDTO.getBannerUrl());
        promotion.setPercentageDiscount(promotionDTO.getPercentageDiscount());
        promotion.setStartDate(promotionDTO.getStartDate());
        promotion.setEndDate(promotionDTO.getEndDate());
        promotion.setPromoProducts(returnProductList(promotionDTO.getProductIds()));

        promotionRepository.save(promotion);
        
        return promotion;
    }

}
