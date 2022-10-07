package me.plantngo.backend.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
    private double value;
}
