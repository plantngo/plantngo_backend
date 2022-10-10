package me.plantngo.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/*
each voucher is assumed to be applicable to all products issued by a merchant
 */
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "voucher")
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull(message = "value cannot be null")
    private Double value;

    /*
    type can be P (percentage-discount) or F (flat-discount)
     */
    @NotNull
    private Character type;

    /*
    with type P, discount can be between 0.00 and 1.00
    with type F, discount can be any number greater than 0.00
     */
    @NotNull
    private Double discount;

    @NotNull(message = "description ")
    private String description;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @ManyToMany(mappedBy = "ownedVouchers")
    @JsonBackReference
    private List<Customer> customersThatOwn;

    @ManyToMany(mappedBy = "vouchersCart")
    @JsonBackReference
    private List<Customer> customersInCart;
}
