package com.example.testproject.servicies;

import com.example.testproject.dto.*;
import com.example.testproject.models.*;
import com.example.testproject.repositories.*;
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
    RatingRepository1 ratingRepository1;

    @Autowired
    public GoodService(GoodRepository goodRepository, SupplierRepository supplierRepository, ModelMapper modelMapper, GoodOperationRepository goodOperationRepository, GoodCardRepository goodCardRepository, RatingRepository1 ratingRepository1) {
        this.goodRepository = goodRepository;
        this.supplierRepository = supplierRepository;
        this.modelMapper = modelMapper;
        this.goodOperationRepository = goodOperationRepository;
        this.goodCardRepository = goodCardRepository;
        this.ratingRepository1 = ratingRepository1;
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
//    // установить ограничения по рейтингу диапазон оценок
//    //поля не должны быть пустыми
//    // метод только для купленных товаров, оценивать не купленные товары не нужно
//    public Rating setRating(String operationCurrent, String supplierName, String item, double value) {
//
////        Rating ratingDB = ratingRepository1.
////                findByGoodAndSupplier(supplierName, item).orElse(null);
//        Good good = goodRepository.findByName(item);
//        Supplier supplier = supplierRepository.findByName(supplierName);
//        Rating ratingDB = ratingRepository1.findBySupplierAndGood(supplier,good);
//        if (ratingDB == null) {
//            Supplier supplierDB = supplierRepository.findByName(supplierName);
//            // товар именно купленный этим контрагентом
//            Good goodForSetRating = goodRepository
//                    .getGoodForRating(operationCurrent, supplierName, item);
//
//            if (supplierDB == null || !operationCurrent.equals("selling")
//                    || goodForSetRating == null) {
//                return null; // исключение
//            }
//
//            Rating ratingNew = new Rating(value, item, goodForSetRating, supplierDB);
//            ratingRepository1.save(ratingNew);
//            GoodCard goodCard = goodCardRepository.findByName(item);
//
//            double rating = goodCard.getRating();
//            double countValue = goodCard.getCountValue();
//
//            double countValueNew = countValue+1;
//            double ratingNewGoodCard = (rating+value)/countValueNew;
//
//            goodCard.setRating(ratingNewGoodCard);
//            goodCard.setCountValue(countValueNew);
//            goodCardRepository.save(goodCard);
//            return ratingNew;
//        }
//        else return null;
//    }
    @Transactional
    public RatingDTOForCustomer setRating1(RatingDTO ratingDTO) {

        String sellerName = ratingDTO.getSellerName();
        String goodName = ratingDTO.getGoodName();

        Good good = goodRepository.findByName(goodName);
        Supplier supplier = supplierRepository.findByName(sellerName);
        Rating ratingExistDB = ratingRepository1.findBySupplierAndGood(supplier, good);

        if (ratingExistDB == null) {
            // товар именно купленный этим контрагентом
            Good goodForSetRating = goodRepository
                    .getGoodForRating("selling", sellerName, goodName);

            if (supplier == null || goodForSetRating == null) {
                return null; // исключение
            }
            Rating rating = modelMapper.map(ratingDTO, Rating.class);
            rating.setGood(goodForSetRating);
            rating.setSupplier(supplier);

            ratingRepository1.save(rating);
            GoodCard goodCard = goodCardRepository.findByName(goodName);

            double ratingDB = goodCard.getRating();
            double countValue = goodCard.getCountValue();

            double countValueNew = countValue + 1;
            double value = ratingDTO.getValue();
            double ratingNewGoodCard = (ratingDB + value) / countValueNew;

            goodCard.setRating(ratingNewGoodCard);
            goodCard.setCountValue(countValueNew);

            goodCardRepository.save(goodCard);

            RatingDTOForCustomer ratingDTOForCustomer = modelMapper.map(rating,
                    RatingDTOForCustomer.class);
            ratingDTOForCustomer.setMessage("Your evaluation is " + value);
            return ratingDTOForCustomer;
        } else return null;
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
}
