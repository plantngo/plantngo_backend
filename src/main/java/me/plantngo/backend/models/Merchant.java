package me.plantngo.backend.models;

import java.util.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

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
@Table(name = "merchant")
public class Merchant {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Integer id;

        @Size(min = 5, max = 15, message = "Username must be between 5 and 15 characters long")
        @NotBlank(message = "Username cannot be blank")
        private String username;

        @Email(message = "Must be a valid email")
        @NotBlank(message = "Email cannot be blank")
        private String email;

        @NotBlank(message = "Password cannot be blank")
        private String password;

        @NotBlank(message = "Company cannot be blank")
        private String company;

        @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL)
        @JsonManagedReference // Serializes this side
        private List<Category> categories;

        /*
                for merchant, authority can only be MERCHANT
        */
        private final String AUTHORITY = "MERCHANT";

        @OneToMany(mappedBy = "merchant", orphanRemoval = true, cascade = CascadeType.ALL)
        private List<Voucher> vouchers;
}
