package com.example.testproject.servicies;

import com.example.testproject.dto.GoodDTO;
import com.example.testproject.dto.GoodOperationDTO;
import com.example.testproject.dto.GoodOperationSpecificationDTO;
import com.example.testproject.mapper.GoodMapper;
import com.example.testproject.models.*;
import com.example.testproject.repositories.CounterpartRepository;
import com.example.testproject.repositories.GoodCardRepository;
import com.example.testproject.repositories.GoodOperationRepository;
import com.example.testproject.repositories.GoodRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class GoodService {
    private final GoodRepository goodRepository;
    private final CounterpartRepository counterpartRepository;
    private final GoodOperationRepository goodOperationRepository;
    private final GoodCardRepository goodCardRepository;
    private final GoodMapper goodMapper;
    private  final GoodOperationSpecificationService goodOperationSpecificationService;

    @Transactional
    public Good supplyGood(GoodDTO goodDTO) {

        //проверка наличия карточки товара
        String goodNameForCheck = goodDTO.getName();
        GoodCard goodCard = goodCardRepository.findByName(goodNameForCheck);
        if (goodCard == null) {
            return null;
        }

        // проверка цены поставки
        int priceSupply = goodCard.getPriceSupply();
        if (goodDTO.getPrice() > priceSupply) {
            return null;
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

            goodDB = goodRepository.save(good);

            // сохранение количества в карточке товара
            // товар новый, операций не было
            goodCard.setAvailableQuantity(goodDB.getQuantity());

            // товар уже существует в БД
        } else {

            // присваиваю новые значения цены и количества товару в БД
            int quantityNew = goodDB.getQuantity() + goodDTO.getQuantity();
            int priceNew = (goodDB.getPrice() * goodDB.getQuantity()
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

        return goodDB;
    }

    @Transactional
    public Good sellGood(GoodDTO goodDTO) {

        Good goodDB = goodRepository.findByName(goodDTO.getName());
        if (goodDB == null) {
            return null;
        } else {

            int quantity = goodDTO.getQuantity();
            int quantityDB = goodDB.getQuantity();

            if (quantity > quantityDB) { // не хватает количества товаров
                return null;
            }
            String item = goodDTO.getName();
            int price = goodDTO.getPrice();
            GoodCard goodCard = goodCardRepository.findByName(item);
            int priceSelling = goodCard.getPriceSelling();
            if (price < priceSelling) {
                return null; //исключение
            }

            int sellQuantity = goodCard.getSellQuantity();

            int priceNew = 0;
            int priceDB = goodDB.getPrice();

            int salesIncome = ((goodDTO.getPrice() - goodDB.getPrice()) * goodDTO.getQuantity());

            int quantityNew = quantityDB - quantity;
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

            return goodDB;
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
    public GoodOperationDTO supplyGoods(List<GoodDTO> goodsDTO) {
        int totalSum = 0;
        for (GoodDTO goodDTO : goodsDTO
            //исключения, все или ничего
        ) {
            if (supplyGood(goodDTO) == null) {
                return null;
            }
            totalSum += goodDTO.getPrice() * goodDTO.getQuantity();
        }
        return new GoodOperationDTO(goodsDTO, totalSum);
    }

    @Transactional
    public GoodOperationDTO sellGoods(List<GoodDTO> goodsDTO) {
        int totalSum = 0;
        for (GoodDTO goodDTO : goodsDTO
            //исключения, все или ничего
        ) {
            if (sellGood(goodDTO) == null) {
                return null;
            }
            totalSum += goodDTO.getPrice() * goodDTO.getQuantity();
        }
        return new GoodOperationDTO(goodsDTO, totalSum);
    }


    public Optional<Integer> getGoodsAvailableQuantityByDate(String item, Date date) {
        return goodOperationRepository.getGoodOperationsByItemAndDate(item, date);
    }

    public  int getSalesIncomeForPeriod(Date dateFrom, Date dateTo) {
        List<GoodOperation> goodOperations = goodOperationRepository
                .getSalesIncomeForPeriod(dateFrom, dateTo);
        int salesIncome = 0;
        for (GoodOperation goodOperation : goodOperations
        ) {
            salesIncome += goodOperation.getSalesIncome();
        }
        return salesIncome;
    }

    public  int getSalesIncomeGoodForPeriod(String item, Date dateFrom, Date dateTo) {
        List<GoodOperation> goodOperations = goodOperationRepository
                .getSalesIncomeGoodForPeriod(item, dateFrom, dateTo);
        int salesIncome = 0;
        for (GoodOperation goodOperation : goodOperations
        ) {
            salesIncome += goodOperation.getSalesIncome();
        }
        return salesIncome;
       }

    public  int getSalesIncomeCounterpartGoodForPeriod(String counterpartName,
                                                       String item, Date dateFrom, Date dateTo) {
        List<GoodOperation> goodOperations = goodOperationRepository
                .getSalesIncomeCounterpartNameGoodForPeriod(counterpartName,item, dateFrom, dateTo);
        int salesIncome = 0;
        for (GoodOperation goodOperation : goodOperations
        ) {
            salesIncome += goodOperation.getSalesIncome();
        }
        return salesIncome;
    }

    public  int getSalesIncomeFilter(GoodOperationSpecificationDTO goodOperationSpecificationDTO) {
        List<GoodOperation> goodOperations = goodOperationSpecificationService.getOperationsToGetIncome(goodOperationSpecificationDTO);
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
        return goodRepository.getGoodsByCounterPartName(counterpartName);
    }
}

