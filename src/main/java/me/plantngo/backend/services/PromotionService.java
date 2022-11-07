package me.plantngo.backend.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    private final PromotionRepository promotionRepository;

    private final ProductService productService;

    @Autowired
    public PromotionService(PromotionRepository promotionRepository, ProductService productService) {
        this.promotionRepository = promotionRepository;
        this.productService = productService;
    }

    public Promotion getPromotionById(Integer id) {
        if (promotionRepository.findById(id).isEmpty()) {
            throw new PromotionNotFoundException("PromotionId " + id + " does not exist.");
        }
        return promotionRepository.findById(id).get();
    }

    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }
    
    public List<Promotion> getAllPromotionsSorted() {
        List<Promotion> promoList = promotionRepository.findAll();
        Collections.sort(promoList, new Comparator<Promotion>() {
            @Override
            public int compare(Promotion u1, Promotion u2) {
              return u2.getClicks().compareTo(u1.getClicks());
            }
          });
        return promoList;
    }

    public List<Promotion> getPromotionsByMerchant(Merchant merchant) {
        return promotionRepository.findByMerchant(merchant);
    }

    public Promotion addPromotion(PromotionDTO promotionDTO, Merchant merchant) {

        Promotion promotion = this.promotionMapToEntity(promotionDTO, merchant);
        promotion.setClicks(0);

        // promotion.setMerchantId(merchant.getId());
        promotionRepository.save(promotion);

        return promotion;
    }

    private List<Product> returnProductList(List<Integer> productIds) {
        List<Product> result = new ArrayList<>();
        for (Integer productId : productIds) {
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

    public void addClicksToPromotion(Integer promotionId){
        if(!promotionRepository.existsById(promotionId)){
            throw new NotExistException("Promotion ID: " + promotionId);
        }
        Promotion promotion = this.getPromotionById(promotionId);
        promotion.setClicks(promotion.getClicks() + 1); 
        promotionRepository.saveAndFlush(promotion);
    }

    private Promotion promotionMapToEntity(PromotionDTO promotionDTO, Merchant merchant) {
        ModelMapper mapper = new ModelMapper();
        Promotion promotion = mapper.map(promotionDTO, Promotion.class);
        promotion.setMerchant(merchant);

        return promotion;
    }

    public Promotion updatePromotion(PromotionDTO promotionDTO, Integer promotionId) {

        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new NotExistException("Promotion"));

        promotion.setDescription(promotionDTO.getDescription());
        promotion.setBannerUrl(promotionDTO.getBannerUrl());
        promotion.setStartDate(promotionDTO.getStartDate());
        promotion.setEndDate(promotionDTO.getEndDate());

        promotionRepository.save(promotion);

        return promotion;
    }

}
