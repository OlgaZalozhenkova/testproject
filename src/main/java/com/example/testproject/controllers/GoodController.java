package com.example.testproject.controllers;

import com.example.testproject.dto.*;
import com.example.testproject.models.Good;
import com.example.testproject.servicies.GoodService;
import com.example.testproject.util.DataNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@AllArgsConstructor
@RequestMapping("/good")
public class GoodController {

    private final GoodService goodService;
    private final SimpleDateFormat simpleDateFormat;

    // поставить товар


    @PostMapping("/supply")
    public GoodOperationDTO supplyGoods(@RequestBody GoodsObject goodsObject) {
        return goodService.supplyGoods(goodsObject);
    }

    // продать товар
    @PostMapping("/sell")
    public GoodOperationDTO sellGoods(@RequestBody GoodsObject goodsObject) {
        return goodService.sellGoods(goodsObject);
    }

    // аналитика по товарам
    // все товары
    @GetMapping("/all")
    public List<Good> findAll() {
        List<Good> goods = goodService.findAll();
        if (goods.isEmpty()) {
            throw new DataNotFoundException();
        } else {
            return goods;
        }
    }

    @GetMapping("/item")
    public Good findByItem(@RequestParam("name") String name) {
        return goodService.findByName(name);
    }

    // товары определенного контрагента
    @GetMapping("/counterpart")
    public List<Good> getGoodsByCounterpartName(@RequestParam("counterpartName")
                                                 String counterpartName) {
        List<Good> goods = goodService.getGoodsByCounterPartName(counterpartName);
        if (goods.isEmpty()) {
            throw new DataNotFoundException();
        } else {
            return goods;
        }
    }

    // товары определенной категории
    @GetMapping("/category")
    public List<Good> findByCategory(@RequestParam("category") String category) {
        List<Good> goods =goodService.findByCategory(category);
        if (goods.isEmpty()) {
            throw new DataNotFoundException();
        } else {
            return goods;
        }
    }

    // доступное количество товара на складе на определенную дату
    @GetMapping("/available/quantity")
    public Double getGoodsAvailableQuantityByDate(@RequestParam("item") String item
            , @RequestParam("date") Date date) {
        return goodService.getGoodsAvailableQuantityByDate(item, date).orElse(0.0);
    }

    // аналитика по доходу от продаж
//    @GetMapping("/salesincome/filter")
//    public double getSalesIncomeFilter(@RequestParam(value = "counterpartName", required = false) String counterpartName,
//                             @RequestParam(value = "item", required = false) String item,
//                             @RequestParam(value = "fromDate", required = false) String fromDate,
//                             @RequestParam(value = "toDate", required = false) String toDate) throws ParseException {
//
//        simpleDateFormat.applyPattern("yyyy/MM/dd");
//        Date dateFrom = simpleDateFormat.parse(fromDate);
//        Date dateTo = simpleDateFormat.parse(toDate);
//        GoodOperationSpecificationDTO goodOperationSpecificationDTO =
//                new GoodOperationSpecificationDTO(item, counterpartName,dateFrom, dateTo);
//        return goodService.getSalesIncomeFilter(goodOperationSpecificationDTO);
//    }

    @GetMapping("/salesincome/filter")
    public double getSalesIncomeFilter(@RequestParam(value = "counterpartName", required = false) String counterpartName,
                                       @RequestParam(value = "item", required = false) String item,
                                       @RequestParam(value = "dateFrom", required = false) Date dateFrom,
                                       @RequestParam(value = "dateTo", required = false) Date dateTo)  {

          GoodOperationSpecificationDTO goodOperationSpecificationDTO =
                new GoodOperationSpecificationDTO(item, counterpartName,dateFrom, dateTo);
        return goodService.getSalesIncomeFilter(goodOperationSpecificationDTO);
    }

    @PostMapping("/inventory")
    public String makeInventory(@RequestBody InventoryObject inventoryObject) {
        return goodService.makeInventory(inventoryObject);
    }
}
