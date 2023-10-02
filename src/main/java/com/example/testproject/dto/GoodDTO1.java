package com.example.testproject.dto;

import com.example.testproject.models.Supplier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GoodDTO1 {

    private String name;
    int price;
    int quantity;
    Supplier supplier;

}
