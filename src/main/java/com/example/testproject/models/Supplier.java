package com.example.testproject.models;

import com.fasterxml.jackson.annotation.*;
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
@Table(name = "supplier")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")

public class Supplier {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "suppliers")
    @JsonManagedReference
    private List<Good> goods;

  //  @JsonIgnore
    @OneToMany(mappedBy = "supplier")
    private List<GoodOperation> goodOperations;

    public Supplier(String name) {
        this.name = name;
    }
}
