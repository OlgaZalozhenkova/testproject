package com.example.testproject.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "good_operations")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class GoodOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @Column(name = "item")
    String item;

    @Column(name = "operation_current")
    String operationCurrent;

    @Column(name = "operation_price")
    int price;

    @Column(name = "operation_quantity")
    int quantity;

    @Column(name = "supplier_name")
    String counterpartName;

    @Column(name = "date")
    Date date;

    @Column(name = "price_db")
    int priceDB;

    @Column(name = "quantity_db")
    int quantityDB;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "good_id", referencedColumnName = "id")
    private Good good;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    private Counterpart counterpart;

    public GoodOperation(String item, String operationCurrent, int price, int quantity, String counterpartName, Date date, int priceDB, int quantityDB, Good good, Counterpart counterpart) {
        this.item = item;
        this.operationCurrent = operationCurrent;
        this.price = price;
        this.quantity = quantity;
        this.counterpartName = counterpartName;
        this.date = date;
        this.priceDB = priceDB;
        this.quantityDB = quantityDB;
        this.good = good;
        this.counterpart = counterpart;
    }
}
