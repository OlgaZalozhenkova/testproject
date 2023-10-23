package com.example.testproject.controllers;

import com.example.testproject.dto.RatingDTO;
import com.example.testproject.models.Rating;
import com.example.testproject.servicies.RatingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/rating")
public class RatingController {

    private final RatingService ratingService;

    // Рейтинги
    // поставить оценку можно только один раз
    @PostMapping("/set")
    public String setRating(@RequestBody RatingDTO ratingDTO) {
        return ratingService.setRating(ratingDTO);
    }

    // изменить оценку можно только один раз
    @PostMapping("/change")
    public String changeRating(@RequestBody RatingDTO ratingDTO) {
        return ratingService.changeRating(ratingDTO);
    }

    // удалить оценку можно только один раз
    @PostMapping("/delete")
    public String deleteRating(@RequestBody RatingDTO ratingDTO) {
        return ratingService.deleteRating(ratingDTO);
    }

    // аналитика по рейтингам
    // рейтинг из журнала рейтингов по наименованию товара и наименованию покупателя
    @GetMapping("/get")
    public Optional<Rating> findByGoodAndCounterpart(@RequestParam("goodName") String goodName,
                                                     @RequestParam("counterpartName") String counterpartName) {
        return ratingService.findByGoodAndCounterpart(goodName, counterpartName);
    }
}
