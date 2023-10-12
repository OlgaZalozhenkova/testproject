package com.example.testproject.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Counterpart counterpart;

    public Rating(double value, String goodName, Good good, Counterpart counterpart) {
        this.value = value;
        this.goodName = goodName;
        this.good = good;
        this.counterpart = counterpart;
    }
}
