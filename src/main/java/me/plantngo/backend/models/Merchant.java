package me.plantngo.backend.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
        @JsonIgnore
        private String password;

        @NotBlank(message = "Company cannot be blank")
        private String company;

        // @NotNull
        private String logoUrl;

        // @NotNull
        private String bannerUrl;

        // @NotNull
        private String address;

        // @NotNull
        private String description;

        // @NotNull
        private Double latitude;

        // @NotNull
        private Double longitude;

        // @NotNull
        private String cuisineType;

        // @NotNull
        private Integer priceRating;

        // @NotNull
        private String operatingHours;

        private Double carbonRating;

        @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL)
        @JsonManagedReference("merchant_category") // Serializes this side
        @EqualsAndHashCode.Exclude
        private List<Category> categories;

        @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL)
        @JsonManagedReference(value = "merchant_promotion")
        private List<Promotion> promotions;

        /*
         * for merchant, authority can only be MERCHANT
         */
        private final String AUTHORITY = "MERCHANT";

        @OneToMany(mappedBy = "merchant", orphanRemoval = true, cascade = CascadeType.ALL)
        @JsonIgnore
        private List<Voucher> vouchers;

        @OneToMany(mappedBy = "merchant", orphanRemoval = true, cascade = CascadeType.ALL)
        @JsonIgnore
        private List<Order> order;

        @JsonIgnore
        @Size(min = 16, max = 16)
        private String resetPasswordToken;
}
