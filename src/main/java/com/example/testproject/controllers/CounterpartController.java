package com.example.testproject.controllers;

import com.example.testproject.models.Counterpart;
import com.example.testproject.servicies.CounterpartService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/counterpart")
@Slf4j
public class CounterpartController {

    private final CounterpartService counterpartService;

    // аналитика по контрагентам
    // список контрагентов по наименованию товара
    @GetMapping("/goodname")
    public List<Counterpart> findCounterpartsByGoodName(@RequestParam("name") String name) {
        log.info("Received request to find counterparts by good name: {}", name);
        List<Counterpart> counterparts = counterpartService.findCounterpartsByGoodName(name);
        log.info("Found {} counterparts for good name: {}", counterparts.size(), name);
        return counterparts;
    }
}


