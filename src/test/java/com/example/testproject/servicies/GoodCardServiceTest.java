package com.example.testproject.servicies;

import com.example.testproject.dto.GoodCardDTO;
import com.example.testproject.mapper.GoodCardMapper;
import com.example.testproject.models.GoodCard;
import com.example.testproject.repositories.GoodCardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GoodCardServiceTest {
    @Mock
    private GoodCardRepository goodCardRepository;
    @Mock
    private GoodCardMapper goodCardMapper;

    private GoodCard goodCard;
    private GoodCardDTO goodCardDTO;

    @InjectMocks
    private GoodCardService goodCardService;

    @Test
    public void testFindGoodCardByGoodNameWhenGoodCardIsFoundThenReturnGoodCard() {
        goodCard = new GoodCard();
        goodCard.setName("TestGoodCard");
        when(goodCardRepository.findByName("TestGoodCard")).thenReturn(goodCard);
        GoodCard goodCardResult = goodCardService.findGoodCardByGoodName("TestGoodCard");
        assertEquals(goodCard, goodCardResult);
    }

    @Test
    public void testCreateOrChangeGoodCardWhenChangedThenReturnMessage() {

        goodCardDTO = new GoodCardDTO();
        goodCardDTO.setName("TestGoodCard");
        goodCardDTO.setValueForSelling(10);
        goodCardDTO.setValueForSupply(5);
        goodCardDTO.setCategory("testCategory");
        goodCardDTO.setUnitOfMeasurement("testUOM");

        goodCard = new GoodCard();
        goodCard.setName("TestGoodCard");
        goodCard.setPriceSelling(10);
        goodCard.setPriceSupply(5);
        goodCard.setCategory("testCategory");
        goodCard.setUnitOfMeasurement("testUOM");

        when(goodCardRepository.findByName("TestGoodCard")).thenReturn(goodCard);
        String message = goodCardService.createOrChangeGoodCard(goodCardDTO);
        assertEquals(goodCard.toString(), message);
    }

    @Test
    public void testCreateOrChangeGoodCardWhenCreatedThenReturnMessage() {
        goodCardDTO = new GoodCardDTO();
        goodCardDTO.setName("TestGoodCard");
        goodCardDTO.setValueForSelling(10);
        goodCardDTO.setValueForSupply(5);
        goodCardDTO.setCategory("testCategory");
        goodCardDTO.setUnitOfMeasurement("testUOM");

        when(goodCardRepository.findByName("TestGoodCard")).thenReturn(null);

        goodCard = new GoodCard();
        goodCard.setName("TestGoodCard");
        goodCard.setPriceSelling(10);
        goodCard.setPriceSupply(5);
        goodCard.setCategory("testCategory");
        goodCard.setUnitOfMeasurement("testUOM");

        when(goodCardMapper.map(goodCardDTO)).thenReturn(goodCard);
        String message = goodCardService.createOrChangeGoodCard(goodCardDTO);
        assertEquals(goodCard.toString(), message);
    }

    @Test
    public void testCreateOrChangeGoodCardWhenValueForSupplyIsGreaterThenValueForSellingThenThrowRuntimeException() {
        goodCardDTO = new GoodCardDTO();
        goodCardDTO.setName("TestGoodCard");
        goodCardDTO.setValueForSelling(5);
        goodCardDTO.setValueForSupply(10);
        goodCardDTO.setCategory("testCategory");
        goodCardDTO.setUnitOfMeasurement("testUOM");
        assertThrows(RuntimeException.class, () -> {
            goodCardService.createOrChangeGoodCard(goodCardDTO);
        });
    }
}
