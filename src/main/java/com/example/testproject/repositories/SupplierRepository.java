package com.example.testproject.repositories;

import com.example.testproject.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier,Integer> {

    @Query("select s from Supplier s where s.id = :id")
    Supplier getSupplierByIdQuery(int id);

    @Query("select s from Supplier s join s.goods good where good.name = :name")
    List<Supplier> getSuppliers(String name);

    @Query("select s from Supplier s join s.ratings rating where s.name =:name")
    Supplier findRatingBySupplierName(String name);

    Supplier findByName(String name);
}
