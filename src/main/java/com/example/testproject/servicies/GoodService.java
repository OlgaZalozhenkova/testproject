package com.example.testproject.servicies;

import com.example.testproject.dto.GoodDTO;
import com.example.testproject.models.Good;
import com.example.testproject.models.GoodOperation;
import com.example.testproject.models.Supplier;
import com.example.testproject.repositories.GoodOperationRepository;
import com.example.testproject.repositories.GoodRepository;
import com.example.testproject.repositories.SupplierRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class GoodService {
    GoodRepository goodRepository;
    SupplierRepository supplierRepository;
    ModelMapper modelMapper;
    GoodOperationRepository goodOperationRepository;

    @Autowired
    public GoodService(GoodRepository goodRepository, SupplierRepository supplierRepository, ModelMapper modelMapper, GoodOperationRepository goodOperationRepository) {
        this.goodRepository = goodRepository;
        this.supplierRepository = supplierRepository;
        this.modelMapper = modelMapper;
        this.goodOperationRepository = goodOperationRepository;
    }

    @Transactional
    public Good createGood(Good good) {
        String operationCurrent = "supply";
        Supplier supplier = good.getSuppliers().get(0);
        String nameForCheck = good.getName();
        Good goodDB = goodRepository.findByName(nameForCheck);

        if (goodDB == null) {
            Supplier supplierForCheck = good.getSuppliers().get(0);
            Supplier supplierDB = supplierRepository.findByName(supplierForCheck.getName());
            if (supplierDB == null) {
                supplierRepository.save(good.getSuppliers().get(0));
                goodRepository.save(good);
                createGoodOperation(good,operationCurrent);
            } else {
//                в списке передан только один поставщик
                List<Supplier> goodSuppliers = good.getSuppliers();
//                замена на сохраненного поставщика
//                goodSuppliers.remove(0);
//                goodSuppliers.add(supplierDB);
                goodSuppliers.set(0, supplierDB);
                goodRepository.save(good);
                createGoodOperation(good, operationCurrent);
            }
            return good;

        } else {
            int priceDB = goodDB.getPrice();
            int quantityDB = goodDB.getQuantity();

            int price = good.getPrice();
            int quantity = good.getQuantity();

            int quantityNew = quantityDB + quantity;
            int priceNew = (priceDB * quantityDB + price * quantity) / quantityNew;

            goodDB.setQuantity(quantityNew);
            goodDB.setPrice(priceNew);

            List<Supplier> suppliersDB = goodDB.getSuppliers();
            Supplier supplierForCheck = good.getSuppliers().get(0);
            Supplier supplierDB = supplierRepository.findByName(supplierForCheck.getName());

/*          если товар существует,изменяю цену и количество,
            добавляю новую операцию в список Operation (нет поля goodOperations
            -> нет supplier
            -> нет supplier id),
            сохраняю поставщика, назначается id поставщика (нет goodOperations,
            он создается в конце
            -> нет связи по supplier id)

            */

            if (supplierDB == null) {
                suppliersDB.add(supplierForCheck);
                supplierRepository.saveAll(suppliersDB);
                goodDB.setSuppliers(suppliersDB);
                createGoodOperation1(good, goodDB, supplier,operationCurrent);
            } else {
                createGoodOperation1(good, goodDB, supplierDB,operationCurrent);

            }
            return goodDB;
        }
    }

    @Transactional
    public void createGoodOperation(Good good, String operationCurrent) {
        String item = good.getName();
//        String operationCurrent = "supply";
        int price = good.getPrice();
        int quantity = good.getQuantity();
        Date date = new Date();
        String supplierName = good.getSuppliers().get(0).getName();
//        поскольку товар новый у него только один поставщик в списке
        Supplier supplier = good.getSuppliers().get(0);
        GoodOperation goodOperation = new GoodOperation(item, operationCurrent,
                price, quantity, supplierName, good, supplier, date);
        goodOperationRepository.save(goodOperation);
    }

    @Transactional
    public void createGoodOperation1(Good good, Good goodDB, Supplier supplierDB,
                                     String operationCurrent) {
        String item = good.getName();
//        String operationCurrent = "supply";
        int price = good.getPrice();
        int quantity = good.getQuantity();
        Date date = new Date();
        String supplierName = good.getSuppliers().get(0).getName();
        GoodOperation goodOperation = new GoodOperation(item, operationCurrent,
                price, quantity, supplierName, goodDB, supplierDB, date);
        goodOperationRepository.save(goodOperation);
    }

    @Transactional
    public Good createGoodDTO1(GoodDTO goodDTO) {
        Good good = modelMapper.map(goodDTO, Good.class);
        Supplier supplier = good.getSuppliers().get(0);
        supplierRepository.save(supplier);
        return goodRepository.save(good);
    }

    @Transactional
    public List<Good> createGoods(List<Good> goods) {
        List<Good> savedGoods = new ArrayList<>();
        for (Good good:goods
             ) {
            createGood(good);
            savedGoods.add(good);
        }
        return savedGoods;
    }

    @Transactional
    public Good sellGood(Good good) {
//        ENUM
        String operationCurrent = "selling";
        Good goodDB = goodRepository.findByName(good.getName());
        Supplier supplier = good.getSuppliers().get(0);
        if (goodDB != null) {
            int priceNew = 0;
            int priceDB = goodDB.getPrice();
            int quantityDB = goodDB.getQuantity();

            if (good.getQuantity() > quantityDB) {
                return null;
            }

            int price = good.getPrice();
            int quantity = good.getQuantity();

            int quantityNew = quantityDB - quantity;
            if (quantityNew != 0) {
                priceNew = (priceDB * quantityDB - priceDB * quantity) / quantityNew;
            }
            goodDB.setQuantity(quantityNew);
            goodDB.setPrice(priceNew);

            List<Supplier> suppliersDB = goodDB.getSuppliers();
            Supplier supplierForCheck = good.getSuppliers().get(0);
            Supplier supplierDB = supplierRepository.findByName(supplierForCheck.getName());

            if (supplierDB == null) {
                suppliersDB.add(supplierForCheck);
                supplierRepository.saveAll(suppliersDB);
                goodDB.setSuppliers(suppliersDB);
                createGoodOperation1(good, goodDB, supplier,operationCurrent);
            } else {
                createGoodOperation1(good, goodDB, supplierDB, operationCurrent);

            }
            return good;
        } else {
            return null;
        }
    }
}