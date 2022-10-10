package me.plantngo.backend.DTO;

import javax.validation.constraints.NotNull;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCategoryDTO {
    @NotNull
    private String name;
}
