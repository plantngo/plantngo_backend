package me.plantngo.backend.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIResultDTO {
    
    private String id;

    private String name;

    private String category;

    private String serving_weight;

    private String serving_desc;
}
