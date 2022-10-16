package me.plantngo.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.plantngo.backend.DTO.APIPostDTO;
import me.plantngo.backend.DTO.APIResponseDTO;
import me.plantngo.backend.models.Ingredient;
import me.plantngo.backend.services.EmissionService;

@RestController
@RequestMapping(path = "api/v1/ingredient")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class IngredientController {

    private EmissionService emissionService;

    @Autowired
    public IngredientController(EmissionService emissionService) {
        this.emissionService = emissionService;
    }

    @GetMapping
    public List<Ingredient> getAllIngredients() {
        return emissionService.getAllIngredients();
    }

    @GetMapping(path="/generate")
    public void generateEmissions(@PathVariable("emission") String id) {
        emissionService.populateRepository();
    }
}
