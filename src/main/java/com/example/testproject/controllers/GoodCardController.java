package com.example.testproject.controllers;

import com.example.testproject.dto.GoodCardDTO;
import com.example.testproject.models.GoodCard;
import com.example.testproject.servicies.GoodCardService;
import com.example.testproject.util.DataNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/goodcard")
public class GoodCardController {

    private final GoodCardService goodCardService;

    // создать или изменить карточку товара
    @PostMapping("/create/change")
    public String createOrChangeGoodCard(@RequestBody @Valid GoodCardDTO goodCardDTO
            , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors
            ) {
                errorMsg.append(error.getField()).append("-")
                        .append(error.getDefaultMessage()).append(";");
            }
            throw new RuntimeException(errorMsg.toString());
        }
        return goodCardService.createOrChangeGoodCard(goodCardDTO);
    }

    // карточка товара по наименованию товара
    // Optional
    @GetMapping("/{name}")
    public GoodCard findGoodCardByGoodName(@PathVariable("name") String name) {
        GoodCard goodCard = goodCardService.findGoodCardByGoodName(name);
        if (goodCard != null) {
            return goodCard;
        } else throw new DataNotFoundException();
    }
}
