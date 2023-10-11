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
    int id;

    @Column(name = "name")
    String name;

    @Column(name = "price")
    int price;

    @Column(name = "quantity")
    int quantity;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "good_supplier",
            joinColumns = @JoinColumn(name = "good_id"),
            inverseJoinColumns = @JoinColumn(name = "supplier_id"))
    private List<Counterpart> counterparts;

    @JsonIgnore
    @OneToMany(mappedBy = "good")
    private List<GoodOperation> goodOperations;

//    @JsonIgnore
//    @OneToOne(mappedBy = "good")
//    private GoodCard goodCard;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "good_card_id", referencedColumnName = "id")
    private GoodCard goodCard;

    @JsonIgnore
    @OneToMany(mappedBy = "good")
    List<Rating> ratings;

    public Good(String name, int price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}
