package com.example.testproject.controllers;

import com.example.testproject.dto.GoodDTO;
import com.example.testproject.dto.GoodOperationDTO;
import com.example.testproject.dto.GoodOperationSpecificationDTO;
import com.example.testproject.models.Good;
import com.example.testproject.servicies.GoodService;
import com.example.testproject.util.DataNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
@AllArgsConstructor
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
        List<Good> goods = goodService.findAll();
        if (goods.isEmpty()) {
            throw new DataNotFoundException();
        } else {
            return goods;
        }
    }

    // товары определенного контрагента
    @GetMapping("/counterpart")
    List<Good> getGoodsByCounterpartName(@RequestParam("counterpartName")
                                                 String counterpartName) {
        List<Good> goods = goodService.getGoodsByCounterPartName(counterpartName);
        if (goods.isEmpty()) {
            throw new DataNotFoundException();
        } else {
            return goods;
        }
    }

    // доступное количество товара на складе на определенную дату
    @GetMapping("/available/quantity")
    Double getGoodsAvailableQuantityByDate(@RequestParam("item") String item
            , @RequestParam("date") Date date) {
        return goodService.getGoodsAvailableQuantityByDate(item, date).orElse(0.0);
    }

    // аналитика по доходу от продаж
    @GetMapping("/salesincome/filter")
    double getSalesIncomeFilter(@RequestParam(value = "counterpartName", required = false) String counterpartName,
                             @RequestParam(value = "item", required = false) String item,
                             @RequestParam(value = "dateFrom", required = false) Date dateFrom,
                             @RequestParam(value = "dateTo", required = false) Date dateTo) {

        GoodOperationSpecificationDTO goodOperationSpecificationDTO =
                new GoodOperationSpecificationDTO(item, counterpartName, dateFrom, dateTo);
        return goodService.getSalesIncomeFilter(goodOperationSpecificationDTO);
    }

}
