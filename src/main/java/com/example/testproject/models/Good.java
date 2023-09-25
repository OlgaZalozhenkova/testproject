package com.example.testproject.models;

import com.example.testproject.util.CustomListDeserializer;
import com.example.testproject.util.CustomListSerializer;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "good")
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

    @Column(name ="quantity")
    int quantity;

    //    @JsonIgnore
//    @JsonSerialize(using = CustomListSerializer.class)
//    @JsonDeserialize(using = CustomListDeserializer.class)
//    @JsonBackReference
    @ManyToMany
    @JoinTable(name = "good_supplier",
            joinColumns = @JoinColumn(name = "good_id"),
            inverseJoinColumns = @JoinColumn(name = "supplier_id"))
    private List<Supplier> suppliers;

    @ManyToMany
    @JoinTable(name = "good_operation",
            joinColumns = @JoinColumn(name = "good_id"),
            inverseJoinColumns = @JoinColumn(name = "operation_id"))
    private List<Operation> operations;

//    @ManyToOne
//    @JoinColumn(name = "supplier_id")
//    private Supplier supplier;

    public Good(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
