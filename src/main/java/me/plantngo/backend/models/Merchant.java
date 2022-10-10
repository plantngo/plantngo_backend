package me.plantngo.backend.models;

import java.security.Principal;
import java.util.*;
import javax.persistence.Column;
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

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;

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
        @NotBlank(message = "Username cannot be null")
        private String username;

        @Email(message = "Must be a valid email")
        @NotBlank(message = "Email cannot be null")
        private String email;

        @NotBlank(message = "Password cannot be blank")
        private String password;

        @NotBlank(message = "Company cannot be null")
        private String company;

        @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL)
        @JsonManagedReference // Serializes this side
        private List<Category> categories;

        private final String AUTHORITY = "MERCHANT";
}
