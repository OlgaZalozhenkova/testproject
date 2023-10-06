package com.example.testproject.servicies;

import com.example.testproject.repositories.GoodRepository;
import com.example.testproject.repositories.SupplierRepository;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Getter
@Setter
@Transactional(readOnly = true)
public class SupplierService {

    private  final SupplierRepository supplierRepository;
    private  final GoodRepository goodRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public SupplierService(SupplierRepository supplierRepository, GoodRepository goodRepository, ModelMapper modelMapper) {
        this.supplierRepository = supplierRepository;
        this.goodRepository = goodRepository;
        this.modelMapper = modelMapper;
    }
}
