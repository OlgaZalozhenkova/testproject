package com.example.testproject.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

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
@Builder
public class GoodCard {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "price_supply")
    int priceSupply;

    @Column(name = "price_selling")
    int priceSelling;

    @Column(name = "available_quantity")
    private int availableQuantity;

    @Column(name = "sell_quantity")
    private int sellQuantity;

    @Column(name ="rating")
    private double rating;

    @Column(name = "count_value")
    private double countValue;

//    @JsonIgnore
//    @OneToOne
//    @JoinColumn(name = "good_id", referencedColumnName = "id")
//    Good good;

    @JsonIgnore
    @OneToOne(mappedBy = "goodCard")
    Good good;

    public GoodCard(String name, int priceSupply, int priceSelling, int availableQuantity, int sellQuantity, double rating, double countValue) {
        this.name = name;
        this.priceSupply = priceSupply;
        this.priceSelling = priceSelling;
        this.availableQuantity = availableQuantity;
        this.sellQuantity = sellQuantity;
        this.rating = rating;
        this.countValue = countValue;
    }

    @Override
    public String toString() {
        return "GoodCard{" +
                "name='" + name + '\'' +
                ", priceSupply=" + priceSupply +
                ", priceSelling=" + priceSelling +
                '}';
    }
}
