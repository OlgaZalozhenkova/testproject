package com.example.testproject.mapper;

import com.example.testproject.dto.GoodCardDTO;
import com.example.testproject.models.GoodCard;
import org.springframework.stereotype.Component;

@Component
public class GoodCardMapper {

    public GoodCard map(GoodCardDTO dto) {
        return GoodCard.builder()
                .name(dto.getName())
                .category(dto.getCategory())
                .unitOfMeasurement(dto.getUnitOfMeasurement())
                .priceSupply(dto.getValueForSupply())
                .priceSelling(dto.getValueForSelling())
                .availableQuantity(0)
                .sellQuantity(0)
                .rating(0)
                .countValue(0)
                .build();
    }
}
