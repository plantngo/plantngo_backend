package me.plantngo.backend.models;

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "customer")
public class Customer {

        @Id
        @EqualsAndHashCode.Include
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Integer id;

        @Size(min = 5, max = 15, message = "Username must be between 5 and 15 characters long")
        @NotBlank(message = "Username cannot be null")
        @Column(name = "username")
        private String username;

        @Email(message = "Must be a valid email")
        @NotBlank(message = "Email cannot be null")
        @Column(name = "email")
        private String email;

        @NotBlank(message = "Password cannot be blank")
        @JsonIgnore
        @Column(name = "password")
        private String password;

        @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
        @JsonManagedReference(value = "customer_preference")
        private List<Preference> preferences;

        private Integer greenPoints = 0;

        @ManyToMany
        @JoinTable(name = "ownership", joinColumns = @JoinColumn(name = "customer_id"), inverseJoinColumns = @JoinColumn(name = "voucher_id"))
        @ToString.Exclude
        private Set<Voucher> ownedVouchers;

        @ManyToMany
        @JoinTable(name = "cart", joinColumns = @JoinColumn(name = "customer_id"), inverseJoinColumns = @JoinColumn(name = "voucher_id"))
        @ToString.Exclude
        private Set<Voucher> vouchersCart;

        @OneToMany(mappedBy = "customer", orphanRemoval = true, cascade = CascadeType.ALL)
        @JsonManagedReference(value = "customer_order")
        private List<Order> orders;

        /*
         * for customer, authority can either be CUSTOMER or ADMIN (for testing)
         */
        @NotNull(message = "AUTHORITY cannot be null, choose CUSTOMER or ADMIN")
        private final String AUTHORITY = "CUSTOMER";

        @JsonIgnore
        @Size(min = 16, max = 16)
        @Column(name = "resetPasswordToken")
        private String resetPasswordToken;

        @ManyToMany
        @ToString.Exclude
        @JoinTable(name = "quest_completion", joinColumns = @JoinColumn(name = "customer_id"), inverseJoinColumns = @JoinColumn(name = "quest_id"))
        private Set<Quest> completedQuests;

}
