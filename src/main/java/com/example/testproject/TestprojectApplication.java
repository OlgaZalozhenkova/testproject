package com.example.testproject;

import com.example.testproject.dto.RatingDTO;
import com.example.testproject.mapper.RatingMapper;
import com.example.testproject.models.Rating;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TestprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestprojectApplication.class, args);
    }

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
//
//    @Bean
//    RatingMapper ratingMapper() {
//        return new RatingMapper();
//    }
}
