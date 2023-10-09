package com.example.testproject.controllers;

import com.example.testproject.dto.*;
import com.example.testproject.models.*;
import com.example.testproject.repositories.*;
import com.example.testproject.servicies.GoodService;
import com.example.testproject.servicies.SupplierService;
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
    SupplierRepository supplierRepository;
    ModelMapper modelMapper;
    GoodService goodService;
    SupplierService supplierService;
    GoodOperationRepository goodOperationRepository;
    GoodCardRepository goodCardRepository;
    RatingRepository1 ratingRepository1;

    @Autowired
    public GoodController(GoodRepository goodRepository, SupplierRepository supplierRepository, ModelMapper modelMapper, GoodService goodService, SupplierService supplierService, GoodOperationRepository goodOperationRepository, GoodCardRepository goodCardRepository, RatingRepository1 ratingRepository1) {
        this.goodRepository = goodRepository;
        this.supplierRepository = supplierRepository;
        this.modelMapper = modelMapper;
        this.goodService = goodService;
        this.supplierService = supplierService;
        this.goodOperationRepository = goodOperationRepository;
        this.goodCardRepository = goodCardRepository;
        this.ratingRepository1 = ratingRepository1;
    }

    // создать или изменить карточку товара
    @PostMapping("/create/change/goodcard")
    public String createOrChangeGoodCard(@RequestBody GoodCardDTO goodCardDTO) {
        return goodService.createOrChangeGoodCard1(goodCardDTO);
    }

    // карточка товара по наименованию товара
    @GetMapping("/good/card/{name}")
    public GoodCard findGoodCardByGoodId(@PathVariable("name") String name) {
        return goodCardRepository.findGoodCardByGoodId(name);
    }

    // купить или продать товар
    @PostMapping("/operation")
    public GoodDTOOperation1 createGoodDTOOperation1(@RequestParam("operation") String operation
            , @RequestBody List<GoodDTO1> goodsDTO1) {
        return goodService.createGoodsDTOOperation1(goodsDTO1, operation);
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
    public RatingDTOForCustomer setRating(@RequestBody RatingDTO ratingDTO) {
        return goodService.setRating(ratingDTO);
    }

    // изменить оценку можно только один раз
    @PostMapping("/change/rating")
    public RatingDTOForCustomer changeRating(@RequestBody RatingDTO ratingDTO) {
        return goodService.changeRating(ratingDTO);
    }

    // удалить оценку можно только один раз
    @PostMapping("/delete/rating")
    public RatingDTOForCustomer deleteRating(@RequestBody RatingDTO ratingDTO) {
        return goodService.deleteRating(ratingDTO);
    }

    // аналитика по рейтингам
    // рейтинг из журнала рейтингов по наименованию товара и наименованию покупателя
    @GetMapping("/get/for/rating1")
    public Optional<Rating> findByGoodAndSupplier(@RequestParam("goodName") String goodName,
                                                  @RequestParam("supplierName") String supplierName) {
        return ratingRepository1.findByGoodAndSupplier(goodName, supplierName);
    }

    // аналитика по журналу операций
    // все операции
    @GetMapping("/operations/all")
    public List<GoodOperation> findAllOperations() {
        return goodOperationRepository.findAll();
    }

    // журнал операций купли/продажи по наименованию контрагента
    @GetMapping("/operations/counterpartname")
    public List<GoodOperation> getGoodOperationsBySupplierName(@RequestParam("supplierName") String supplierName) {
        return goodOperationRepository.getGoodOperationsBySupplierName(supplierName);
    }

    // журнал операций купли/продажи по наименованию операции и наименованию контрагента
    @GetMapping("/operations/operationname/counterpartname")
    public List<GoodOperation> getGoodOperationsByOperationAndSupplierName(
            @RequestParam("operationCurrent") String operationCurrent,
            @RequestParam("supplierName") String supplierName) {
        return goodOperationRepository.getGoodOperationsByOperationAndSupplierName(operationCurrent, supplierName);
    }

    // журнал операций купли/продажи за указанный период
    @GetMapping("/operations/date")
    public List<GoodOperation> getGoodOperationsByOperationCurrent(@RequestParam("dateFrom") Date dateFrom, @RequestParam("dateTo") Date dateTo) {
        return goodOperationRepository.getGoodOperationsByDate(dateFrom, dateTo);
    }

    // журнал операций купли/продажи по наименованию операции и наименованию контрагента за указанный период
    @GetMapping("/operations/counterpart/date")
    public List<GoodOperation> geOperationsByOperationAndSupplierNameAndDate(
            @RequestParam("operationCurrent") String operationCurrent,
            @RequestParam("supplierName") String supplierName,
            @RequestParam("dateFrom") Date dateFrom, @RequestParam("dateTo") Date dateTo) {
        return goodOperationRepository.getOperationsByOperationAndSupplierNameAndDate(operationCurrent,
                supplierName, dateFrom, dateTo);
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
    public List<Supplier> findCounterpartsByGoodName(@RequestParam("name") String name) {
        return supplierRepository.findCounterpartsByGoodName(name);
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
