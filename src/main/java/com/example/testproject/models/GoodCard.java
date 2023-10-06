package com.example.testproject.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "good_card")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class GoodCard {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "name")
    String name;

    @Column(name = "price_supply")
    int priceSupply;

    @Column(name = "price_selling")
    int priceSelling;

    @Column(name = "available_quantity")
    int availableQuantity;

    @Column(name = "sell_quantity")
    int sellQuantity;

    @Column(name ="rating")
    double rating;

//    @JsonIgnore
//    @OneToOne
//    @JoinColumn(name = "good_id", referencedColumnName = "id")
//    Good good;

    @JsonIgnore
    @OneToOne(mappedBy = "goodCard")
    Good good;

    public GoodCard(int id, String name, int priceSupply, int priceSelling, int availableQuantity, int sellQuantity) {
        this.id = id;
        this.name = name;
        this.priceSupply = priceSupply;
        this.priceSelling = priceSelling;
        this.availableQuantity = availableQuantity;
        this.sellQuantity = sellQuantity;
    }

    public GoodCard(String name, int priceSupply, int priceSelling, int availableQuantity, int sellQuantity, Good good) {
        this.name = name;
        this.priceSupply = priceSupply;
        this.priceSelling = priceSelling;
        this.availableQuantity = availableQuantity;
        this.sellQuantity = sellQuantity;
        this.good = good;
    }
}
