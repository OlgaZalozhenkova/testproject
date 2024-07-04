package com.example.testproject.servicies;

import com.example.testproject.models.Counterpart;
import com.example.testproject.models.Good;
import com.example.testproject.repositories.CounterpartRepository;
import com.example.testproject.repositories.GoodRepository;
import com.example.testproject.util.GoodNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CounterpartServiceTest {

    @Mock
    private GoodRepository goodRepository;

    @Mock
    private CounterpartRepository counterpartRepository;

    @InjectMocks
    private CounterpartService counterpartService;

    private Good good;
    private List<Counterpart> counterparts;

    @BeforeEach
    public void setUp() {
        good = new Good();
        good.setName("TestGood");

        Counterpart counterpart1 = new Counterpart();
        counterpart1.setName("Counterpart1");

        Counterpart counterpart2 = new Counterpart();
        counterpart2.setName("Counterpart2");

        counterparts = Arrays.asList(counterpart1, counterpart2);
    }

    @Test
    public void testFindCounterpartsByGoodNameWhenGoodIsFoundThenReturnListOfCounterparts() {
        // Arrange
        when(goodRepository.findByName("TestGood")).thenReturn(good);
        when(counterpartRepository.findCounterpartsByGoodName("TestGood")).thenReturn(counterparts);

        // Act
        List<Counterpart> result = counterpartService.findCounterpartsByGoodName("TestGood");

        // Assert
        assertEquals(2, result.size());
        assertEquals("Counterpart1", result.get(0).getName());
        assertEquals("Counterpart2", result.get(1).getName());
    }

    @Test
    public void testFindCounterpartsByGoodNameWhenGoodIsNotFoundThenThrowGoodNotFoundException() {
        // Arrange
        when(goodRepository.findByName("NonExistentGood")).thenReturn(null);

        // Act & Assert
        assertThrows(GoodNotFoundException.class, () -> {
            counterpartService.findCounterpartsByGoodName("NonExistentGood");
        });
    }
}
