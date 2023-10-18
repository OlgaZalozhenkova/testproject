package com.example.testproject.repositories;

import com.example.testproject.models.Good;
import com.example.testproject.models.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GoodRepository extends JpaRepository<Good,Integer> {

    @Query("select g from Good g join g.goodOperations goodOperation " +
            "where goodOperation.operationType = :operationType and " +
            "goodOperation.counterpartName = :counterpartName and goodOperation.item= :item")
    Good getGoodForSetRating(OperationType operationType, String counterpartName, String item);

    Good findByName(String name);

    @Query("select g from Good g join g.counterparts counterpart where counterpart.name = :name")
    List<Good> getGoodsByCounterPartName(String name);

}
