package com.example.testproject.servicies;

import com.example.testproject.models.GoodOperation;
import com.example.testproject.models.OperationType;
import com.example.testproject.repositories.CounterpartRepository;
import com.example.testproject.repositories.GoodOperationRepository;
import com.example.testproject.util.CounterpartNotFoundException;
import com.example.testproject.util.DataNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class GoodOperationService {

    private final GoodOperationRepository goodOperationRepository;
    private final CounterpartRepository counterpartRepository;
    private final SimpleDateFormat simpleDateFormat;

    public List<GoodOperation> findAll() {
        List<GoodOperation> goodOperations = goodOperationRepository.findAll();
        if (goodOperations.isEmpty()) {
            throw new DataNotFoundException();
        } else {
            return goodOperations;
        }
    }

//    public List<GoodOperation> getGoodOperationsForPeriod
//            (String fromDate, String toDate) throws ParseException {
//        simpleDateFormat.applyPattern("yyyy/MM/dd");
//        Date dateFrom = simpleDateFormat.parse(fromDate);
//        Date dateTo = simpleDateFormat.parse(toDate);
//        List<GoodOperation> goodOperations = goodOperationRepository
//                .getGoodOperationsForPeriod(dateFrom, dateTo);
//
//        if (goodOperations.isEmpty()) {
//            throw new DataNotFoundException();
//        } else {
//            return goodOperations;
//        }
//    }

    public List<GoodOperation> getGoodOperationsForPeriod
            (Date dateFrom, Date dateTo) throws ParseException {
//        simpleDateFormat.applyPattern("yyyy/MM/dd");
//        Date dateFrom = simpleDateFormat.parse(fromDate);
//        Date dateTo = simpleDateFormat.parse(toDate);
        List<GoodOperation> goodOperations = goodOperationRepository
                .getGoodOperationsForPeriod(dateFrom, dateTo);

        if (goodOperations.isEmpty()) {
            throw new DataNotFoundException();
        } else {
            return goodOperations;
        }
    }

    public List<GoodOperation> getOperationsByOperationTypeAndCounterpartNameAndPeriod
            (OperationType operationType, String counterpartName, Date dateFrom, Date dateTo) {

        if (counterpartRepository.findByName(counterpartName) == null) {
            throw new CounterpartNotFoundException();
        } else {
            List<GoodOperation> goodOperations = goodOperationRepository.getOperationsByOperationTypeAndCounterpartNameAndPeriod
                    (operationType, counterpartName, dateFrom, dateTo);
            if (goodOperations.isEmpty()) {
                throw new DataNotFoundException();
            } else
                return goodOperations;
        }
    }

    public List<GoodOperation> getGoodOperationByCounterpartName(String counterpartName) {
        if (counterpartRepository.findByName(counterpartName) == null) {
            throw new CounterpartNotFoundException();
        } else {
            return goodOperationRepository.findByCounterpartName(counterpartName);
        }
    }

    public Optional<List<GoodOperation>> getGoodOperationsByOperationTypeAndCounterpartName
            (OperationType operationType, String counterpartName) {
        if (counterpartRepository.findByName(counterpartName) == null) {
            throw new CounterpartNotFoundException();
        } else {
            return goodOperationRepository.getGoodOperationsByOperationTypeAndCounterpartName
                    (operationType, counterpartName);
        }

    }
}
