package com.example.testproject.servicies;

import com.example.testproject.dto.GoodDTO;
import com.example.testproject.models.Good;
import com.example.testproject.models.Operation;
import com.example.testproject.models.Supplier;
import com.example.testproject.repositories.GoodRepository;
import com.example.testproject.repositories.OperationRepository;
import com.example.testproject.repositories.SupplierRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GoodService {
    GoodRepository goodRepository;
    SupplierRepository supplierRepository;
    OperationRepository operationRepository;
    ModelMapper modelMapper;

    @Autowired
    public GoodService(GoodRepository goodRepository, SupplierRepository supplierRepository, OperationRepository operationRepository, ModelMapper modelMapper) {
        this.goodRepository = goodRepository;
        this.supplierRepository = supplierRepository;
        this.operationRepository = operationRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public Good createGood(Good good) {
        Supplier supplier = good.getSuppliers().get(0);
        Operation operation = good.getOperations().get(0);
//        поставить в DB timestamp

        String nameNew = good.getName();
        Good goodDB = goodRepository.findByName(nameNew);
        if (goodDB == null) {
            operation.setName("supply");
            supplierRepository.save(supplier);
            operationRepository.save(operation);
            return goodRepository.save(good);
        } else {
            int priceDB = goodDB.getPrice();
            int quantityDB = goodDB.getQuantity();

            int price = good.getPrice();
            int quantity = good.getQuantity();

            int quantityNew = quantityDB + quantity;
            int priceNew = (priceDB * quantityDB + price * quantity) / quantityNew;

            goodDB.setQuantity(quantityNew);
            goodDB.setPrice(priceNew);

            List<Supplier> suppliersDB = goodDB.getSuppliers();
            Supplier supplierCurrent = good.getSuppliers().get(0);
            Supplier supplierDB = supplierRepository.findByName(supplierCurrent.getName());

            if (supplierDB == null) {
                suppliersDB.add(supplierCurrent);
                supplierRepository.saveAll(suppliersDB);
                goodDB.setSuppliers(suppliersDB);
            }
            List<Operation> operationsDB = goodDB.getOperations();
            operationsDB.add(good.getOperations().get(0));
            operationRepository.saveAll(operationsDB);
            goodDB.setOperations(operationsDB);
            return goodDB;
        }
    }

    @Transactional
    public Good createGoodDTO1(GoodDTO goodDTO) {
        Good good = modelMapper.map(goodDTO, Good.class);
        Supplier supplier = good.getSuppliers().get(0);
        Operation operation = good.getOperations().get(0);
        supplierRepository.save(supplier);
        operationRepository.save(operation);
        return goodRepository.save(good);
    }

}
