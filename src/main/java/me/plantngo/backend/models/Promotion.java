package me.plantngo.backend.models;

import java.net.URL;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.*;

/**
 * Promotion class
 * 
 * Merchants can create promotions for their stores across selected products
 * 
 */

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "promotion")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    private String description;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    @JsonBackReference(value = "merchant_promotion")
    private Merchant merchant;

    @NotBlank
    private String startDate;

    @NotBlank
    private String endDate;

    // @NotBlank
    private URL bannerUrl;

    private Integer clicks;
}
