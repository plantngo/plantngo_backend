package me.plantngo.backend.models;

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Size(min = 5, max = 15, message = "Username must be between 5 and 15 characters long")
    @NotBlank(message = "Username cannot be null")
    private String username;
    
    @Email(message = "Must be a valid email")
    @NotBlank(message = "Email cannot be null")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @JsonIgnore
    private String password;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Preference> preferences;

    private Integer greenPts = 0;

    @ManyToMany
    @JoinTable(name="ownership",
            joinColumns= @JoinColumn(name="customer_id"),
            inverseJoinColumns=
            @JoinColumn(name="voucher_id"))
    @JsonManagedReference
    private Set<Voucher> ownedVouchers;

    @ManyToMany
    @JoinTable(name="cart",
            joinColumns= @JoinColumn(name="customer_id"),
            inverseJoinColumns=
            @JoinColumn(name="voucher_id"))
    @JsonManagedReference
    private Set<Voucher> vouchersCart;



    @OneToMany(mappedBy = "customer", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Order> orders;

    /*
        for customer, authority can either be CUSTOMER or ADMIN (for testing)
     */
    @NotNull(message = "AUTHORITY cannot be null, choose CUSTOMER or ADMIN")
    private final String AUTHORITY = "CUSTOMER";
}
