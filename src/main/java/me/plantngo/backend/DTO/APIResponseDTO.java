package me.plantngo.backend.DTO;

import java.util.List;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIResponseDTO {

    private Integer count;

    private String next;

    private String previous;

    private List<APIResultDTO> results;
}
