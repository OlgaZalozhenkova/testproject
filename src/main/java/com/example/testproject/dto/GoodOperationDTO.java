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
public class GoodOperationDTO {
    private List<GoodDTO> items;
    private double totalSum;

    @Override
    public String toString() {
        return "GoodOperationDTO{" +
                "items=" + items +
                ", totalSum=" + totalSum +
                '}';
    }
}
