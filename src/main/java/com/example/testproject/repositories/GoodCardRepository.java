package com.example.testproject.repositories;

import com.example.testproject.models.GoodCard;
import com.example.testproject.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodCardRepository extends JpaRepository<GoodCard, Integer> {
    GoodCard findByName(String name);

    @Query("select gc from GoodCard gc join gc.good good where good.name = :name")
    GoodCard findGoodCardByGoodId(String name);

////    как установить фиксированное значение
//    @Query("select gc from GoodCard gc join gc.good good " +
//            "join good.goodOperations goodOperation where " +
//            "goodOperation.supplierName = :supplierName " +
//            "and goodOperation.operationCurrent = :operationCurrent")
//    List<GoodCard> findGoodCard(String supplierName, String operationCurrent);

}
