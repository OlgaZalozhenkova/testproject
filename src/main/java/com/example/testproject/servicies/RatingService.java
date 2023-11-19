package com.example.testproject.servicies;

import com.example.testproject.dto.RatingDTO;
import com.example.testproject.mapper.RatingMapper;
import com.example.testproject.models.*;
import com.example.testproject.repositories.CounterpartRepository;
import com.example.testproject.repositories.GoodCardRepository;
import com.example.testproject.repositories.GoodRepository;
import com.example.testproject.repositories.RatingRepository;
import com.example.testproject.util.CounterpartNotFoundException;
import com.example.testproject.util.GoodNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final GoodRepository goodRepository;
    private final CounterpartRepository counterpartRepository;
    private final GoodCardRepository goodCardRepository;
    private final RatingMapper ratingMapper;

    // нужен диапазон оценок
    @Transactional
    public String setRating(RatingDTO ratingDTO) {

        String counterpartName = ratingDTO.getCounterpartName();
        String goodName = ratingDTO.getGoodName();

        Good good = goodRepository.findByName(goodName);
        Counterpart counterpart = counterpartRepository.findByName(counterpartName);

        if (good == null) {
            throw new GoodNotFoundException();
        }
        if (counterpart == null) {
            throw new CounterpartNotFoundException();
        }

        Rating ratingDB = ratingRepository.findByCounterpartAndGood(counterpart, good);

//         оценивать товар можно только один раз
        if (ratingDB == null) {
            // товар для добавления рейтинга должен быть куплен этим контрагентом
            // можно оценивать только купленный товар
            Good goodForSetRating = goodRepository
                    .getGoodForSetRating(OperationType.SELLING, counterpartName, goodName);

            if (goodForSetRating == null) {
                throw new RuntimeException("This supplier has not " +
                        "bought this good!");
            }

            Rating rating = ratingMapper.ratingDTOToRating(ratingDTO);
            rating.setGood(goodForSetRating);
            rating.setCounterpart(counterpart);

            ratingRepository.save(rating);
            GoodCard goodCard = goodCardRepository.findByName(goodName);

            int countValue = goodCard.getCountValue() + 1;

            goodCard.setRating((goodCard.getRating() + ratingDTO.getValue()) / countValue);
            goodCard.setCountValue(countValue);

            return ratingDTO.toString();
        } else {
            throw new RuntimeException("Rating already exists!");
        }
    }

    @Transactional
    public String changeRating(RatingDTO ratingDTO) {

        String counterpartName = ratingDTO.getCounterpartName();
        String goodName = ratingDTO.getGoodName();

        Good good = goodRepository.findByName(goodName);
        Counterpart counterpart = counterpartRepository.findByName(counterpartName);

        if (good == null) {
            throw new GoodNotFoundException();
        }
        if (counterpart == null) {
            throw new CounterpartNotFoundException();
        }

        Rating ratingDB = ratingRepository.findByCounterpartAndGood(counterpart, good);

        if (ratingDB == null) {
            throw new RuntimeException("Rating you want to change doesn't exist!");
        }
        if (ratingDB.isDeleted()) {
            throw new RuntimeException("Rating is already deleted!");
        }
        if (ratingDB.isChanged()) {
            throw new RuntimeException("Rating is already changed!");
        }

        GoodCard goodCard = goodCardRepository.findByName(goodName);
        double currentRating = goodCard.getRating();
        double countValue = goodCard.getCountValue();
        goodCard.setRating((currentRating * countValue - ratingDB.getValue()
                + ratingDTO.getValue()) / countValue);
        ratingDB.setValue(ratingDTO.getValue());
        ratingDB.setChanged(true);
        goodCardRepository.save(goodCard);
        return ratingDTO.toString();
    }

    @Transactional
    public String deleteRating(RatingDTO ratingDTO) {

        String counterpartName = ratingDTO.getCounterpartName();
        String goodName = ratingDTO.getGoodName();

        Good good = goodRepository.findByName(goodName);
        Counterpart counterpart = counterpartRepository.findByName(counterpartName);

        if (good == null) {
            throw new GoodNotFoundException();
        }
        if (counterpart == null) {
            throw new CounterpartNotFoundException();
        }

        Rating ratingDB = ratingRepository.findByCounterpartAndGood(counterpart, good);

        if (ratingDB == null) {
            throw new RuntimeException("Rating you want to change doesn't exist!");
        }
        if (ratingDB.isDeleted()) {
            throw new RuntimeException("Rating is already deleted!");
        }

        ratingDB.setDeleted(true);
        GoodCard goodCard = goodCardRepository.findByName(goodName);
        double currentRating = goodCard.getRating();
        int countValue = goodCard.getCountValue();

        // в карточке существует единственная оценка этого покупателя
        if (countValue == 1) {
            goodCard.setCountValue(0);
            goodCard.setRating(0);
        } else {
            goodCard.setRating((currentRating * countValue - ratingDTO.getValue())
                    / (countValue - 1));
            goodCard.setCountValue(countValue - 1);
        }
        return ratingDTO.toString();
    }

    public Optional<Rating> findByGoodAndCounterpart(String goodName, String counterpartName) {
        return ratingRepository.findByGoodAndCounterpart(goodName, counterpartName);
    }

    public Rating findByGoodName(String goodName) {
        Rating rating = ratingRepository.findByGoodName(goodName);
        if (rating == null) {
            throw new RuntimeException("Rating with the good name doesn't exist!");
        }
        return  rating;
    }
}
