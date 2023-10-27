package com.example.testproject.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

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
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    private String category;

    @Column(name = "unit_of_measurement")
    private String unitOfMeasurement;

    @Column(name = "price_supply")
    private double priceSupply;

    @Column(name = "price_selling")
    private double priceSelling;

    @Column(name = "available_quantity")
    private double availableQuantity;

    @Column(name = "sell_quantity")
    private double sellQuantity;

    @Column(name ="rating")
    private double rating;

    @Column(name = "count_value")
    private int countValue;

    @JsonIgnore
    @OneToOne(mappedBy = "goodCard")
    private Good good;

    public GoodCard(String name, double priceSupply, double priceSelling, double availableQuantity, double sellQuantity, double rating, int countValue) {
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
