package me.plantngo.backend.models;

import java.util.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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
@Table(name = "preference")
public class Preference {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String preference;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;


}
