package com.example.testproject.servicies;

import com.example.testproject.dto.GoodDTO;
import com.example.testproject.mapper.GoodMapper;
import com.example.testproject.models.*;
import com.example.testproject.repositories.CounterpartRepository;
import com.example.testproject.repositories.GoodCardRepository;
import com.example.testproject.repositories.GoodOperationRepository;
import com.example.testproject.repositories.GoodRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GoodServiceTest {
    @Mock
    private GoodRepository goodRepository;
    @Mock
    private CounterpartRepository counterpartRepository;
    @Mock
    private GoodOperationRepository goodOperationRepository;
    @Mock
    private GoodCardRepository goodCardRepository;
//    @Mock
//    private GoodMapper goodMapper;
//    @Mock
//    private GoodOperationSpecificationService goodOperationSpecificationService;
    @InjectMocks
    private GoodService goodService;

    @Test
    public void supplyGoodTest() {
        GoodDTO goodDTO = new GoodDTO();
        goodDTO.setName("TestName");
        goodDTO.setPrice(6);
        goodDTO.setCounterpartName("CounterpartTestName");
        goodDTO.setQuantity(2);

        GoodCard goodCard = new GoodCard();
        goodCard.setName("TestName");
        goodCard.setPriceSupply(10);

        Counterpart counterpart = new Counterpart();
        counterpart.setName("CounterpartTestName");
        List<Counterpart> counterpartList = new ArrayList<>();
        counterpartList.add(counterpart);

        Good good = new Good();
        good.setName("TestName");
        good.setPrice(6);
        good.setQuantity(1);
        good.setCounterparts(counterpartList);

        GoodOperation goodOperation = new GoodOperation(1,"TestName", OperationType.SUPPLY, 6,2,
                "CounterpartTestName", new Date(),6,3,0,good,counterpart);

        when(goodCardRepository.findByName(goodDTO.getName())).thenReturn(goodCard);
        when(goodRepository.findByName("TestName")).thenReturn(good);
        when(counterpartRepository.findByName(goodDTO.getCounterpartName())).thenReturn(counterpart);

        when(goodOperationRepository.save(goodOperation)).thenReturn(goodOperation);
//        when(goodService.createGoodOperation(goodDTO, good,
//                counterpart, OperationType.SUPPLY)).thenReturn(goodOperation);
        goodService.supplyGood(goodDTO);// ничего не возвращает
        assertEquals(good, goodOperation.getGood());
    }
}
