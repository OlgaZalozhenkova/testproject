package com.example.testproject.servicies;

import com.example.testproject.models.Counterpart;
import com.example.testproject.repositories.GoodRepository;
import com.example.testproject.repositories.CounterpartRepository;
import com.example.testproject.util.GoodNotFoundException;
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

    private final CounterpartRepository counterpartRepository;
    private final GoodRepository goodRepository;

    public List<Counterpart> findCounterpartsByGoodName(String name) {
        if (goodRepository.findByName(name) == null) {
            throw new GoodNotFoundException();
        } else {
            return counterpartRepository.findCounterpartsByGoodName(name);
        }
    }
}
