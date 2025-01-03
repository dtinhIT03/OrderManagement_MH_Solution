package com.example.demo.repository.criteria;

import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.function.Consumer;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchCriteria implements Consumer<SearchCriteria> {
    private CriteriaBuilder criteriaBuilder;//sử dụng để tạo câu truy vấn động
    private Predicate predicate;//đại diện cho điều kiện 1 câu truy vấn
    private Root root;//đại diện cho thực thể chính trong câu truy vấn

    //accept dùng để thực hiện 1 hành động nào đó trên Order
//    @Override
//    public void accept(SearchCriteria searchCriteria) {
//        //trong đây sẽ kiểm tra các operation : ,>,<
//        if(searchCriteria.getOperation().equalsIgnoreCase(">")){
//            predicate =  criteriaBuilder.and(predicate,criteriaBuilder.greaterThan(root.get(searchCriteria.getName()),searchCriteria.getValue().toString()));
//        }else if(searchCriteria.getOperation().equalsIgnoreCase("<")){
//            predicate =  criteriaBuilder.and(predicate,criteriaBuilder.lessThan(root.get(searchCriteria.getName()),searchCriteria.getValue().toString()));
//
//        }else if(searchCriteria.getOperation().equalsIgnoreCase(":")){
//            if(root.get(searchCriteria.getName()).getJavaType() == String.class){
//                predicate =  criteriaBuilder.and(predicate,criteriaBuilder.like(root.get(searchCriteria.getName()),"%"+searchCriteria.getValue().toString()+"%"));
//            }
//            else if (root.get(searchCriteria.getName()).getJavaType().isEnum()) {
//                // Xử lý cho Enum
//                Class enumClass = root.get(searchCriteria.getName()).getJavaType();
//                Enum enumValue = Enum.valueOf(enumClass, searchCriteria.getValue().toString());
//                predicate = criteriaBuilder.and(predicate,
//                        criteriaBuilder.equal(root.get(searchCriteria.getName()), enumValue));
//            }
//            else{
//                predicate =  criteriaBuilder.and(predicate,criteriaBuilder.equal(root.get(searchCriteria.getName()),"%"+searchCriteria.getValue().toString()+"%"));
//            }
//
//        }
//
//    }

    @Override
    public void accept(SearchCriteria criteria) {
        switch (criteria.getOperation().toLowerCase()) {
            case ">" -> predicate = handleGreaterThan(criteria);
            case "<" -> predicate = handleLessThan(criteria);
            case ":" -> predicate = handleEquals(criteria);
            default -> throw new IllegalArgumentException("Operation not supported: " + criteria.getOperation());
        }
    }

    private Predicate handleGreaterThan(SearchCriteria criteria) {
        if (root.get(criteria.getName()).getJavaType() == LocalDateTime.class) {
            try {
                LocalDateTime dateValue = LocalDateTime.parse(criteria.getValue().toString());
                return criteriaBuilder.and(predicate,
                        criteriaBuilder.greaterThan(root.get(criteria.getName()), dateValue));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format for " + criteria.getName());
            }
        }
        return criteriaBuilder.and(predicate,
                criteriaBuilder.greaterThan(root.get(criteria.getName()), criteria.getValue().toString()));
    }

    private Predicate handleLessThan(SearchCriteria criteria) {
        if (root.get(criteria.getName()).getJavaType() == LocalDateTime.class) {
            try {
                LocalDateTime dateValue = LocalDateTime.parse(criteria.getValue().toString());
                return criteriaBuilder.and(predicate,
                        criteriaBuilder.lessThan(root.get(criteria.getName()), dateValue));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format for " + criteria.getName());
            }
        }
        return criteriaBuilder.and(predicate,
                criteriaBuilder.lessThan(root.get(criteria.getName()), criteria.getValue().toString()));
    }

    private Predicate handleEquals(SearchCriteria criteria) {
        Class<?> fieldType = root.get(criteria.getName()).getJavaType();

        if (fieldType == String.class) {
            return criteriaBuilder.and(predicate,
                    criteriaBuilder.like(root.get(criteria.getName()),
                            "%" + criteria.getValue().toString() + "%"));
        }
        else if (fieldType == LocalDateTime.class) {
            try {
                LocalDateTime dateValue = LocalDateTime.parse(criteria.getValue().toString());
                return criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get(criteria.getName()), dateValue));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format for " + criteria.getName());
            }
        }
        else if (fieldType.isEnum()) {
            try {
                Class<? extends Enum> enumClass = (Class<? extends Enum>) fieldType;
                Enum<?> enumValue = Enum.valueOf(enumClass, criteria.getValue().toString());
                return criteriaBuilder.and(predicate,
                        criteriaBuilder.equal(root.get(criteria.getName()), enumValue));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid enum value for " + criteria.getName());
            }
        }
        else if (User.class.isAssignableFrom(fieldType)) {
            Join<Order, User> join = root.join(criteria.getName());
            return criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(join.get("id"), criteria.getValue()));
        }
        else {
            return criteriaBuilder.and(predicate,
                    criteriaBuilder.equal(root.get(criteria.getName()), criteria.getValue()));
        }
    }

    public Predicate getPredicate() {
        return predicate;
    }
}
