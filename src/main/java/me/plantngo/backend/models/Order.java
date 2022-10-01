package me.plantngo.backend.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.CascadeType;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private Integer customerId;

    @NotNull
    private Integer merchant_Id;

    private Date createdDate;

    private Double totalPrice;

    private String sessionId;

    // @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    // @JoinColumn(name = "order_id",referencedColumnName = "id",insertable = false,updatable = false)
    // private List<OrderItem> orderItems;

    // @ManyToOne(cascade = CascadeType.ALL)
    // @JoinColumn(name = "customer_id", referencedColumnName = "id", insertable = false, updatable = false)
    // private Customer customer;

    // @ManyToOne(cascade = CascadeType.ALL)
    // @JoinColumn(name = "merchant_id", referencedColumnName = "id", insertable = false, updatable = false)
    // private Merchant merchant;


    
}
