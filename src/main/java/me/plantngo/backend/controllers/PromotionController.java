package me.plantngo.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.plantngo.backend.models.Promotion;
import me.plantngo.backend.services.PromotionService;

//TODO: implement PutMapping for creation and deletion of vouchers

@RestController()
@RequestMapping(path = "api/v1/promotion")
@Api(value = "Promotion Controller", description = "Operations pertaining to Merchant promotions")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PromotionController {

    private final PromotionService promotionService;

    @Autowired
    public PromotionController(PromotionService promotionService){
        this.promotionService = promotionService;
    }

    @ApiOperation(value = "Get all existing Promotions")
    @GetMapping
    public List<Promotion> getAllPromotions(){
        return promotionService.getAllPromotions();
    }

    @ApiOperation(value = "Get a Promotion given its promocode")
    @GetMapping(value="/{promocode}")
    public Promotion getPromotion(@PathVariable("promocode") String promocode) {
        return promotionService.getPromotionByPromocode(promocode);
    }
    

    // @GetMapping(path = "/{customerName}")
    // public List<Promotion> getPromotionsByMerchant()
    

}
