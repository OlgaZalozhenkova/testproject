package com.example.testproject.repositories;

import com.example.testproject.models.Counterpart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CounterpartRepository extends JpaRepository<Counterpart,Integer> {

    @Query("select s from Counterpart s join s.goods good where good.name = :name")
    List<Counterpart> findCounterpartsByGoodName(String name);

    Counterpart findByName(String name);

//    @Query("select s from Supplier s join s.ratings rating where s.name =:name")
//    Supplier findRatingBySupplierName(String name);

//@Query("select s from Supplier s where s.id = :id")
//Supplier getSupplierByIdQuery(int id);
}
