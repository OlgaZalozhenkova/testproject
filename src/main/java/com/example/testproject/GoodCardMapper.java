package com.example.testproject;

import com.example.testproject.dto.GoodCardDTO;
import com.example.testproject.models.GoodCard;

public class GoodCardMapper {

    public GoodCard map(GoodCardDTO dto) {
        return GoodCard.builder()
                .name(dto.getName())
                .priceSupply(dto.getValueForSupply())
                .priceSelling(dto.getValueForSelling())
                .availableQuantity(0)
                .sellQuantity(0)
                .rating(0)
                .countValue(0)
                .build();
    }
}
