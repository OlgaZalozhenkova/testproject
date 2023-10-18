package com.example.testproject.controllers;

import com.example.testproject.dto.GoodCardDTO;
import com.example.testproject.models.GoodCard;
import com.example.testproject.servicies.GoodCardService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/goodcard")
public class GoodCardController {

    private final GoodCardService goodCardService;

    // создать или изменить карточку товара
    @PostMapping("/create/change")
    public String createOrChangeGoodCard(@RequestBody GoodCardDTO goodCardDTO) {
        return goodCardService.createOrChangeGoodCard(goodCardDTO);
    }

    // карточка товара по наименованию товара
    // Optional
    @GetMapping("/{name}")
    public GoodCard findGoodCardByGoodName(@PathVariable("name") String name) {
        return goodCardService.findGoodCardByGoodName(name);
    }
}
