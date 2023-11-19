package com.example.testproject.servicies;

import com.example.testproject.dto.*;
import com.example.testproject.mapper.GoodMapper;
import com.example.testproject.models.*;
import com.example.testproject.repositories.CounterpartRepository;
import com.example.testproject.repositories.GoodCardRepository;
import com.example.testproject.repositories.GoodOperationRepository;
import com.example.testproject.repositories.GoodRepository;
import com.example.testproject.util.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class GoodService {
    private final GoodRepository goodRepository;
    private final CounterpartRepository counterpartRepository;
    private final GoodOperationRepository goodOperationRepository;
    private final GoodCardRepository goodCardRepository;
    private final GoodMapper goodMapper;
    private final GoodOperationSpecificationService goodOperationSpecificationService;

    @Transactional
    public void supplyGood(GoodDTO goodDTO) {

        //проверка наличия карточки товара
        String goodNameForCheck = goodDTO.getName();
        GoodCard goodCard = goodCardRepository.findByName(goodNameForCheck);
        if (goodCard == null) {
            throw new GoodCardNotFoundException();
        }

        // проверка цены поставки
        double priceSupply = goodCard.getPriceSupply();
        if (goodDTO.getPrice() > priceSupply) {
            throw new RuntimeException("Price doesn't match good card!");
        }

        // проверка наличия товара в БД
        Good goodDB = goodRepository.findByName(goodNameForCheck);

        // проверка наличия контрагента в БД
        Counterpart counterpartDB = counterpartRepository.findByName(goodDTO.getCounterpartName());

        // товар абсолютно новый
        if (goodDB == null) {

            Good good = goodMapper.map(goodDTO);

            // назначаю товару пустой список покупателей и поставщиков
            // поскольку товар абсолютно новый, то его никто не поставлял и не покупал
            // поставщик может присутствовать в БД, но привязанный к другому товару/товарам
            List<Counterpart> counterparts = new ArrayList<>();


            // контрагент абсолютно новый
            if (counterpartDB == null) {
                counterpartDB = counterpartRepository
                        .save(new Counterpart(goodDTO.getCounterpartName()));
                counterparts.add(counterpartDB);
                good.setCounterparts(counterparts);

                // контрагент уже существует БД, но к данному товару не привязан
            } else {
                counterparts.add(counterpartDB);
                good.setCounterparts(counterparts);
            }

            // назначаю карточку товара
            // товар абсолютно новый  и карточка существует, но еще не привязана
            good.setGoodCard(goodCard);
            good.setCategory(goodCard.getCategory());
            good.setUnitOfMeasurement(goodCard.getUnitOfMeasurement());

            goodDB = goodRepository.save(good);

            // сохранение количества в карточке товара
            // товар новый, операций не было
            goodCard.setAvailableQuantity(goodDB.getQuantity());

            // товар уже существует в БД
        } else {

            // присваиваю новые значения цены и количества товару в БД
            double quantityNew = goodDB.getQuantity() + goodDTO.getQuantity();
            double priceNew = (goodDB.getPrice() * goodDB.getQuantity()
                    + goodDTO.getPrice() * goodDTO.getQuantity())
                    / (goodDB.getQuantity() + goodDTO.getQuantity());

            goodDB.setQuantity(quantityNew);
            goodDB.setPrice(priceNew);

            // обновление количества в карточке товара
            goodCard.setAvailableQuantity(quantityNew);

            // у товара список контрагентов не пустой, так как товар существует
            // и назначен контрагент/контрагенты по предыдущим операциям
            List<Counterpart> counterpartsDB = goodDB.getCounterparts();

            // контрагент абсолютно новый, операции с данным товаром ранее не проводил
            if (counterpartDB == null) {
                counterpartDB = counterpartRepository
                        .save(new Counterpart(goodDTO.getCounterpartName()));
                counterpartsDB.add(counterpartDB);
                goodDB.setCounterparts(counterpartsDB);

                // контрагент существует в БД
            } else {

                // проверка проведения существующим контрагентом операций с данным товаром
                //контрагент операции с данным товаром не проводил, в списке отсутствует
                if (!counterpartsDB.contains(counterpartDB)) {
                    counterpartsDB.add(counterpartDB);
                    goodDB.setCounterparts(counterpartsDB);
                }
            }
        }
        // запись операции по товару в БД в табл. good_operations
        createGoodOperation(goodDTO, goodDB,
                counterpartDB, OperationType.SUPPLY);
    }

    @Transactional
    public void sellGood(GoodDTO goodDTO) {

        Good goodDB = goodRepository.findByName(goodDTO.getName());
        if (goodDB == null) {
            throw new GoodNotFoundException();
        } else {

            double quantity = goodDTO.getQuantity();
            double quantityDB = goodDB.getQuantity();

            if (quantity > quantityDB) { // не хватает количества товаров
                throw new RuntimeException("Quantity in stock is not enough!");
            }
            String item = goodDTO.getName();
            double price = goodDTO.getPrice();
            GoodCard goodCard = goodCardRepository.findByName(item);
            double priceSelling = goodCard.getPriceSelling();
            if (price < priceSelling) {
                throw new RuntimeException("Price doesn't match good card!");
            }

            double sellQuantity = goodCard.getSellQuantity();

            double priceNew = 0;
            double priceDB = goodDB.getPrice();

            double salesIncome = ((goodDTO.getPrice() - goodDB.getPrice()) * goodDTO.getQuantity());

            double quantityNew = quantityDB - quantity;
            if (quantityNew != 0) {
                priceNew = (priceDB * quantityDB - priceDB * quantity) / quantityNew;
            } // если количество 0, то цена 0 остается
            goodDB.setQuantity(quantityNew);// для количества > 0 и количества = 0
            goodDB.setPrice(priceNew);// для количества > 0 и количества = 0

            goodCard.setAvailableQuantity(quantityNew);
            goodCard.setSellQuantity(sellQuantity + quantity);

            List<Counterpart> counterpartsDB = goodDB.getCounterparts();
            String counterpartName = goodDTO.getCounterpartName();
            Counterpart counterpartDB = counterpartRepository.findByName(counterpartName);

            if (counterpartDB == null) {
                counterpartDB = counterpartRepository.save(new Counterpart(goodDTO.getCounterpartName()));
                counterpartsDB.add(counterpartDB);
                goodDB.setCounterparts(counterpartsDB);

            } else {
                // проверка проведения существующим контрагентом операций с данным товаром
                //контрагент операции с данным товаром не проводил, в списке отсутствует
                if (!counterpartsDB.contains(counterpartDB)) {
                    counterpartsDB.add(counterpartDB);
                    goodDB.setCounterparts(counterpartsDB);
                }
            }

            createGoodOperation(goodDTO, goodDB,
                    counterpartDB, OperationType.SELLING).setSalesIncome(salesIncome);
            goodDB.setSalesIncome(goodDB.getSalesIncome() + salesIncome);
        }
    }

    @Transactional
    public GoodOperation createGoodOperation(GoodDTO goodDTO, Good goodDB,
                                             Counterpart counterpartDB, OperationType operationType) {
        GoodOperation goodOperation = GoodOperation.builder()
                .item(goodDTO.getName())
                .operationType(operationType)
                .price(goodDTO.getPrice())
                .quantity(goodDTO.getQuantity())
                .counterpartName(goodDTO.getCounterpartName())
                .date(new Date())
                .priceDB(goodDB.getPrice())
                .quantityDB(goodDB.getQuantity())
                .good(goodDB)
                .counterpart(counterpartDB) // контрагент либо уже существовал в БД либо его сохранили как нового
                .build();
        return goodOperationRepository.save(goodOperation);
    }

    @Transactional
    public GoodOperationDTO supplyGoods(GoodsObject goodsObject) {
        List<GoodDTO> goodDTOs = goodsObject.getGoodDTOs();
        int totalSum = 0;
        for (GoodDTO goodDTO : goodDTOs) {
            supplyGood(goodDTO);
            totalSum += goodDTO.getPrice() * goodDTO.getQuantity();
        }
        return new GoodOperationDTO(goodDTOs, totalSum);
    }

    @Transactional
    public GoodOperationDTO sellGoods(GoodsObject goodsObject) {
        List<GoodDTO> goodDTOs = goodsObject.getGoodDTOs();
        int totalSum = 0;
        for (GoodDTO goodDTO : goodDTOs) {
            sellGood(goodDTO);
            totalSum += goodDTO.getPrice() * goodDTO.getQuantity();
        }
        return new GoodOperationDTO(goodDTOs, totalSum);
    }


    public Optional<Double> getGoodsAvailableQuantityByDate(String item, Date date) {
        if (goodRepository.findByName(item) == null) {
            throw new GoodNotFoundException();
        } else return goodOperationRepository.getGoodOperationsByItemAndDate(item, date);
    }

    public double getSalesIncomeFilter(GoodOperationSpecificationDTO goodOperationSpecificationDTO) {
        List<GoodOperation> goodOperations = goodOperationSpecificationService
                .getOperationsToGetIncome(goodOperationSpecificationDTO);
        int salesIncome = 0;
        for (GoodOperation goodOperation : goodOperations
        ) {
            salesIncome += goodOperation.getSalesIncome();
        }
        return salesIncome;
    }

    public List<Good> findAll() {
        return goodRepository.findAll();
    }

    public List<Good> getGoodsByCounterPartName(String counterpartName) {
        if (counterpartRepository.findByName(counterpartName) == null) {
            throw new CounterpartNotFoundException();
        }
        return goodRepository.getGoodsByCounterPartName(counterpartName);
    }

    @Transactional
    public String makeInventory(InventoryObject inventoryObject) {
        Map<String, Double> goodsInFact = inventoryObject.getGoodsInFact();
        List<Good> goods = goodRepository.findAll();
        Map<String, Double> goodsInAccounting = new HashMap();

        for (Good good : goods
        ) {
            if (good.getQuantity() != 0) {
                goodsInAccounting.put(good.getName(), good.getQuantity());
            }
        }

        if (!goodsInFact.equals(goodsInAccounting)) {
            Map<String, Double> corrections = new HashMap<>();
            Double quantityInAccounting;
            Double quantityInFact;
            for (Map.Entry entry : goodsInFact.entrySet()) {
                quantityInAccounting = goodsInAccounting.get(entry.getKey());
                // не проверка на 0 (это учтено выше), а проверка существования объекта
                // выявление объектов, которые есть в инв., а в Map нет(в БД они могут быть с
                // количеством 0
                if (quantityInAccounting != null) {
                    quantityInFact = (Double) entry.getValue();
                    if (quantityInFact > quantityInAccounting |
                            quantityInFact < quantityInAccounting) {
                        corrections.put((String) entry.getKey(), quantityInFact);
                    }
                    goodsInAccounting.remove(entry.getKey());
                    // в Map нет, а БД есть с количеством 0
                    // те позиции, кот.передали в инв. уже когда-то приходовались и есть карточка товара
                } else {
                    corrections.put((String) entry.getKey(), (Double) entry.getValue());
                }
            }
            if (!goodsInAccounting.isEmpty()) {
                for (Map.Entry entry : goodsInAccounting.entrySet()
                ) {
                    corrections.put((String) entry.getKey(), 0.0);
                }
            }

            createGoodOperationInventory(corrections);

            // назначаем новое количество товару
            for (Map.Entry entry : corrections.entrySet()
            ) {
                goodRepository.findByName((String) entry.getKey())
                        .setQuantity((Double) entry.getValue());
            }
            return corrections.toString();
        }
        return new HashMap<>().toString();
    }

    @Transactional
    public void createGoodOperationInventory(Map<String, Double> corrections) {
        for (Map.Entry entry : corrections.entrySet()
        ) {
            // из логики приложения good всегда будет найден
            Good good = goodRepository.findByName((String) entry.getKey());
            Double quantityInAccounting = good.getQuantity();
            Double quantityInFact = (Double) entry.getValue();
            Double quantityToCorrect;
            Counterpart counterpartInventory;
            counterpartInventory = counterpartRepository.findByName("Inventory");
            if (counterpartInventory == null) {
                counterpartInventory = new Counterpart("Inventory");
                counterpartRepository.save(counterpartInventory);
            }

            // в списке переданы позиции, кот. отличаются по кол-ву от БД и их новые количества
                quantityToCorrect = quantityInFact - quantityInAccounting;

            GoodOperation goodOperation = GoodOperation.builder()
                    .item((String) entry.getKey())
                    .operationType(OperationType.INVENTORY)
                    .price(0.0)
                    .quantity(quantityToCorrect)
                    .counterpartName("Inventory")
                    .date(new Date())
                    .priceDB(good.getPrice())
                    .quantityDB(quantityInFact)
                    .good(good)
                    .counterpart(counterpartInventory) // контрагент либо уже существовал в БД либо его сохранили как нового
                    .build();
            goodOperationRepository.save(goodOperation);
        }
    }

    public List<Good> findByCategory(String category) {
       return goodRepository.findByCategory(category);
    }

    public Good findByName(String name) {
        Good good = goodRepository.findByName(name);
        if (good == null) {
            throw new GoodNotFoundException();
        } else {
            return good;
        }
    }
}


