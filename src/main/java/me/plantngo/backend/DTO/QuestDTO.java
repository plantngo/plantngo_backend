package me.plantngo.backend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class QuestDTO {
    /*
    type of action needed to be done
    "order", "purchased-voucher", "login" (on separate days)
     */
    @NotBlank
    private String type;

    /*
    number of times action of type must be done to complete
     */
    @NotNull
    private Integer countToComplete;

    /*
    point awarded upon completion
     */
    @NotNull
    private Integer points;

    /*
    indicates how many days the quest should stay up from the moment of posting
     */
    @NotNull
    private Integer persistForHowManyDays;
}
