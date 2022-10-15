package me.plantngo.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.plantngo.backend.models.Promotion;
import me.plantngo.backend.services.PromotionService;

//TODO: implement PutMapping for creation and deletion of vouchers

@RestController()
@RequestMapping(path = "api/v1/promotion")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PromotionController {

    private final PromotionService promotionService;

    @Autowired
    public PromotionController(PromotionService promotionService){
        this.promotionService = promotionService;
    }

    @GetMapping
    public List<Promotion> getAllPromotions(){
        return promotionService.getAllPromotions();
    }

    @GetMapping(value="/{promocode}")
    public Promotion getPromotion(@PathVariable("promocode") String promocode) {
        return promotionService.getPromotionByPromocode(promocode);
    }
    

    // @GetMapping(path = "/{customerName}")
    // public List<Promotion> getPromotionsByMerchant()
    

}
