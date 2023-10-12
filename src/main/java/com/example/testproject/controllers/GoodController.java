package com.example.testproject.controllers;

import com.example.testproject.dto.*;
import com.example.testproject.models.*;
import com.example.testproject.repositories.*;
import com.example.testproject.servicies.GoodService;
import com.example.testproject.servicies.CounterpartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/good")
public class GoodController {
    GoodRepository goodRepository;
    CounterpartRepository counterpartRepository;
    ModelMapper modelMapper;
    GoodService goodService;
    CounterpartService counterpartService;
    GoodOperationRepository goodOperationRepository;
    GoodCardRepository goodCardRepository;
    RatingRepository ratingRepository;

    @Autowired
    public GoodController(GoodRepository goodRepository, CounterpartRepository counterpartRepository, ModelMapper modelMapper, GoodService goodService, CounterpartService counterpartService, GoodOperationRepository goodOperationRepository, GoodCardRepository goodCardRepository, RatingRepository ratingRepository) {
        this.goodRepository = goodRepository;
        this.counterpartRepository = counterpartRepository;
        this.modelMapper = modelMapper;
        this.goodService = goodService;
        this.counterpartService = counterpartService;
        this.goodOperationRepository = goodOperationRepository;
        this.goodCardRepository = goodCardRepository;
        this.ratingRepository = ratingRepository;
    }

    // создать или изменить карточку товара
    @PostMapping("/create/change/goodcard")
    public String createOrChangeGoodCard(@RequestBody GoodCardDTO goodCardDTO) {
        return goodService.createOrChangeGoodCard(goodCardDTO);
    }

    // карточка товара по наименованию товара
    // Optional
    @GetMapping("/good/card/{name}")
    public GoodCard findGoodCardByGoodId(@PathVariable("name") String name) {
        return goodCardRepository.findGoodCardByGoodId(name);
    }

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

    // доступное количество товара на складе на определенную дату
    @GetMapping("/available/quantity")
    String getGoodsAvailableQuantityByDate(@RequestParam("item") String item
            , @RequestParam("date") Date date) {
        return goodService.getGoodsAvailableQuantityByDate(item, date);
    }

    // Рейтинги
    // поставить оценку можно только один раз
    @PostMapping("/set/rating")
    public String setRating(@RequestBody RatingDTO ratingDTO) {
        return goodService.setRating(ratingDTO);
    }

    // изменить оценку можно только один раз
    @PostMapping("/change/rating")
    public String changeRating(@RequestBody RatingDTO ratingDTO) {
        return goodService.changeRating(ratingDTO);
    }

    // удалить оценку можно только один раз
    @PostMapping("/delete/rating")
    public String deleteRating(@RequestBody RatingDTO ratingDTO) {
        return goodService.deleteRating(ratingDTO);
    }

    // аналитика по рейтингам
    // рейтинг из журнала рейтингов по наименованию товара и наименованию покупателя
    @GetMapping("/get/for/rating1")
    public Optional<Rating> findByGoodAndCounterpart(@RequestParam("goodName") String goodName,
                                                  @RequestParam("counterpartName") String counterpartName) {
        return ratingRepository.findByGoodAndCounterpart(goodName, counterpartName);
    }

    // аналитика по журналу операций
    // все операции
    @GetMapping("/operations/all")
    public List<GoodOperation> findAllOperations() {
        return goodOperationRepository.findAll();
    }

    // журнал операций купли/продажи по наименованию контрагента
    @GetMapping("/operations/counterpartname")
    public List<GoodOperation> getGoodOperationsByCounterpartName(@RequestParam("counterpartName") String counterpartName) {
        return goodOperationRepository.getGoodOperationsByCounterpartName(counterpartName);
    }

    // журнал операций купли/продажи по наименованию операции и наименованию контрагента
    @GetMapping("/operations/operationname/counterpartname")
    public List<GoodOperation> getGoodOperationsByOperationAndCounterpartName(
            @RequestParam("operationCurrent") String operationCurrent,
            @RequestParam("counterpartName") String counterpartName) {
        return goodOperationRepository.getGoodOperationsByOperationAndCounterpartName(operationCurrent, counterpartName);
    }

    // журнал операций купли/продажи за указанный период
    @GetMapping("/operations/date")
    public List<GoodOperation> getGoodOperationsByOperationCurrent(@RequestParam("dateFrom") Date dateFrom, @RequestParam("dateTo") Date dateTo) {
        return goodOperationRepository.getGoodOperationsByDate(dateFrom, dateTo);
    }

    // журнал операций купли/продажи по наименованию операции и наименованию контрагента за указанный период
    @GetMapping("/operations/counterpart/date")
    public List<GoodOperation> geOperationsByOperationAndCounterpartNameAndDate(
            @RequestParam("operationCurrent") String operationCurrent,
            @RequestParam("counterpartName") String counterpartName,
            @RequestParam("dateFrom") Date dateFrom, @RequestParam("dateTo") Date dateTo) {
        return goodOperationRepository.getOperationsByOperationAndCounterpartNameAndDate(operationCurrent,
                counterpartName, dateFrom, dateTo);
    }

    // аналитика по товарам
    // все товары
    @GetMapping("/all")
    public List<Good> findAll() {
        return goodRepository.findAll();
    }

    // аналитика по контрагентам
    // список контрагентов по наименованию товара
    @GetMapping("/counterparts/goodname")
    public List<Counterpart> findCounterpartsByGoodName(@RequestParam("name") String name) {
        return counterpartRepository.findCounterpartsByGoodName(name);
    }

//    @PostMapping("/create/good")
//    public Good createGood(@RequestBody Good good) {
//        return goodService.createGood(good);
//    }
//
//    @PostMapping("/sell/good")
//    public Good sellGood(@RequestBody Good good) {
//        return goodService.sellGood(good);
//    }

//    @PostMapping("/create/change/goodcard")
//    public GoodCard createOrChangeGoodCard(@RequestBody GoodCard goodCard) {
//        return goodService.createOrChangeGoodCard(goodCard);
//    }

}
