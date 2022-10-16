package me.plantngo.backend.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIIngredientDTO {
    private String food;

    private String unit;

    private String amount;
}
