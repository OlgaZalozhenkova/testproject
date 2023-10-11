package com.example.testproject.mapper;

import com.example.testproject.dto.GoodDTO;
import com.example.testproject.models.Good;
import org.springframework.stereotype.Component;

@Component
public class GoodMapper {
    public Good map(GoodDTO goodDTO) {
        return Good.builder().
                name(goodDTO.getName())
                .price(goodDTO.getPrice())
                .quantity(goodDTO.getQuantity())
                        .build();
    }
}
