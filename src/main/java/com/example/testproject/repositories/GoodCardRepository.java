package com.example.testproject.repositories;

import com.example.testproject.models.GoodCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodCardRepository extends JpaRepository<GoodCard, Integer> {

    GoodCard findByName(String name);

}
