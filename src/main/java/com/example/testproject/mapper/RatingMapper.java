package com.example.testproject.mapper;

import com.example.testproject.dto.RatingDTO;
import com.example.testproject.models.Rating;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RatingMapper {
    Rating ratingDTOToRating(RatingDTO ratingDTO);
}
