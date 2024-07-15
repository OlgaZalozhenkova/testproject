package com.example.testproject.servicies;

import com.example.testproject.models.Counterpart;
import com.example.testproject.models.Good;
import com.example.testproject.models.GoodOperation;
import com.example.testproject.models.OperationType;
import com.example.testproject.repositories.CounterpartRepository;
import com.example.testproject.repositories.GoodOperationRepository;
import com.example.testproject.util.CounterpartNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GoodOperationServiceTest {
    @Mock
    private GoodOperationRepository goodOperationRepository;
    @Mock
    private CounterpartRepository counterpartRepository;
    @InjectMocks
    private GoodOperationService goodOperationService;

    private List<GoodOperation> goodOperations;

    @Test
    public void findAllWhenFoundThenReturnListOfGoodOperations() {
        Good good1 = new Good();
        Counterpart counterpart1 = new Counterpart();
        Good good2 = new Good();
        Counterpart counterpart2 = new Counterpart();
        GoodOperation goodOperation1 = new GoodOperation("good1", OperationType.SUPPLY, 10, 1,
                "Smirnov", new Date(1111), 10, 1, good1, counterpart1);
        GoodOperation goodOperation2 = new GoodOperation("good2", OperationType.SUPPLY, 20, 2,
                "Ivanov", new Date(1111), 20, 2, good2, counterpart2);
        goodOperations = Arrays.asList(goodOperation1, goodOperation2);

        when(goodOperationRepository.findAll()).thenReturn(goodOperations);
        List<GoodOperation> result = goodOperationService.findAll();
        assertEquals(2, result.size());
        assertEquals(good1, result.get(0).getGood());
        assertEquals(good2, result.get(1).getGood());
        assertEquals(OperationType.SUPPLY, result.get(0).getOperationType());
        assertEquals(OperationType.SUPPLY, result.get(1).getOperationType());
    }

    @Test
    public void getGoodOperationByCounterpartNameWhenCounterPartFoundThenReturnListOfGoodOperations() {
        Counterpart counterpart1 = new Counterpart();
        counterpart1.setName("TestName");
        Good good1 = new Good();
        Good good2 = new Good();
        GoodOperation goodOperation1 = new GoodOperation("good1", OperationType.SUPPLY, 10, 1,
                "TestName", new Date(1111), 10, 1, good1, counterpart1);
        GoodOperation goodOperation2 = new GoodOperation("good2", OperationType.SUPPLY, 20, 2,
                "TestName", new Date(1111), 20, 2, good2, counterpart1);
        goodOperations = Arrays.asList(goodOperation1, goodOperation2);
        when(counterpartRepository.findByName("TestName")).thenReturn(counterpart1);
        when(goodOperationRepository.findByCounterpartName("TestName")).thenReturn(goodOperations);
        List<GoodOperation> result = goodOperationService.getGoodOperationByCounterpartName("TestName");
        assertEquals(2, result.size());
        assertEquals(good1, result.get(0).getGood());
        assertEquals(good2, result.get(1).getGood());
    }

    @Test
    public void getGoodOperationByCounterpartNameWhenCounterPartNoTFoundThenThrowCounterpartNotFoundException() {
        Counterpart counterpart1 = new Counterpart();
        counterpart1.setName("TestName");
        when(counterpartRepository.findByName("TestName")).thenReturn(null);
        assertThrows(CounterpartNotFoundException.class, () -> goodOperationService
                .getGoodOperationByCounterpartName("TestName"));
    }

    @Test
    public void getGoodOperationsByOperationTypeAndCounterpartNameWhenFoundThenReturnListOfGoodOperations() {
        Counterpart counterpart1 = new Counterpart();
        counterpart1.setName("TestName");
        Good good1 = new Good();
        Good good2 = new Good();
        GoodOperation goodOperation1 = new GoodOperation("good1", OperationType.SUPPLY, 10, 1,
                "TestName", new Date(1111), 10, 1, good1, counterpart1);
        GoodOperation goodOperation2 = new GoodOperation("good2", OperationType.SUPPLY, 20, 2,
                "TestName", new Date(1111), 20, 2, good2, counterpart1);
        goodOperations = Arrays.asList(goodOperation1, goodOperation2);

        when(counterpartRepository.findByName("TestName")).thenReturn(counterpart1);
        when(goodOperationRepository.getGoodOperationsByOperationTypeAndCounterpartName(OperationType.SUPPLY,
                "TestName")).thenReturn(Optional.ofNullable(goodOperations));
        Optional<List<GoodOperation>> result = goodOperationService.getGoodOperationsByOperationTypeAndCounterpartName(
                OperationType.SUPPLY, "TestName");
        if (result.get() != null) {
            assertEquals(goodOperations, result.get());
            assertEquals(good1, result.get().get(0).getGood());
            assertEquals(good2, result.get().get(1). getGood());
        }
    }
    @Test
    public void getGoodOperationsByOperationTypeAndCounterpartNameWhenCounterPartNoTFoundThenThrowCounterpartNotFoundException() {
        Counterpart counterpart1 = new Counterpart();
        counterpart1.setName("TestName");
        when(counterpartRepository.findByName("TestName")).thenReturn(null);
        assertThrows(CounterpartNotFoundException.class, () -> goodOperationService
                .getGoodOperationByCounterpartName("TestName"));
    }
}
