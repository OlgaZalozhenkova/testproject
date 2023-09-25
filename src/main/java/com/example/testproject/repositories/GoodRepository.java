package com.example.testproject.repositories;

import com.example.testproject.models.Good;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodRepository extends JpaRepository<Good,Integer> {
    @Query("select g from Good g where g.id = :id")
    Good getGoodByIdQuery(int id);

    @Query("select g from Good g join g.suppliers supplier where supplier.id = :id")
    List<Good> getGoodsBySupplierId(int id);

    Good findByName(String name);


}
