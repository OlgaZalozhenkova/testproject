package com.example.testproject.servicies;

import com.example.testproject.dto.GoodCardDTO;
import com.example.testproject.mapper.GoodCardMapper;
import com.example.testproject.models.GoodCard;
import com.example.testproject.repositories.GoodCardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class GoodCardService {

    private final GoodCardRepository goodCardRepository;
    private  final GoodCardMapper goodCardMapper;

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

    public GoodCard findGoodCardByGoodName(String name) {
        return goodCardRepository.findByName(name);
    }
}
