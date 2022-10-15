package me.plantngo.backend.controllers;

import java.util.List;

import org.aspectj.apache.bcel.classfile.Module.Require;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.plantngo.backend.models.Promotion;
import me.plantngo.backend.services.PromotionService;


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

    // @GetMapping(path = "/{customerName}")
    // public List<Promotion> getPromotionsByMerchant()
    

}
