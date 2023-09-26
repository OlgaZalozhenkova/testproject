package com.example.testproject.controllers;

import com.example.testproject.dto.GoodDTO;
import com.example.testproject.dto.SupplierDTO;
import com.example.testproject.models.Good;
import com.example.testproject.models.Operation;
import com.example.testproject.models.Supplier;
import com.example.testproject.repositories.GoodRepository;
import com.example.testproject.repositories.OperationRepository;
import com.example.testproject.repositories.SupplierRepository;
import com.example.testproject.servicies.GoodService;
import com.example.testproject.servicies.SupplierService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.*;


@RestController
@RequestMapping("/good")
public class GoodController {
    GoodRepository goodRepository;
    SupplierRepository supplierRepository;
    ModelMapper modelMapper;
    GoodService goodService;
    SupplierService supplierService;
    OperationRepository operationRepository;

    @Autowired
    public GoodController(GoodRepository goodRepository, SupplierRepository supplierRepository, ModelMapper modelMapper, GoodService goodService, SupplierService supplierService, OperationRepository operationRepository) {
        this.goodRepository = goodRepository;
        this.supplierRepository = supplierRepository;
        this.modelMapper = modelMapper;
        this.goodService = goodService;
        this.supplierService = supplierService;
        this.operationRepository = operationRepository;
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

    @PostMapping("/create/goodDTO")
    public Good createGood(@RequestBody GoodDTO goodDTO) {
        return goodService.createGoodDTO1(goodDTO);
    }

//    @PostMapping("/create/good")
//    public Good createGood(@RequestBody Good good) {
//        Supplier supplier = good.getSupplier();
//        supplierRepository.save(supplier);
//        return goodRepository.save(good);
//    }

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

//    @GetMapping("/operation")
//    public java.util.Map<Good,Operation> findOperation(@RequestParam("name") String name) {
//        List<Operation> operations = operationRepository.findByName(name);
//        java.util.Map<Good,Operation> goods = new HashMap<>();
//        for (Operation operation: operations
//             ) {
//            goods.add(operation.getGoods());
//        }
//
//    }
}
