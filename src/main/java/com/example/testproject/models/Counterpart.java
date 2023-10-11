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

public class Counterpart {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "counterparts")
    @JsonManagedReference
    private List<Good> goods;

    @JsonIgnore
    @OneToMany(mappedBy = "counterpart")
    private List<GoodOperation> goodOperations;

    @JsonIgnore
    @OneToMany(mappedBy = "counterpart")
    private List<Rating> ratings;

    public Counterpart(String name) {
        this.name = name;
    }
}
