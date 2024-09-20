package com.example.testproject.servicies;

import com.example.testproject.models.Counterpart;
import com.example.testproject.models.Good;
import com.example.testproject.repositories.CounterpartRepository;
import com.example.testproject.repositories.GoodRepository;
import com.example.testproject.util.GoodNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CounterpartServiceTest1 {

    @Mock
    private GoodRepository goodRepository;

    @Mock
    private CounterpartRepository counterpartRepository;

    @InjectMocks
    private CounterpartService counterpartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindCounterpartsByGoodNameGoodExists() {
        // Arrange
        String goodName = "Test Good";
        Good good = new Good();
        good.setName(goodName);

        List<Counterpart> counterparts = List.of(new Counterpart(), new Counterpart());

        when(goodRepository.findByName(goodName)).thenReturn(good);
        when(counterpartRepository.findCounterpartsByGoodName(goodName)).thenReturn(counterparts);

        // Act
        List<Counterpart> result = counterpartService.findCounterpartsByGoodName(goodName);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(goodRepository, times(1)).findByName(goodName);
        verify(counterpartRepository, times(1)).findCounterpartsByGoodName(goodName);
    }

    @Test
    void testFindCounterpartsByGoodNameGoodDoesNotExist() {
        // Arrange
        String goodName = "Nonexistent Good";
        when(goodRepository.findByName(goodName)).thenReturn(null);

        // Act & Assert
        assertThrows(GoodNotFoundException.class, () -> counterpartService.findCounterpartsByGoodName(goodName));

        verify(goodRepository, times(1)).findByName(goodName);
        verify(counterpartRepository, never()).findCounterpartsByGoodName(anyString());
    }
}

