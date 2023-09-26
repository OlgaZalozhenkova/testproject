package com.example.testproject.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "good_operations")
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

    @Column(name="supplier_name")
    String supplierName;

    @ManyToOne
    @JoinColumn(name = "good_id", referencedColumnName = "id")
    private Good good;

    @ManyToOne
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "operation_current_id", referencedColumnName = "id")
    private Operation operation;

    public GoodOperation(String item, String operationCurrent, int price, int quantity, String supplierName, Good good, Supplier supplier, Operation operation) {
        this.item = item;
        this.operationCurrent = operationCurrent;
        this.price = price;
        this.quantity = quantity;
        this.supplierName = supplierName;
        this.good = good;
        this.supplier = supplier;
        this.operation = operation;
    }
}
