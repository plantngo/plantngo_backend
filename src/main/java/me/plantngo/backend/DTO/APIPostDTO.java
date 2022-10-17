package me.plantngo.backend.DTO;

import java.util.List;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIPostDTO {

    private List<APIIngredientDTO> ingredients;

    private Integer servings;
}
