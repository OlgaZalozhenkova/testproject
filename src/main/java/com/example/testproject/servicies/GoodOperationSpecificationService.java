package com.example.testproject.servicies;

import com.example.testproject.dto.GoodOperationSpecificationDTO;
import com.example.testproject.models.GoodOperation;
import com.example.testproject.repositories.GoodOperationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import static com.example.testproject.servicies.specifications.GoodOperationSpecification.*;
import static org.springframework.data.jpa.domain.Specification.where;

@AllArgsConstructor
@Service
public class GoodOperationSpecificationService {

    private GoodOperationRepository goodOperationRepository;

    public List<GoodOperation> getOperationsByItemAndCounterpartAndOperationType
            (GoodOperationSpecificationDTO dto) {
        return goodOperationRepository.findAll(where(hasItem(dto.getItem()))
                .and(hasCounterpartName(dto.getCounterpartName()))
                .and(hasOperationType(dto.getOperationType())));
    }

    public List<GoodOperation> getOperationsToGetIncome
            (GoodOperationSpecificationDTO dto) {
        return goodOperationRepository.findAll(where(hasItem(dto.getItem()))
                .and(hasCounterpartName(dto.getCounterpartName()))
                .and(hasDateFrom(dto.getDateFrom()))
                .and(hasDateTo(dto.getDateTo())));
    }

}
