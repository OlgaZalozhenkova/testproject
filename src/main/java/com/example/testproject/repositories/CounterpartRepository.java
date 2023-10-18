package com.example.testproject.repositories;

import com.example.testproject.models.Counterpart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CounterpartRepository extends JpaRepository<Counterpart,Integer> {

    @Query("select c from Counterpart c join c.goods good where good.name = :name")
    List<Counterpart> findCounterpartsByGoodName(String name);

    Counterpart findByName(String name);

}
