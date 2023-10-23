package com.example.testproject.repositories;

import com.example.testproject.models.GoodCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoodCardRepository extends JpaRepository<GoodCard, Integer> {

    GoodCard findByName(String name);

}
