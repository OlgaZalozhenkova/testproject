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
        String item = good.getName();
        String operationCurrent = "supply";
        int price = good.getPrice();
        int quantity = good.getQuantity();
        Date date = new Date();
        String supplierName = good.getSuppliers().get(0).getName();

        String goodNameForCheck = good.getName();
        Supplier supplierForCheck = good.getSuppliers().get(0);
        List<Supplier> goodSuppliers = good.getSuppliers();

        Good goodDB = goodRepository.findByName(goodNameForCheck);
        Supplier supplierDB = supplierRepository.findByName(supplierForCheck.getName());

        if (goodDB == null) {

            if (supplierDB == null) {
                supplierRepository.save(supplierForCheck);
                goodRepository.save(good);
                createGoodOperation(item, operationCurrent, price,
                        quantity, supplierName, good, supplierForCheck, date);
            } else {
                goodSuppliers.set(0, supplierDB);
                goodRepository.save(good);
                createGoodOperation(item, operationCurrent, price,
                        quantity, supplierName, good, supplierDB, date);
            }
            return good;

        } else {
            int priceDB = goodDB.getPrice();
            int quantityDB = goodDB.getQuantity();

            int quantityNew = quantityDB + quantity;
            int priceNew = (priceDB * quantityDB + price * quantity) / quantityNew;

            goodDB.setQuantity(quantityNew);
            goodDB.setPrice(priceNew);

            List<Supplier> suppliersDB = goodDB.getSuppliers();

            if (supplierDB == null) {
                supplierRepository.save(supplierForCheck);
                suppliersDB.add(supplierForCheck);
//                supplierRepository.saveAll(suppliersDB);
                goodDB.setSuppliers(suppliersDB);
                createGoodOperation(item, operationCurrent, price,
                        quantity, supplierName, goodDB, supplierForCheck, date);
            } else {
                boolean isExist = suppliersDB.contains(supplierDB);
                if (!isExist) { // в базе есть, а в списке нет
                    suppliersDB.add(supplierDB);
                    goodDB.setSuppliers(suppliersDB);
                }

                // в базе есть, в списке есть
//                goodRepository.save(goodDB);
                createGoodOperation(item, operationCurrent, price,
                        quantity, supplierName, goodDB, supplierDB, date);
            }
            return goodDB;
        }
    }

    @Transactional
    public void createGoodOperation(String item, String operationCurrent, int price,
                                    int quantity, String supplierName,
                                    Good good, Supplier supplier, Date date) {
        GoodOperation goodOperation = new GoodOperation(item, operationCurrent,
                price, quantity, supplierName, good, supplier, date);
        goodOperationRepository.save(goodOperation);
    }

    @Transactional
    public Good createGoodDTO(GoodDTO goodDTO) {
        Good good = modelMapper.map(goodDTO, Good.class);
        return createGood(good);
    }

    @Transactional
    public List<Good> createGoodsDTO(List<GoodDTO> goodsDTO) {
        List<Good> savedGoods = new ArrayList<>();
        for (GoodDTO goodDTO : goodsDTO
        ) {
            Good good = modelMapper.map(goodDTO, Good.class);
            savedGoods.add(createGood(good));
        }

        return savedGoods;
    }

    @Transactional
    public List<Good> sellGoodsDTO(List<GoodDTO> goodsDTO) {
        List<Good> savedGoods = new ArrayList<>();
        for (GoodDTO goodDTO : goodsDTO
        ) {
            Good good = modelMapper.map(goodDTO, Good.class);
            savedGoods.add(sellGood(good));
        }

        return savedGoods;
    }

    @Transactional
    public List<Good> createGoods(List<Good> goods) {
        List<Good> savedGoods = new ArrayList<>();
        for (Good good : goods
        ) {
            savedGoods.add(createGood(good));

        }
        return savedGoods;
    }

    @Transactional
    public Good sellGood(Good good) {

        String item = good.getName();
        String operationCurrent = "selling";
        int price = good.getPrice();
        int quantity = good.getQuantity();
        Date date = new Date();
        String supplierName = good.getSuppliers().get(0).getName();

        Good goodDB = goodRepository.findByName(good.getName());

        if (goodDB != null) {
            int priceNew = 0;
            int priceDB = goodDB.getPrice();
            int quantityDB = goodDB.getQuantity();

            if (good.getQuantity() > quantityDB) { // не хватает количества товаров
                return null;
            }

            int quantityNew = quantityDB - quantity;
            if (quantityNew != 0) {
                priceNew = (priceDB * quantityDB - priceDB * quantity) / quantityNew;
            } // если количество 0, то цена 0 остается
            goodDB.setQuantity(quantityNew);// для количества > 0 и количества = 0
            goodDB.setPrice(priceNew);// для количества > 0 и количества = 0

            List<Supplier> suppliersDB = goodDB.getSuppliers();
            Supplier supplierForCheck = good.getSuppliers().get(0);
            Supplier supplierDB = supplierRepository.findByName(supplierForCheck.getName());

            if (supplierDB == null) {
                supplierRepository.save(supplierForCheck);
                suppliersDB.add(supplierForCheck);
//                supplierRepository.saveAll(suppliersDB);
                goodDB.setSuppliers(suppliersDB);
                createGoodOperation(item, operationCurrent, price,
                        quantity, supplierName, goodDB, supplierForCheck, date);
            } else {
                boolean isExist = suppliersDB.contains(supplierDB);
                if (!isExist) { // в базе есть, а в списке нет
                    suppliersDB.add(supplierDB);
                    goodDB.setSuppliers(suppliersDB);
                }

                createGoodOperation(item, operationCurrent, price,
                        quantity, supplierName, goodDB, supplierDB, date);
            }
            return goodDB; //данные без измененного количества
        } else {
            return null; // товар не существует
        }
    }

    @Transactional
    public List<Good> sellGoods(List<Good> goods) {
        List<Good> savedGoods = new ArrayList<>();
        for (Good good : goods
        ) {
            savedGoods.add(sellGood(good));

        }
        return savedGoods;
    }
}
