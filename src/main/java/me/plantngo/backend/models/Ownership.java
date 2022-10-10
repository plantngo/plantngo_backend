package me.plantngo.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import javax.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "ownership")
public class Ownership {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Code;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonBackReference
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "voucher_id", nullable = false)
    @JsonBackReference
    private Voucher voucher;
}
