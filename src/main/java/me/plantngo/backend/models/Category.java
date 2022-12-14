package me.plantngo.backend.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    private String name;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "product_category")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Product> products;

    @ManyToOne
    @JoinColumn(name = "merchant_id")
    @JsonBackReference(value = "merchant_category")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Merchant merchant;
}
