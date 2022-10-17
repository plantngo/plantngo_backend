package me.plantngo.backend.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.plantngo.backend.DTO.APIIngredientDTO;
import me.plantngo.backend.DTO.APIPostDTO;
import me.plantngo.backend.DTO.APIResponseDTO;
import me.plantngo.backend.DTO.APIResultDTO;
import me.plantngo.backend.models.Ingredient;
import me.plantngo.backend.repositories.IngredientRepository;

@Service
public class EmissionService {

    private IngredientRepository ingredientRepository;

    @Autowired
    public EmissionService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> getAllIngredients() {
        populateRepository();
        return ingredientRepository.findAll();
    }

    private void populateRepository() {
        ingredientRepository.deleteAll();
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://app.myemissions.green/api/v1/calculator/foods/?limit=1000";

        APIResponseDTO apiResponseDTO = restTemplate.getForObject(url, APIResponseDTO.class);

        List<APIResultDTO> results = apiResponseDTO.getResults();

        for (APIResultDTO r : results) {
            Ingredient ingredient = new Ingredient();
            ingredient.setIngredientId(r.getId());
            ingredient.setCategory(r.getCategory());
            ingredient.setName(r.getName());
            // ingredient.setEmissionPerGram(this.calculateEmissions(r.getId()));
            ingredientRepository.save(ingredient);
        }
    }

    public Double calculateEmissions(String ingredientId) {

        RestTemplate restTemplate = new RestTemplate();

        String url = "https://app.myemissions.green/api/v1/calculator/";

        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        APIIngredientDTO apiIngredientDTO = new APIIngredientDTO(ingredientId, "17b6249c-cbda-4e59-b575-018f7781c68c", "1");
        List<APIIngredientDTO> apiIngredientDTOs = new ArrayList<>();
        apiIngredientDTOs.add(apiIngredientDTO);
        APIPostDTO apiPostDTO = new APIPostDTO(apiIngredientDTOs, 1);

        HttpEntity<APIPostDTO> entity = new HttpEntity<>(apiPostDTO, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        String jsonBody = null;

        if (response.getStatusCodeValue() == 201) {
            jsonBody = response.getBody();
        } else {
            return null;
        }

        JsonArray objectArr = JsonParser.parseString(jsonBody).getAsJsonObject().get("ingredients").getAsJsonArray();
        JsonObject object = objectArr.get(0).getAsJsonObject();

        Double emissions = object.get("emissions").getAsDouble();

        return emissions;
    }
}
