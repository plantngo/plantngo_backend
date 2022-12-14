package me.plantngo.backend.controllers;

import java.net.MalformedURLException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.plantngo.backend.DTO.PromotionDTO;
import me.plantngo.backend.models.Merchant;
import me.plantngo.backend.models.Promotion;
import me.plantngo.backend.services.MerchantService;
import me.plantngo.backend.services.PromotionService;

@RestController()
@RequestMapping(path = "api/v1/promotion")
@Api(value = "Promotion Controller", description = "Operations pertaining to Merchant promotions")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PromotionController {

    private final PromotionService promotionService;
    private final MerchantService merchantService;

    @Autowired
    public PromotionController(PromotionService promotionService, MerchantService merchantService) {
        this.promotionService = promotionService;
        this.merchantService = merchantService;
    }

    @ApiOperation(value = "Get all existing Promotions")
    @GetMapping
    public List<Promotion> getAllPromotions() {
        return promotionService.getAllPromotions();
    }

    @ApiOperation(value = "Get all existing Promotions Sorted")
    @GetMapping(path = "/allPromoSorted")
    public List<Promotion> getAllPromotionsSorted() {
        return promotionService.getAllPromotionsSorted();
    }

    @ApiOperation(value = "Adding clicks")
    @PutMapping(path = "/addClick/{promotionId}")
    public ResponseEntity<String> getPromotionbyId(@PathVariable("promotionId") Integer id) {
        promotionService.addClicksToPromotion(id);
        return new ResponseEntity<>("Promotion clicked", HttpStatus.OK);
    }

    @ApiOperation(value = "Create a Promotion")
    @PostMapping(path = "/{merchantName}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Promotion> addPromotion(@Valid @RequestPart("promotion") PromotionDTO promotionDTO,
            @PathVariable("merchantName") String merchantName, @RequestPart("image") MultipartFile file)
            throws MalformedURLException {
        Merchant merchant = merchantService.getMerchantByUsername(merchantName);
        Promotion promotion = promotionService.addPromotion(promotionDTO, merchant, file);
        return new ResponseEntity<>(promotion, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get Merchant Promotions")
    @GetMapping(path = "/{merchantName}")
    public List<Promotion> getAllPromotions(@PathVariable("merchantName") String merchantName) {
        return promotionService.getPromotionsByMerchant(merchantService.getMerchantByUsername(merchantName));
    }

    @ApiOperation(value = "Delete a Merchant's Promotion given its Id")
    @DeleteMapping(path = "/{promotionId}")
    public ResponseEntity<String> deletePromotion(@PathVariable("promotionId") Integer promotionId) {
        promotionService.deletePromotion(promotionId);
        return new ResponseEntity<>("Promotion deleted", HttpStatus.OK);
    }

    @ApiOperation(value = "Update an existing Promotion")
    @PutMapping(path = "/{promotionId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updatePromotion(@Valid @RequestPart("promotion") PromotionDTO promotionDTO,
            @PathVariable("promotionId") Integer promotionId, @RequestPart("image") MultipartFile file) throws MalformedURLException {
        promotionService.updatePromotion(promotionDTO, promotionId, file);
        return new ResponseEntity<>("Promotion updated", HttpStatus.CREATED);
    }

}
