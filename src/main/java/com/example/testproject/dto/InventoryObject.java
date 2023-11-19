package com.example.testproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Map;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InventoryObject {
    Map<String,Double> goodsInFact;
}
