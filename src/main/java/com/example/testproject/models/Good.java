package com.example.testproject.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "good")
@Builder
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")

public class Good {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;

    @Column(name = "quantity")
    private int quantity;

    @Column(name ="sales_income")
    int salesIncome;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "good_supplier",
            joinColumns = @JoinColumn(name = "good_id"),
            inverseJoinColumns = @JoinColumn(name = "supplier_id"))
    private List<Counterpart> counterparts;

    @JsonIgnore
    @OneToMany(mappedBy = "good")
    private List<GoodOperation> goodOperations;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "good_card_id", referencedColumnName = "id")
    private GoodCard goodCard;

    @JsonIgnore
    @OneToMany(mappedBy = "good")
    private List<Rating> ratings;

    public Good(String name, int price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}
