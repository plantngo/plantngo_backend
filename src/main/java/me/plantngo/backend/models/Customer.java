package me.plantngo.backend.models;

import java.util.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
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
    @NotNull(message = "Username cannot be null")
    private String username;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Preference> preferences;

    @Email(message = "Must be a valid email")
    @NotBlank(message = "Email cannot be null")
    private String email;

    
    private String password;

    private Integer greenPts;

    /*
        for customer, authority can either be CUSTOMER or ADMIN (for testing)
     */
    @NotNull(message = "AUTHORITY cannot be null, choose CUSTOMER or ADMIN")
    private final String AUTHORITY = "CUSTOMER";
}
