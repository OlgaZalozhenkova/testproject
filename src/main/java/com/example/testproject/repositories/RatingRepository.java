package com.example.testproject.repositories;

import com.example.testproject.models.Counterpart;
import com.example.testproject.models.Good;
import com.example.testproject.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {

    Rating findByCounterpartAndGood(Counterpart counterpart, Good good);

    @Query("select r from Rating r join r.counterpart counterpart where r.goodName =:goodName and counterpart.name =:counterpartName")
    Optional<Rating> findByGoodAndCounterpart(String goodName, String counterpartName);

}
