package com.example.testproject.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    String supplierName;

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
    private Supplier supplier;

    public GoodOperation(String item, String operationCurrent, int price, int quantity, String supplierName, Good good, Supplier supplier, Date date, int priceDB, int quantityDB) {
        this.item = item;
        this.operationCurrent = operationCurrent;
        this.price = price;
        this.quantity = quantity;
        this.supplierName = supplierName;
        this.good = good;
        this.supplier = supplier;
        this.date = date;
        this.priceDB = priceDB;
        this.quantityDB = quantityDB;
    }
}
