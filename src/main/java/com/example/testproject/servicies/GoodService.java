package com.example.testproject.servicies;

import com.example.testproject.dto.*;
import com.example.testproject.models.Good;
import com.example.testproject.models.GoodCard;
import com.example.testproject.models.GoodOperation;
import com.example.testproject.models.Supplier;
import com.example.testproject.repositories.GoodCardRepository;
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
    GoodCardRepository goodCardRepository;

    @Autowired
    public GoodService(GoodRepository goodRepository, SupplierRepository supplierRepository, ModelMapper modelMapper, GoodOperationRepository goodOperationRepository, GoodCardRepository goodCardRepository) {
        this.goodRepository = goodRepository;
        this.supplierRepository = supplierRepository;
        this.modelMapper = modelMapper;
        this.goodOperationRepository = goodOperationRepository;
        this.goodCardRepository = goodCardRepository;
    }

    @Transactional
    public Good createGood(Good good) {
        String goodNameForCheck = good.getName();
        GoodCard goodCard = goodCardRepository.findByName(goodNameForCheck);

        if (goodCard == null) {
            //            else запрос или исключение
            return null;
        }
        int price = good.getPrice();
        int priceSupply = goodCard.getPriceSupply();
        if (price > priceSupply) {
            return null; //исключение
        }

        String item = good.getName();
        String operationCurrent = "supply";
        int quantity = good.getQuantity();
        Date date = new Date();
        String supplierName = good.getSuppliers().get(0).getName();

        Supplier supplierForCheck = good.getSuppliers().get(0);
        List<Supplier> goodSuppliers = good.getSuppliers();

        Good goodDB = goodRepository.findByName(goodNameForCheck);
        Supplier supplierDB = supplierRepository.findByName(supplierForCheck.getName());

        if (goodDB == null) {

            goodCard.setAvailableQuantity(quantity);

            if (supplierDB == null) {
                supplierRepository.save(supplierForCheck);
                goodRepository.save(good);
                createGoodOperation(item, operationCurrent, price,
                        quantity, supplierName, good, supplierForCheck, date, price, quantity);
            } else {
                goodSuppliers.set(0, supplierDB);
                goodRepository.save(good);
                createGoodOperation(item, operationCurrent, price,
                        quantity, supplierName, good, supplierDB, date, price, quantity);
            }
            good.setGoodCard(goodCard);
            return good;

        } else {
            int priceDB = goodDB.getPrice();
            int quantityDB = goodDB.getQuantity();

            int quantityNew = quantityDB + quantity;
            int priceNew = (priceDB * quantityDB + price * quantity) / quantityNew;

            goodDB.setQuantity(quantityNew);
            goodDB.setPrice(priceNew);

            List<Supplier> suppliersDB = goodDB.getSuppliers();

            goodCard.setAvailableQuantity(quantityNew);

            if (supplierDB == null) {
                supplierRepository.save(supplierForCheck);
                suppliersDB.add(supplierForCheck);
//                supplierRepository.saveAll(suppliersDB);
                goodDB.setSuppliers(suppliersDB);
                createGoodOperation(item, operationCurrent, price,
                        quantity, supplierName, goodDB, supplierForCheck,
                        date, priceNew, quantityNew);
            } else {
                boolean isExist = suppliersDB.contains(supplierDB);
                if (!isExist) { // в базе есть, а в списке нет
                    suppliersDB.add(supplierDB);
                    goodDB.setSuppliers(suppliersDB);
                }

                // в базе есть, в списке есть
                createGoodOperation(item, operationCurrent, price,
                        quantity, supplierName, goodDB, supplierDB, date,
                        priceNew, quantityNew
                );
            }
            return goodDB;
        }
    }

    @Transactional
    public void createGoodOperation(String item, String operationCurrent, int price,
                                    int quantity, String supplierName,
                                    Good good, Supplier supplier, Date date,
                                    int priceDB, int quantityDB) {
        GoodOperation goodOperation = new GoodOperation(item, operationCurrent,
                price, quantity, supplierName, good, supplier, date, priceDB, quantityDB);
        goodOperationRepository.save(goodOperation);
    }

    @Transactional
    public GoodDTOOperation1 createGoodsDTOOperation1(List<GoodDTO1> goodsDTO1
            , String operation) {
        int totalSum = 0;
        for (GoodDTO1 goodDTO1 : goodsDTO1
        ) {
            Good good = modelMapper.map(goodDTO1, Good.class);
            Supplier supplier = goodDTO1.getSupplier();
            List<Supplier> suppliers = new ArrayList<>();
            suppliers.add(supplier);
            good.setSuppliers(suppliers);
//            good.setSuppliers(List.of(supplier));
            totalSum = totalSum + goodDTO1.getPrice() * goodDTO1.getQuantity();
            //try catch, тогда должно ничего не продасться(все или ничего)
            if (operation.equals("supply")) {
                createGood(good);
            } else if (operation.equals("selling")) {
                sellGood(good);
            } else return null;//exception
        }

//        TODO поймать исключения
        return new GoodDTOOperation1(goodsDTO1, totalSum);
    }

    @Transactional
    public GoodCard createOrChangeGoodCard1(GoodCard goodCard) {
        GoodCard goodCardDB = goodCardRepository.findByName(goodCard.getName());
        if (goodCardDB == null) {
            return goodCardRepository.save(goodCard);
        } else return null;
    }

    @Transactional
    public Good sellGood(Good good) {

        String item = good.getName();
        int price = good.getPrice();
        GoodCard goodCard = goodCardRepository.findByName(item);
        int priceSelling = goodCard.getPriceSelling();
        if (price < priceSelling) {
            return null; //исключение
        }
        Good goodDB = goodRepository.findByName(good.getName());
        int quantity = good.getQuantity();
        int quantityDB = goodDB.getQuantity();

        if (quantity > quantityDB) { // не хватает количества товаров

//                TODO товаров не хватает и вывести вы можете заказать и вывести остаток на складе
            return null;
        }
        String operationCurrent = "selling";
        Date date = new Date();
        String supplierName = good.getSuppliers().get(0).getName();

        int sellQuantity = goodCard.getSellQuantity();

        if (goodDB != null) {
            int priceNew = 0;
            int priceDB = goodDB.getPrice();

            int quantityNew = quantityDB - quantity;
            if (quantityNew != 0) {
                priceNew = (priceDB * quantityDB - priceDB * quantity) / quantityNew;
            } // если количество 0, то цена 0 остается
            goodDB.setQuantity(quantityNew);// для количества > 0 и количества = 0
            goodDB.setPrice(priceNew);// для количества > 0 и количества = 0

            goodCard.setAvailableQuantity(quantityNew);
            goodCard.setSellQuantity(sellQuantity + quantity);

            List<Supplier> suppliersDB = goodDB.getSuppliers();
            Supplier supplierForCheck = good.getSuppliers().get(0);
            Supplier supplierDB = supplierRepository.findByName(supplierForCheck.getName());

            if (supplierDB == null) {
                supplierRepository.save(supplierForCheck);
                suppliersDB.add(supplierForCheck);
//                supplierRepository.saveAll(suppliersDB);
                goodDB.setSuppliers(suppliersDB);
                createGoodOperation(item, operationCurrent, price,
                        quantity, supplierName, goodDB, supplierForCheck,
                        date, priceNew, quantityNew);
            } else {
                boolean isExist = suppliersDB.contains(supplierDB);
                if (!isExist) { // в базе есть, а в списке нет
                    suppliersDB.add(supplierDB);
                    goodDB.setSuppliers(suppliersDB);
                }

                createGoodOperation(item, operationCurrent, price,
                        quantity, supplierName, goodDB, supplierDB,
                        date, priceNew, quantityNew);
            }
            return goodDB; //данные без измененного количества
        } else {
            return null; // товар не существует
        }
    }

//    @Transactional
//    public GoodDTOCustomer createGoodDTOCustomer(GoodDTO goodDTO) {
//        Good good = modelMapper.map(goodDTO, Good.class);
//
//        createGood(good);
//
//        int priceCurrent = goodDTO.getPrice();
//        int quantityCurrent = goodDTO.getQuantity();
//        Supplier supplier = goodDTO.getSuppliers().get(0);
//
//        SupplierDTO supplierDTO = convertToSupplierDTO(supplier);
//        int totalSum = priceCurrent * quantityCurrent;
//        GoodDTOCustomer goodDTOCustomer = modelMapper.map(good, GoodDTOCustomer.class);
//        enrichGoodDTOCustomer(priceCurrent, quantityCurrent, totalSum, supplierDTO, goodDTOCustomer);
//        return goodDTOCustomer;
//    }

    //    @Transactional
//    public List<GoodDTOCustomer> createGoodsDTOCustomer(List<GoodDTO> goodsDTO) {
//        List<GoodDTOCustomer> savedGoods = new ArrayList<>();
//        for (GoodDTO goodDTO : goodsDTO
//        ) {
//            savedGoods.add(createGoodDTOCustomer(goodDTO));
//        }
//        return savedGoods;
//    }

    //    public SupplierDTO convertToSupplierDTO(Supplier supplier) {
//        return modelMapper.map(supplier, SupplierDTO.class);
//    }

//    public void enrichGoodDTOCustomer(int priceCurrent, int quantityCurrent,
//                                      int totalSum, SupplierDTO supplierDTO,
//                                      GoodDTOCustomer goodDTOCustomer) {
//        goodDTOCustomer.setPriceCurrent(priceCurrent);
//        goodDTOCustomer.setQuantityCurrent(quantityCurrent);
//        goodDTOCustomer.setTotalSum(totalSum);
//        goodDTOCustomer.setSupplierDTO(supplierDTO);
//    }
//
//
//    @Transactional
//    public GoodDTOOperation createGoodsDTOOperation(List<GoodDTO> goodsDTO) {
//        List<GoodDTOCustomer> goodDTOCustomerList = new ArrayList<>();
//        int totalSum = 0;
//        for (GoodDTO goodDTO : goodsDTO
//        ) {
//            goodDTOCustomerList.add(createGoodDTOCustomer(goodDTO));
//            totalSum = totalSum + goodDTO.getPrice() * goodDTO.getQuantity();
//        }
//        return new GoodDTOOperation(goodDTOCustomerList, totalSum);
//    }

    //    @Transactional
//    public void createGoods1(List<Good> goods) {
//        for (Good good : goods) {
//            createGood(good);
//        }
//    }
//
//    @Transactional
//    public List<Good> sellGoodsDTO(List<GoodDTO> goodsDTO) {
//        List<Good> savedGoods = new ArrayList<>();
//        for (GoodDTO goodDTO : goodsDTO
//        ) {
//            Good good = modelMapper.map(goodDTO, Good.class);
//            savedGoods.add(sellGood(good));
//        }
//
//        return savedGoods;
//    }
//
    @Transactional
    public List<Good> createGoods(List<Good> goods) {
        List<Good> savedGoods = new ArrayList<>();
        for (Good good : goods
        ) {
            savedGoods.add(createGood(good));

        }
        return savedGoods;
    }

//    @Transactional
//    public List<Good> sellGoods(List<Good> goods) {
//        List<Good> savedGoods = new ArrayList<>();
//        for (Good good : goods
//        ) {
//            savedGoods.add(sellGood(good));
//
//        }
//        return savedGoods;
//    }

}
