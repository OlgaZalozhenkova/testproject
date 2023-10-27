package com.example.testproject.servicies;

import com.example.testproject.dto.GoodCardDTO;
import com.example.testproject.mapper.GoodCardMapper;
import com.example.testproject.models.GoodCard;
import com.example.testproject.repositories.GoodCardRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Service
@AllArgsConstructor
public class GoodCardService {

    private final GoodCardRepository goodCardRepository;
    private final GoodCardMapper goodCardMapper;

    @Transactional
    public String createOrChangeGoodCard(GoodCardDTO goodCardDTO) {

        GoodCard goodCardDB = goodCardRepository.findByName(goodCardDTO.getName());

        double valueForSupplyNew = goodCardDTO.getValueForSupply();
        double valueForSellingNew = goodCardDTO.getValueForSelling();

        if (valueForSupplyNew > valueForSellingNew) {
            throw new RuntimeException("Price for selling should be greater " +
                    "than price for supply!");
        }

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

    public GoodCard findGoodCardByGoodName(String name) {
        return goodCardRepository.findByName(name);
    }
}
