package com.example.testproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoodDTOOperation1 {
    private List<GoodDTO1> items;
    private int totalSum;
}
