package com.example.testproject.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodCardDTO {

    @NotEmpty(message = "Item name should not be empty")
    @Size(min = 2, max = 30)
    private String name;

    @DecimalMin(value = "0.1", message = "Supply price should be greater than 0.1")
    private double valueForSupply;

    @DecimalMin(value = "0.1", message = "Sell price should be greater than 0.1")
    private double ValueForSelling;

    @NotEmpty(message = "Category should not be empty")
    @Size(min = 2, max = 30)
    private String category;

    @NotEmpty(message = "Unit of measurement should not be empty")
    @Size(min = 1, max = 30)
    private String unitOfMeasurement;
}
