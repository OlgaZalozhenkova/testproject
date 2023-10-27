package com.example.testproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingDTO {
    private int value;
    private String goodName;
    private String counterpartName;

    @Override
    public String toString() {
        return "RatingDTO{" +
                "value=" + value +
                ", goodName='" + goodName + '\'' +
                ", counterpartName='" + counterpartName + '\'' +
                '}';
    }
}
