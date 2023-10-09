package com.example.testproject.repositories;

import com.example.testproject.models.Good;
import com.example.testproject.models.Rating;
import com.example.testproject.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository1 extends JpaRepository<Rating, Integer> {

    Rating findBySupplierAndGood(Supplier supplier, Good good);

    @Query("select r from Rating r join r.supplier supplier where r.goodName =:goodName and supplier.name =:supplierName")
    Optional<Rating> findByGoodAndSupplier(String goodName, String supplierName);

//    @Query("select r from Rating r join r.suppliers supplier where r.goodName =:goodName and supplier.name =:supplierName")
//    Rating findByGoodAndSupplier1(String goodName, String supplierName);

//List<Rating> findByGoodName(String goodName);

}
