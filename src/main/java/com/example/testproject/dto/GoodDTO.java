package com.example.testproject.dto;

import com.example.testproject.models.Operation;
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
public class GoodDTO {

   private String name;
   private List<Supplier> suppliers;
   private List<Operation> operations;

    public GoodDTO(String name) {
        this.name = name;
    }
}
