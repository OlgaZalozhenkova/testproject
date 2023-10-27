package com.example.testproject.repositories;

import com.example.testproject.models.GoodOperation;
import com.example.testproject.models.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface GoodOperationRepository extends JpaRepository<GoodOperation, Integer>, JpaSpecificationExecutor<GoodOperation> {
    @Query("select g from GoodOperation g where g.counterpartName = :counterpartName")
    List<GoodOperation> getGoodOperationsByCounterpartName(String counterpartName);

    @Query("select g from GoodOperation g where g.date >= :dateFrom and g.date <= :dateTo")
    List<GoodOperation> getGoodOperationsForPeriod(Date dateFrom, Date dateTo);

    @Query("select g from GoodOperation g where g.operationType = :operationType and g.counterpartName = :counterpartName")
    Optional<List<GoodOperation>>getGoodOperationsByOperationTypeAndCounterpartName(OperationType operationType, String counterpartName);

    @Query("select g from GoodOperation g where g.operationType = :operationType and " +
            "g.counterpartName = :counterpartName and g.date >= :dateFrom and g.date <= :dateTo order by current_date")
    List<GoodOperation> getOperationsByOperationTypeAndCounterpartNameAndPeriod(OperationType operationType, String counterpartName, Date dateFrom, Date dateTo);

    List<GoodOperation> findByCounterpartName(String counterpartName);

    @Query(value = "select good_operations.quantity_db from good_operations " +
            "where good_operations.item = :item and good_operations.date <= :date " +
            "order by date desc limit 1", nativeQuery = true)
    Optional<Double> getGoodOperationsByItemAndDate(String item, Date date);

    @Query("select g from GoodOperation g where g.operationType = 'SELLING'"
            + "and g.date >= :dateFrom and g.date <= :dateTo")
    List<GoodOperation> getSalesIncomeForPeriod(Date dateFrom, Date dateTo);

    @Query("select g from GoodOperation g where  g.item = :item and g.operationType = 'SELLING'"
            + "and g.date >= :dateFrom and g.date <= :dateTo")
    List<GoodOperation> getSalesIncomeGoodForPeriod(String item, Date dateFrom, Date dateTo);

    @Query("select g from GoodOperation g where g.counterpartName = :counterpartName and g.item = :item and g.operationType = 'SELLING' and g.date >= :dateFrom and g.date <= :dateTo order by g.date")
    List<GoodOperation> getSalesIncomeCounterpartNameGoodForPeriod(String counterpartName, String item, Date dateFrom, Date dateTo);
}