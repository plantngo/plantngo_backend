package me.plantngo.backend.DTO;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestProgressDTO {
    private Integer id;
    /*
     * type of action needed to be done
     * "order", "purchased-voucher", "login" (on separate days)
     */
    @NotBlank
    private String type;

    /*
     * number of times action of type must be done to complete
     */
    @NotNull
    private Integer countToComplete;

    @NotNull
    private Integer countCompleted;
    /*
     * point awarded upon completion
     */
    @NotNull
    private Integer points;

    /*
     * indicates how many days the quest should stay up from the moment of posting
     */
    private LocalDateTime endDateTime;

}
