package com.example.testproject.servicies;

import com.example.testproject.dto.*;
import com.example.testproject.mapper.GoodCardMapper;
import com.example.testproject.mapper.GoodMapper;
import com.example.testproject.models.*;
import com.example.testproject.repositories.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class GoodService {
    GoodRepository goodRepository;
    CounterpartRepository counterpartRepository;
    ModelMapper modelMapper;
    GoodOperationRepository goodOperationRepository;
    GoodCardRepository goodCardRepository;
    RatingRepository ratingRepository;
    GoodCardMapper goodCardMapper;
    GoodMapper goodMapper;

    @Transactional
    public String createOrChangeGoodCard(GoodCardDTO goodCardDTO) {

        GoodCard goodCardDB = goodCardRepository.findByName(goodCardDTO.getName());

        int valueForSupplyNew = goodCardDTO.getValueForSupply();
        int valueForSellingNew = goodCardDTO.getValueForSelling();

        if (goodCardDB == null) {
            GoodCard goodCard = goodCardMapper.map(goodCardDTO);
            goodCardRepository.save(goodCard);
            return goodCard.toString();
        } else {
            goodCardDB.setPriceSupply(valueForSupplyNew);
            goodCardDB.setPriceSelling(valueForSellingNew);
            return goodCardDB.toString();
        }
    }

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
                boolean isExist = counterpartsDB.contains(counterpartDB);

                //контрагент операции с данным товаром не проводил, в списке отсутствует
                if (!isExist) {
                    counterpartsDB.add(counterpartDB);
                    goodDB.setCounterparts(counterpartsDB);
                }
            }
        }
        // запись операции по товару в БД в табл. good_operations
        createGoodOperation(goodDTO,goodDB,
                counterpartDB,"supply");
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
                boolean isExist = counterpartsDB.contains(counterpartDB);

                //контрагент операции с данным товаром не проводил, в списке отсутствует
                if (!isExist) {
                    counterpartsDB.add(counterpartDB);
                    goodDB.setCounterparts(counterpartsDB);
                }
            }

            createGoodOperation(goodDTO,goodDB,
                    counterpartDB,"selling");

            return goodDB;
        }
    }

    @Transactional
    public void createGoodOperation(GoodDTO goodDTO,Good goodDB,
                                    Counterpart counterpartDB,String operationType) {
        GoodOperation goodOperation = GoodOperation.builder()
                .item(goodDTO.getName())
                .operationCurrent(operationType)
                .price(goodDTO.getPrice())
                .quantity(goodDTO.getQuantity())
                .counterpartName(goodDTO.getCounterpartName())
                .date(new Date())
                .priceDB(goodDB.getPrice())
                .quantityDB(goodDB.getQuantity())
                .good(goodDB)
                .counterpart(counterpartDB) // контрагент либо уже существовал в БД либо его сохранили как нового
                .build();
        goodOperationRepository.save(goodOperation);
    }

    @Transactional
    public GoodOperationDTO supplyGoods(List<GoodDTO> goodsDTO) {
        int totalSum = 0;
        for (GoodDTO goodDTO : goodsDTO
                //исключения
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
        ) {
            if (sellGood(goodDTO) == null) {
                return null;
            }
        }
        return new GoodOperationDTO(goodsDTO, totalSum);
    }

    @Transactional
    public RatingDTOForCustomer setRating(RatingDTO ratingDTO) {

        String counterpartName = ratingDTO.getCustomerName();
        String goodName = ratingDTO.getGoodName();

        Good good = goodRepository.findByName(goodName);
        Counterpart counterpart = counterpartRepository.findByName(counterpartName);
        Rating ratingDB = ratingRepository.findByCounterpartAndGood(counterpart, good);

        if (ratingDB == null) {
            // товар именно купленный этим контрагентом
            Good goodForSetRating = goodRepository
                    .getGoodForRating("selling", counterpartName, goodName);

            if (counterpart == null || goodForSetRating == null) {
                return null; // исключение
            }
            Rating rating = modelMapper.map(ratingDTO, Rating.class);
            rating.setGood(goodForSetRating);
            rating.setCounterpart(counterpart);
            rating.setChanged(false);

            ratingRepository.save(rating);
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
    public RatingDTOForCustomer changeRating(RatingDTO ratingDTO) {

        String sellerName = ratingDTO.getCustomerName();
        String goodName = ratingDTO.getGoodName();
        double valueDTO = ratingDTO.getValue();

        Counterpart counterpart = counterpartRepository.findByName(sellerName);
        Good good = goodRepository.findByName(goodName);
        Rating ratingExistDB = ratingRepository.findByCounterpartAndGood(counterpart, good);

        if (ratingExistDB == null || ratingExistDB.isChanged() || ratingExistDB.isDeleted()) {
            return null; //исключение
        } else {
            double valueDB = ratingExistDB.getValue();
            ratingExistDB.setValue(valueDTO);
            ratingExistDB.setChanged(true);
            GoodCard goodCard = goodCardRepository.findByName(goodName);
            double currentRating = goodCard.getRating();
            double countValue = goodCard.getCountValue();
            double ratingNew = (currentRating * countValue - valueDB + valueDTO) / countValue;
            goodCard.setRating(ratingNew);

            RatingDTOForCustomer ratingDTOForCustomer = new RatingDTOForCustomer(valueDTO,
                    "Your have changed evaluation to " + valueDTO);
            return ratingDTOForCustomer;
        }
    }

    @Transactional
    public RatingDTOForCustomer deleteRating(RatingDTO ratingDTO) {

        String sellerName = ratingDTO.getCustomerName();
        String goodName = ratingDTO.getGoodName();

        Counterpart counterpart = counterpartRepository.findByName(sellerName);
        Good good = goodRepository.findByName(goodName);
        Rating ratingExistDB = ratingRepository.findByCounterpartAndGood(counterpart, good);

        if (ratingExistDB == null || ratingExistDB.isDeleted()) {
            return null; //исключение
        } else {
            double valueDB = ratingExistDB.getValue();
            ratingExistDB.setDeleted(true);
            GoodCard goodCard = goodCardRepository.findByName(goodName);
            double currentRating = goodCard.getRating();
            double countValue = goodCard.getCountValue();
            double ratingNew = (currentRating * countValue - valueDB) / (countValue - 1);
            goodCard.setRating(ratingNew);
            goodCard.setCountValue(countValue - 1);

            RatingDTOForCustomer ratingDTOForCustomer = new RatingDTOForCustomer(valueDB,
                    "Your have removed your evaluation " + valueDB);
            return ratingDTOForCustomer;
        }
    }

    public String getGoodsAvailableQuantityByDate(String item, Date date) {

        int availableQuantity = goodOperationRepository
                .getGoodOperationsByItemAndDate(item, date);

        return "Available quantity of " + item + " is " + availableQuantity;
    }
}

