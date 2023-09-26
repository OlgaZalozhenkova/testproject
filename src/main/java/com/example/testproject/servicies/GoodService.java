package com.example.testproject.servicies;

import com.example.testproject.dto.GoodDTO;
import com.example.testproject.models.Good;
import com.example.testproject.models.GoodOperation;
import com.example.testproject.models.Operation;
import com.example.testproject.models.Supplier;
import com.example.testproject.repositories.GoodOperationRepository;
import com.example.testproject.repositories.GoodRepository;
import com.example.testproject.repositories.OperationRepository;
import com.example.testproject.repositories.SupplierRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class GoodService {
    GoodRepository goodRepository;
    SupplierRepository supplierRepository;
    OperationRepository operationRepository;
    ModelMapper modelMapper;
    GoodOperationRepository goodOperationRepository;

    @Autowired
    public GoodService(GoodRepository goodRepository, SupplierRepository supplierRepository, OperationRepository operationRepository, ModelMapper modelMapper, GoodOperationRepository goodOperationRepository) {
        this.goodRepository = goodRepository;
        this.supplierRepository = supplierRepository;
        this.operationRepository = operationRepository;
        this.modelMapper = modelMapper;
        this.goodOperationRepository = goodOperationRepository;
    }

    @Transactional
    public Good createGood(Good good) {

        Supplier supplier = good.getSuppliers().get(0);
        Operation operation = good.getOperations().get(0);
//        поставить в DB timestamp

        String nameForCheck = good.getName();
        Good goodDB = goodRepository.findByName(nameForCheck);
        if (goodDB == null) {
            operation.setName("supply");
            supplierRepository.save(supplier);
            operationRepository.save(operation);
            Good goodNew = goodRepository.save(good);
            createGoodOperation(good);
            return goodNew;
        } else {
            int priceDB = goodDB.getPrice();
            int quantityDB = goodDB.getQuantity();

            int price = good.getPrice();
            int quantity = good.getQuantity();

            int quantityNew = quantityDB + quantity;
            int priceNew = (priceDB * quantityDB + price * quantity) / quantityNew;

            goodDB.setQuantity(quantityNew);
            goodDB.setPrice(priceNew);

            List<Operation> operationsDB = goodDB.getOperations();
            operationsDB.add(good.getOperations().get(0));
            operationRepository.saveAll(operationsDB);
            goodDB.setOperations(operationsDB);

            List<Supplier> suppliersDB = goodDB.getSuppliers();
            Supplier supplierForCheck = good.getSuppliers().get(0);
            Supplier supplierDB = supplierRepository.findByName(supplierForCheck.getName());

/*          если товар существует,изменяю цену и количество,
            добавляю новую операцию в список Operation (нет поля goodOperations
            -> нет supplier
            -> нет supplier id),
            сохраняю поставщика, назначается id поставщика (нет goodOperations,
            он создается в конце
            -> нет связи по supplier id)

            */
            createGoodOperation1(good,goodDB,supplier);

            if (supplierDB == null) {
                suppliersDB.add(supplierForCheck);
                supplierRepository.saveAll(suppliersDB);
                goodDB.setSuppliers(suppliersDB);
            }

            return goodDB;
        }
    }

    @Transactional
    public void createGoodOperation(Good good) {
        String item = good.getName();
        String operationCurrent = "supply";
        int price = good.getPrice();
        int quantity = good.getQuantity();
        String supplierName = good.getSuppliers().get(0).getName();
        Supplier supplier = good.getSuppliers().get(0);
        Operation operation = good.getOperations().get(0);
        GoodOperation goodOperation = new GoodOperation(item, operationCurrent,
                price, quantity, supplierName, good, supplier, operation);
        goodOperationRepository.save(goodOperation);
    }

    @Transactional
    public void createGoodOperation1(Good good, Good goodDB, Supplier supplierDB) {
        String item = good.getName();
        String operationCurrent = "supply";
        int price = good.getPrice();
        int quantity = good.getQuantity();
        String supplierName = good.getSuppliers().get(0).getName();
        Operation operation = good.getOperations().get(0);
//        List<Operation> operations = goodDB.getOperations();
//        Operation operation = operations.get(operations.size()-1);
        GoodOperation goodOperation = new GoodOperation(item, operationCurrent,
                price, quantity, supplierName, goodDB, supplierDB, operation);
        goodOperationRepository.save(goodOperation);
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
