package com.example.testproject.repositories;

import com.example.testproject.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier,Integer> {
    @Query("select s from Supplier s where s.id = :id")
    Supplier getSupplierByIdQuery(int id);
    Supplier findByName(String name);
}
