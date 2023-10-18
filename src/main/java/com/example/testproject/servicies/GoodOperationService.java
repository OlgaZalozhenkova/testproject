package com.example.testproject.servicies;

import com.example.testproject.models.GoodOperation;
import com.example.testproject.models.OperationType;
import com.example.testproject.repositories.GoodOperationRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class GoodOperationService {

    private final GoodOperationRepository goodOperationRepository;

    public List<GoodOperation> findAll() {
        return goodOperationRepository.findAll();
    }

    public List<GoodOperation> getGoodOperationsForPeriod(Date dateFrom, Date dateTo) {
        return goodOperationRepository.getGoodOperationsForPeriod(dateFrom, dateTo);
    }

    public List<GoodOperation> getOperationsByOperationTypeAndCounterpartNameAndPeriod
            (OperationType operationType, String counterpartName, Date dateFrom, Date dateTo) {
        return goodOperationRepository.getOperationsByOperationTypeAndCounterpartNameAndPeriod
                (operationType, counterpartName, dateFrom, dateTo);
    }

    public Optional<List<GoodOperation>> getGoodOperationByCounterpartName(String counterpartName) {
        return goodOperationRepository.findByCounterpartName(counterpartName);
    }

    public Optional<List<GoodOperation>> getGoodOperationsByOperationTypeAndCounterpartName
            (OperationType operationType, String counterpartName) {
        return goodOperationRepository.getGoodOperationsByOperationTypeAndCounterpartName
                (operationType, counterpartName);
    }
}
