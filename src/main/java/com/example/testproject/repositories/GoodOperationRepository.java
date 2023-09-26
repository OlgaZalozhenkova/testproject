package com.example.testproject.repositories;

import com.example.testproject.models.GoodOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodOperationRepository extends JpaRepository<GoodOperation,Integer> {
}
