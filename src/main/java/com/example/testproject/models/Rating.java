package com.example.testproject.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "rating")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Rating {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "value")
    double value;

    @Column(name = "good_name")
    String goodName;

    @Column(name = "is_changed")
    boolean isChanged;

    @Column(name = "is_deleted")
    boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "good_id", referencedColumnName = "id")
    Good good;

    @ManyToOne
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    private Supplier supplier;

    public Rating(double value, String goodName, Good good, Supplier supplier) {
        this.value = value;
        this.goodName = goodName;
        this.good = good;
        this.supplier = supplier;
    }

    //    @ManyToMany
//    @JoinTable(name = "rating_supplier",
//            joinColumns = @JoinColumn(name = "rating_id"),
//            inverseJoinColumns = @JoinColumn(name = "supplier_id"))
//   // @JoinColumn(name = "supplier_id", referencedColumnName = "id")
//    List<Supplier> suppliers;

//    public Rating(double value, String goodName, Good good, List<Supplier> suppliers) {
//        this.value = value;
//        this.goodName = goodName;
//        this.good = good;
//        this.suppliers = suppliers;
//    }
}
