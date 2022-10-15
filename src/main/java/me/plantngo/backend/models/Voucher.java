package me.plantngo.backend.models;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/*
each voucher is assumed to be applicable to all products issued by a merchant
 */
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
    private Integer value;

    /*
     * type can be P (percentage-discount) or F (flat-discount)
     */
    @NotNull
    private Character type;

    /*
     * with type P, discount can be between 0.00 and 1.00
     * with type F, discount can be any number greater than 0.00
     */
    @NotNull
    private Double discount;

    @NotNull(message = "description ")
    private String description;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    @JsonIgnore
    private Merchant merchant;

    @NotNull
    private Integer merchantId;

    @ManyToMany(mappedBy = "ownedVouchers")
    private List<Customer> customersThatOwn;

    @ManyToMany(mappedBy = "vouchersCart")
    private List<Customer> customersInCart;
}
