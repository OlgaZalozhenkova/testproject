package com.example.testproject;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;

@SpringBootApplication
public class TestprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestprojectApplication.class, args);
    }

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    SimpleDateFormat simpleDateFormat() {
        return new SimpleDateFormat();
    }

}
