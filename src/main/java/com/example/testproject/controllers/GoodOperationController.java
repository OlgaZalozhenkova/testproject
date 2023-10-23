package com.example.testproject.controllers;

import com.example.testproject.dto.GoodOperationSpecificationDTO;
import com.example.testproject.models.GoodOperation;
import com.example.testproject.models.OperationType;
import com.example.testproject.servicies.GoodOperationService;
import com.example.testproject.servicies.GoodOperationSpecificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/goodoperation")
public class GoodOperationController {

    private final GoodOperationService goodOperationService;
    private final GoodOperationSpecificationService goodOperationSpecificationService;

    // аналитика по журналу операций
    // все операции
    @GetMapping("/all")
    public List<GoodOperation> getAllOperations() {
        return goodOperationService.findAll();
    }

    // журнал операций за указанный период
    @GetMapping("/period")
    public List<GoodOperation> getGoodOperationsForPeriod
    (@RequestParam("dateFrom") Date dateFrom, @RequestParam("dateTo") Date dateTo) {
        return goodOperationService.getGoodOperationsForPeriod(dateFrom, dateTo);
    }

    // журнал операций по наименованию контрагента
    @GetMapping("/counterpart")
    public List<GoodOperation> getGoodOperationsByCounterpartName(@RequestParam("counterpartName") String counterpartName) {
        return goodOperationService.getGoodOperationByCounterpartName(counterpartName);
    }

    // журнал операций по типу операции и наименованию контрагента
    @GetMapping("/type/counterpart")
    public List<GoodOperation> getGoodOperationsByOperationTypeAndCounterpartName(
            @RequestParam("operationType") OperationType operationType,
            @RequestParam("counterpartName") String counterpartName) {
        return goodOperationService.getGoodOperationsByOperationTypeAndCounterpartName
                (operationType, counterpartName).orElse(null);
    }

    // журнал операций по типу операции и наименованию контрагента за указанный период
    @GetMapping("/type/counterpart/period")
    public List<GoodOperation> geOperationsByOperationTypeAndCounterpartNameAndPeriod(
            @RequestParam("operationType") OperationType operationType,
            @RequestParam("counterpartName") String counterpartName,
            @RequestParam("dateFrom") Date dateFrom, @RequestParam("dateTo") Date dateTo) {
        return goodOperationService.getOperationsByOperationTypeAndCounterpartNameAndPeriod(operationType,
                counterpartName, dateFrom, dateTo);
    }

    // журнал операций фильтры
    @GetMapping("/filter")
    public List<GoodOperation> getOperationsByItemAndCounterpartAndOperationType
    (@RequestParam(value = "item", required = false) String item,
     @RequestParam(value = "counterpartName", required = false) String counterpartName,
     @RequestParam(value = "operationType", required = false) OperationType operationType) {
        GoodOperationSpecificationDTO goodDTO = new GoodOperationSpecificationDTO(item,
                counterpartName, operationType);
        return goodOperationSpecificationService
                .getOperationsByItemAndCounterpartAndOperationType(goodDTO);
    }
}
