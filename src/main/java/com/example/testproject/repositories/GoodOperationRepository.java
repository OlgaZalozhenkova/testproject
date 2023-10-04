package com.example.testproject.repositories;

import com.example.testproject.models.GoodOperation;
import com.example.testproject.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface GoodOperationRepository extends JpaRepository<GoodOperation, Integer> {
    @Query("select g from GoodOperation g where g.supplierName = :supplierName")
    List<GoodOperation> getGoodOperationsBySupplierName(String supplierName);

    @Query("select g from GoodOperation g where g.operationCurrent = :supply and g.item = :item")
    List<GoodOperation> getSellQuantity(String supply, String item);

    @Query("select g from GoodOperation g where g.date >= :dateFrom and g.date <= :dateTo")
    List<GoodOperation> getGoodOperationsByDate(Date dateFrom, Date dateTo);

    @Query("select g from GoodOperation g where g.operationCurrent = :operationCurrent and g.supplierName = :supplierName")
    List<GoodOperation> getGoodOperationsByOperationAndSupplierName(String operationCurrent, String supplierName);

    @Query("select g from GoodOperation g where g.operationCurrent = :operationCurrent and g.supplierName = :supplierName and g.date >= :dateFrom and g.date <= :dateTo")
    List<GoodOperation> getOperationsByOperationAndSupplierNameAndDate(String operationCurrent, String supplierName, Date dateFrom, Date dateTo);

    @Query("select g from GoodOperation g where g.item = :item and g.date <= :date")
    List<GoodOperation> getGoodOperationsByItemAndDate(String item, Date date);
}
