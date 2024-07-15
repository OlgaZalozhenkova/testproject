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
    private long id;

    @Column(name = "item")
    private String item;

    @Column(name = "operation")
    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    @Column(name = "operation_price")
    private double price;

    @Column(name = "operation_quantity")
    private double quantity;

    @Column(name = "supplier_name")
    private String counterpartName;

    @Column(name = "odate")
    private Date date;

    @Column(name = "price_db")
    private double priceDB;

    @Column(name = "quantity_db")
    private double quantityDB;

    @Column(name = "sales_income")
    private double salesIncome;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "good_id", referencedColumnName = "id")
    private Good good;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    private Counterpart counterpart;

    public GoodOperation(String item, OperationType operationType, double price, double quantity, String counterpartName, Date date, double priceDB, double quantityDB, Good good, Counterpart counterpart) {
        this.item = item;
        this.operationType = operationType;
        this.price = price;
        this.quantity = quantity;
        this.counterpartName = counterpartName;
        this.date = date;
        this.priceDB = priceDB;
        this.quantityDB = quantityDB;
        this.good = good;
        this.counterpart = counterpart;
    }

    public GoodOperation(String item, OperationType operationType, double price, double quantity, String counterpartName, Date date, double priceDB, double quantityDB, double salesIncome, Good good, Counterpart counterpart) {
        this.item = item;
        this.operationType = operationType;
        this.price = price;
        this.quantity = quantity;
        this.counterpartName = counterpartName;
        this.date = date;
        this.priceDB = priceDB;
        this.quantityDB = quantityDB;
        this.salesIncome = salesIncome;
        this.good = good;
        this.counterpart = counterpart;
    }
}
