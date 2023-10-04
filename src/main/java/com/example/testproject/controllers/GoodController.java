package com.example.testproject.controllers;

import com.example.testproject.dto.*;
import com.example.testproject.models.Good;
import com.example.testproject.models.GoodCard;
import com.example.testproject.models.GoodOperation;
import com.example.testproject.models.Supplier;
import com.example.testproject.repositories.GoodCardRepository;
import com.example.testproject.repositories.GoodOperationRepository;
import com.example.testproject.repositories.GoodRepository;
import com.example.testproject.repositories.SupplierRepository;
import com.example.testproject.servicies.GoodService;
import com.example.testproject.servicies.SupplierService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


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

    @Autowired
    public GoodController(GoodRepository goodRepository, SupplierRepository supplierRepository, ModelMapper modelMapper, GoodService goodService, SupplierService supplierService, GoodOperationRepository goodOperationRepository, GoodCardRepository goodCardRepository) {
        this.goodRepository = goodRepository;
        this.supplierRepository = supplierRepository;
        this.modelMapper = modelMapper;
        this.goodService = goodService;
        this.supplierService = supplierService;
        this.goodOperationRepository = goodOperationRepository;
        this.goodCardRepository = goodCardRepository;
    }

    @GetMapping("/good/card/{name}")
    public GoodCard findGoodCardByGoodId (@PathVariable("name") String name) {
        return goodCardRepository.findGoodCardByGoodId(name);
    }

    @GetMapping("/all")
    public List<Good> findAll() {
        return goodRepository.findAll();
    }

    @GetMapping("/all/operations")
    public List<GoodOperation> findAllOperations() {
        return goodOperationRepository.findAll();
    }

    @GetMapping("/item/{id}")
    public Good findGood(@PathVariable("id") int id) {
        return goodRepository.getGoodByIdQuery(id);
    }

    @GetMapping("/item")
    public Good findGood(@RequestParam("name") String name) {
        return goodRepository.findByName(name);
    }

    @GetMapping("/supplier/{id}")
    public Supplier findSupplier(@PathVariable("id") int id) {
        return supplierRepository.getSupplierByIdQuery(id);
    }

    @GetMapping("/supplier/name")
    public List<Supplier> findSupplier(@RequestParam("name") String name) {
        return supplierRepository.getSuppliers(name);
    }

    @PostMapping("/create/good")
    public Good createGood(@RequestBody Good good) {
        return goodService.createGood(good);
    }

    @PostMapping("/sell/good")
    public Good sellGood(@RequestBody Good good) {
        return goodService.sellGood(good);
    }

    @PostMapping("/operation")
    public GoodDTOOperation1 createGoodDTOOperation1(@RequestParam("operation") String operation
            ,@RequestBody List<GoodDTO1> goodsDTO1) {
        return goodService.createGoodsDTOOperation1(goodsDTO1,operation);
    }
    @GetMapping("/supplier")
    public Supplier findByName(@RequestParam("name") String name) {
        return supplierRepository.findByName(name);
    }

    @GetMapping("/query/{id}")
    public List<Good> query(@PathVariable("id") int id) {
        return goodRepository.getGoodsBySupplierId(id);
    }

    @GetMapping("/query/name")
    public List<Good> query(@RequestParam("name") String name) {
        return goodRepository.getGoodsBySupplierName(name);
    }

    @GetMapping("/operations")
    public List<GoodOperation> getGoodOperationsBySupplierName(@RequestParam("supplierName") String supplierName) {
        return goodOperationRepository.getGoodOperationsBySupplierName(supplierName);
    }

    @PostMapping("/create/change/goodcard")
    public GoodCard createOrChangeGoodCard(@RequestBody GoodCard goodCard) {
        return goodService.createOrChangeGoodCard1(goodCard);
    }

    @GetMapping("/sell/good/quantity")
    public List<GoodOperation> getSellQuantity(@RequestParam("supply") String supply
            , @RequestParam("item") String item) {
        return goodOperationRepository
                .getSellQuantity(supply, item);
    }

    @GetMapping("/operation/supplier")
    public List<GoodOperation> getGoodOperationsByOperationAndSupplierName(@RequestParam("operationCurrent") String operationCurrent, @RequestParam("supplierName") String supplierName) {
        return goodOperationRepository.getGoodOperationsByOperationAndSupplierName(operationCurrent, supplierName);
    }

    @GetMapping("/operation/item/date")
    public List<GoodOperation> getGoodOperationsByItemAndDate(@RequestParam("item") String item, @RequestParam("date") Date date) {
        return goodOperationRepository.getGoodOperationsByItemAndDate(item, date);
    }

    @GetMapping("/operation/supplier/date")
    public List<GoodOperation> geOperationsByOperationAndSupplierNameAndDate(
            @RequestParam("operationCurrent") String operationCurrent,
            @RequestParam("supplierName") String supplierName,
            @RequestParam("dateFrom") Date dateFrom, @RequestParam("dateTo") Date dateTo) {
        return goodOperationRepository.getOperationsByOperationAndSupplierNameAndDate(operationCurrent,
                supplierName, dateFrom, dateTo);
    }

    @GetMapping("/operation/date")
    public List<GoodOperation> getGoodOperationsByOperationCurrent(@RequestParam("dateFrom") Date dateFrom, @RequestParam("dateTo") Date dateTo) {
        return goodOperationRepository.getGoodOperationsByDate(dateFrom, dateTo);
    }

    @PostMapping("/create/goods")
    public List<Good> createGoods(@RequestBody List<Good> goods) {
        return goodService.createGoods(goods);
    }
//
//    @PostMapping("/sell/goods")
//    public List<Good> sellGoods(@RequestBody List<Good> goods) {
//        return goodService.sellGoods(goods);
//    }

    //    @PostMapping("/create/goodDTOCustomer")
//    public GoodDTOCustomer createGoodDTOCustomer(@RequestBody GoodDTO goodDTO) {
//        return goodService.createGoodDTOCustomer(goodDTO);
//    }

//    @PostMapping("/sell/goodsDTO")
//    public List<Good> sellGoodsDTO(@RequestBody List<GoodDTO> goodsDTO) {
//        return goodService.sellGoodsDTO(goodsDTO);
//    }

    //    @PostMapping("/create/goodsDTOOperation")
//    public GoodDTOOperation createGoodDTOOperation(@RequestBody List<GoodDTO> goodsDTO) {
//        return goodService.createGoodsDTOOperation(goodsDTO);
//    }

//    @PostMapping("/create/goodsDTOCustomer")
//    public List<GoodDTOCustomer> createGoodDTO(@RequestBody List<GoodDTO> goodsDTO) {
//        return goodService.createGoodsDTOCustomer(goodsDTO);
//    }

    //    @GetMapping("/sell/good/quantity")
//    public List<GoodOperation> getGoodOperationsByOperationCurrent
//            (@RequestParam("operationcurrent") String operationCurrent
//            , @RequestParam("goodname") String supplierName) {
//        return goodOperationRepository
//                .getSellQuantity(operationCurrent,supplierName);
//    }
}
