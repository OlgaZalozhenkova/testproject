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
public class GoodDTOCustomer {

    private String name;
    int priceCurrent;
    int quantityCurrent;
    int totalSum;
    private SupplierDTO supplierDTO;

}
