package com.example.testproject.servicies;

import com.example.testproject.models.Counterpart;
import com.example.testproject.repositories.GoodRepository;
import com.example.testproject.repositories.CounterpartRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CounterpartService {

    private  final CounterpartRepository counterpartRepository;
    private  final GoodRepository goodRepository;
    private final ModelMapper modelMapper;

    public List<Counterpart> findCounterpartsByGoodName(String name) {
        return counterpartRepository.findCounterpartsByGoodName(name);
    }

}
