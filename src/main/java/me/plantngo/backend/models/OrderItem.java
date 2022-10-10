package me.plantngo.backend.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "orderitems")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderItemId;

    @NotNull
    private Long productId;

    @NotNull
    private int quantity;

    @NotNull
    private double price;

    private Integer orderId;

    private Date createdDate;

    // @ManyToOne
    // @JoinColumn(name = "order_id",referencedColumnName = "id",insertable = false,updatable = false)
    // private Order order;

    //TODO: Create Product
    // @OneToOne
    // @JoinColumn(name = "productId",referencedColumnName = "id",insertable = false,updatable = false)
    // private Product product;

}