package com.example.testproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoodDTO {
    private String name;
    private double price;
    private double quantity;
    private String counterpartName;

//    @Override
//    public String toString() {
//        return "GoodDTO{" +
//                "name='" + name + '\'' +
//                ", price=" + price +
//                ", quantity=" + quantity +
//                '}';
//    }
}
