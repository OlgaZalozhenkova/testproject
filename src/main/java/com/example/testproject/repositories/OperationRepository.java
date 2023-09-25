package com.example.testproject.repositories;

import com.example.testproject.models.Operation;
import com.example.testproject.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation,Integer> {
    List<Operation> findByName(String name);
}
