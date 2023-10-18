package com.example.testproject.controllers;

import com.example.testproject.dto.GoodDTO;
import com.example.testproject.dto.GoodOperationDTO;
import com.example.testproject.dto.GoodOperationSpecificationDTO;
import com.example.testproject.models.Good;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
//@AllArgsConstructor
@RequestMapping("/good")
public class GoodController {

   private final GoodService goodService;

    // поставить товар
    @PostMapping("/supply")
    public GoodOperationDTO supplyGoods(@RequestBody List<GoodDTO> goodsDTO) {
        return goodService.supplyGoods(goodsDTO);
    }

    // продать товар
    @PostMapping("/sell")
    public GoodOperationDTO sellGoods(@RequestBody List<GoodDTO> goodsDTO) {
        return goodService.sellGoods(goodsDTO);
    }

    // аналитика по товарам
    // все товары
    @GetMapping("/all")
    public List<Good> findAll() {
        return goodService.findAll();
    }

    // товары определенного контрагента
    @GetMapping("/counterpart")
    List<Good> getGoodsByCounterpartName(@RequestParam("counterpartName")
                                                 String counterpartName) {
        return goodService.getGoodsByCounterPartName(counterpartName);
    }

    // доступное количество товара на складе на определенную дату
    @GetMapping("/available/quantity")
    Integer getGoodsAvailableQuantityByDate(@RequestParam("item") String item
            , @RequestParam("date") Date date) {
        return goodService.getGoodsAvailableQuantityByDate(item, date).orElse(0);
    }

    // аналитика по доходу от продаж
    // доход от продаж за определенный период времени
    @GetMapping("/salesincome/period")
    int getSalesIncomeForPeriod(@RequestParam("dateFrom") Date dateFrom,
                                @RequestParam("dateTo") Date dateTo) {
        return goodService.getSalesIncomeForPeriod(dateFrom, dateTo);
    }

    // доход от продаж определенного товара за определенный период времени
    @GetMapping("/salesincome/good/period")
    int getSalesIncomeGoodForPeriod(@RequestParam("item") String item,
                                    @RequestParam("dateFrom") Date dateFrom,
                                    @RequestParam("dateTo") Date dateTo) {
        return goodService.getSalesIncomeGoodForPeriod(item, dateFrom, dateTo);
    }

    // доход от продаж определенного товара определенному покупателю
    // за определенный период времени
    @GetMapping("/salesincome/counterpart/good/period")
    int getSalesIncomeForPeriod2(@RequestParam("counterpartName") String counterpartName,
                                 @RequestParam("item") String item,
                                 @RequestParam("dateFrom") Date dateFrom,
                                 @RequestParam("dateTo") Date dateTo) {
        return goodService.getSalesIncomeCounterpartGoodForPeriod(counterpartName,
                item, dateFrom, dateTo);
    }

//    @GetMapping("/salesincome/filter")
//    int getSalesIncomeFilter(@RequestParam("counterpartName") String counterpartName,
//                             @RequestParam("item") String item,
//                             @RequestParam("dateFrom") Date dateFrom,
//                             @RequestParam("dateTo") Date dateTo) {
//        GoodOperationSpecificationDTO goodOperationSpecificationDTO =
//                new GoodOperationSpecificationDTO(item,counterpartName,dateFrom,dateTo);
//        return goodService.
//    }

//    @PostMapping("/create/good")
//    public Good createGood(@RequestBody Good good) {
//        return goodService.createGood(good);
//    }
//
//    @PostMapping("/sell/good")
//    public Good sellGood(@RequestBody Good good) {
//        return goodService.sellGood(good);
//    }
}
