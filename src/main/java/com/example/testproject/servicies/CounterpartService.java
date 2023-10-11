package com.example.testproject.servicies;

import com.example.testproject.repositories.GoodRepository;
import com.example.testproject.repositories.CounterpartRepository;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Getter
@Setter
@Transactional(readOnly = true)
public class CounterpartService {

    private  final CounterpartRepository counterpartRepository;
    private  final GoodRepository goodRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CounterpartService(CounterpartRepository counterpartRepository, GoodRepository goodRepository, ModelMapper modelMapper) {
        this.counterpartRepository = counterpartRepository;
        this.goodRepository = goodRepository;
        this.modelMapper = modelMapper;
    }
 }
