package me.plantngo.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.plantngo.backend.DTO.APIPostDTO;
import me.plantngo.backend.DTO.APIResponseDTO;
import me.plantngo.backend.models.Ingredient;
import me.plantngo.backend.models.ProductIngredient;
import me.plantngo.backend.services.EmissionService;

@RestController
@RequestMapping(path = "api/v1/ingredient")
@Api(value = "Ingredient Controller", description = "Operations pertaining to Ingredients in Merchant's products")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class IngredientController {

    private final EmissionService emissionService;

    @Autowired
    public IngredientController(EmissionService emissionService) {
        this.emissionService = emissionService;
    }

    @ApiOperation(value = "Get all Ingredients")
    @GetMapping
    public List<Ingredient> getAllIngredients() {
        return emissionService.getAllIngredients();
    }

    @ApiOperation(value = "Temporary endpoint to scrape data from myemissions.green and create + store Ingredient objects in local Repository")
    @GetMapping(path="/generate")
    public List<Ingredient> generateEmissions() {
        return emissionService.populateRepository();
    }

}
