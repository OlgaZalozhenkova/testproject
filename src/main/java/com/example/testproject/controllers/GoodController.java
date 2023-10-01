package com.example.testproject.controllers;

import com.example.testproject.dto.GoodDTO;
import com.example.testproject.dto.SupplierDTO;
import com.example.testproject.models.Good;
import com.example.testproject.models.GoodOperation;
import com.example.testproject.models.Supplier;
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

    @Autowired
    public GoodController(GoodRepository goodRepository, SupplierRepository supplierRepository, ModelMapper modelMapper, GoodService goodService, SupplierService supplierService, GoodOperationRepository goodOperationRepository) {
        this.goodRepository = goodRepository;
        this.supplierRepository = supplierRepository;
        this.modelMapper = modelMapper;
        this.goodService = goodService;
        this.supplierService = supplierService;
        this.goodOperationRepository = goodOperationRepository;
    }

    @GetMapping("/all")
    public List<Good> findAll() {
        return goodRepository.findAll();
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

    @PostMapping("/create/good")
    public Good createGood(@RequestBody Good good) {
        return goodService.createGood(good);
    }

    @PostMapping("/create/goods")
    public List<Good> createGoods(@RequestBody List<Good> goods) {
        return goodService.createGoods(goods);
    }

    @PostMapping("/sell/goods")
    public List<Good> sellGoods(@RequestBody List<Good> goods) {
        return goodService.sellGoods(goods);
    }

    @PostMapping("/sell/good")
    public Good sellGood(@RequestBody Good good) {
        return goodService.sellGood(good);
    }

    @PostMapping("/create/goodDTO")
    public Good createGood(@RequestBody GoodDTO goodDTO) {
        return goodService.createGoodDTO(goodDTO);
    }

    @PostMapping("/create/goodsDTO")
    public List<Good> createGoodDTO(@RequestBody List<GoodDTO> goodsDTO) {
        return goodService.createGoodsDTO(goodsDTO);
    }

    @PostMapping("/sell/goodsDTO")
    public List<Good> sellGoodsDTO(@RequestBody List<GoodDTO> goodsDTO) {
        return goodService.sellGoodsDTO(goodsDTO);
    }

    @PostMapping("/create/supplier")
    public Supplier createSupplier(@RequestBody SupplierDTO supplierDTO) {
        return supplierService.createSupplier(supplierDTO);
    }

    @GetMapping("/supplier")
    public Supplier findByName(@RequestParam("name") String name) {
        return supplierRepository.findByName(name);
    }

    @GetMapping("/query/{id}")
    public List<Good> query(@PathVariable("id") int id) {
        return goodRepository.getGoodsBySupplierId(id);
    }

    @GetMapping("/operations")
    public List<GoodOperation> getGoodOperationsBySupplierName(@RequestParam("supplierName") String supplierName) {
        return goodOperationRepository.getGoodOperationsBySupplierName(supplierName);
    }

    @GetMapping("/operation/current")
    public List<GoodOperation> getGoodOperationsByOperationCurrent(@RequestParam("operationCurrent") String operationCurrent) {
        return goodOperationRepository.getGoodOperationsByOperationCurrent(operationCurrent);
    }

    @GetMapping("/operation/date")
    public List<GoodOperation> getGoodOperationsByOperationCurrent(@RequestParam("dateFrom") Date dateFrom, @RequestParam("dateTo") Date dateTo) {
        return goodOperationRepository.getGoodOperationsByDate(dateFrom, dateTo);
    }
}
