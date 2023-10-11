package com.example.testproject.dto;

import com.example.testproject.models.Counterpart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GoodDTO {

    private String name;
    private int price;
    private int quantity;
    private String counterpartName;

}
