package com.example.testproject.dto;

import com.example.testproject.models.OperationType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class GoodOperationSpecificationDTO {
    private String item;
    private String counterpartName;
    @Enumerated(EnumType.STRING)
    private OperationType operationType;
    Date dateFrom;
    Date dateTo;

    public GoodOperationSpecificationDTO(String item, String counterpartName, OperationType operationType) {
        this.item = item;
        this.counterpartName = counterpartName;
        this.operationType = operationType;
    }

    public GoodOperationSpecificationDTO(String item, String counterpartName, Date dateFrom, Date dateTo) {
        this.item = item;
        this.counterpartName = counterpartName;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }
}
