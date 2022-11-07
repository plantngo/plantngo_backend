package me.plantngo.backend.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "quest")
public class Quest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    // @NotNull
    private LocalDateTime postedDateTime;

    /*
     * type of action needed to be done
     * "order", "purchase-voucher", "login" (on separate days)
     */
    @NotBlank
    private String type;

    /*
     * number of times action of type must be done to complete
     */
    @NotNull
    private Integer countToComplete;

    /*
     * point awarded upon completion
     */
    @NotNull
    private Integer points;

    // @NotNull
    private LocalDateTime endDateTime;

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(mappedBy = "completedQuests")
    private Set<Customer> customersThatHaveCompleted;
}
