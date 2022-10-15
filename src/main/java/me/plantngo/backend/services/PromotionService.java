package me.plantngo.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.plantngo.backend.exceptions.PromotionNotFoundException;
import me.plantngo.backend.models.Promotion;
import me.plantngo.backend.repositories.PromotionRepository;

@Service
public class PromotionService {
    private PromotionRepository promotionRepository;

    @Autowired
    public PromotionService(PromotionRepository promotionRepository){
        this.promotionRepository = promotionRepository;
    }

    public Promotion getPromotionById(Integer id){
        if (!promotionRepository.findById(id).isEmpty()) {
            throw new PromotionNotFoundException("Id " + id + " does not exist.");
        }
        return promotionRepository.findById(id).get();
    }

    public Promotion getPromotionByPromocode(String promocode){
        if (!promotionRepository.existsByPromocode(promocode)){
            throw new PromotionNotFoundException("Promocode " + promocode + " does not exist.");
        }
        return promotionRepository.findByPromocode(promocode).get();
    }

    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }

    
}
