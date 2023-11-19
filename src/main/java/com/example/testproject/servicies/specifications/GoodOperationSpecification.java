package com.example.testproject.servicies.specifications;

import com.example.testproject.models.GoodOperation;
import com.example.testproject.models.OperationType;
import org.springframework.data.jpa.domain.Specification;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;

public class GoodOperationSpecification {

    public static Specification<GoodOperation> hasItem(String item) {
        if (item != null && !item.isEmpty()) {
            return (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("item"), item);
        }
        return null;
    }
    public static Specification<GoodOperation> hasCounterpartName(String counterpartName) {
        if (counterpartName != null && !counterpartName.isEmpty()) {
            return (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("counterpartName"), counterpartName);
        }
        return null;
    }
    public static Specification<GoodOperation> hasOperationType(OperationType operationType) {
        if (operationType != null) {
            return (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("operationType"), operationType);
        }
        return null;
    }

    public static Specification<GoodOperation> hasDateFrom(Date dateFrom) {
        if (dateFrom != null) {
            return (root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("date"), dateFrom);
        }
        return null;
    }

//    public static Specification<GoodOperation> hasDateTo(Date dateTo) {
//        if (dateTo != null) {
//            return (root, query, criteriaBuilder) ->
//                    criteriaBuilder.lessThanOrEqualTo(root.get("date"), dateTo);
//        }
//        return null;
//    }

    public static Specification<GoodOperation> hasDateTo(Date dateTo) {
        String date = "2023/10/25";
        Date date1 = new Date(date);
        System.out.println(date1.toString());
        if (dateTo != null) {
            return (root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("date"), dateTo);
        }
        return null;
    }
}
