package com.example.demo.repository;

import com.example.demo.dto.response.PageResponse;
import com.example.demo.entity.Order;
import com.example.demo.repository.criteria.OrderSearchCriteria;
import com.example.demo.repository.criteria.SearchCriteria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SearchRepository {
    @PersistenceContext // dùng để inject 1 EntityManager vào 1 bean trong môi trường JavaEE
    private EntityManager entityManager; //giao diện chính trong JPA để thao tác với cơ sở dữ liệu

    /**
     * Advance search order by criterias
     *
     * @param offset
     * @param pageSize
     * @param params
     * @return
     */
    public PageResponse advanceSearch(int offset,int pageSize,String... params){
        //firstName:T, lastName:T => phải viết 1 biểu thức để mình field vào entity

        //1. lay ra danh sach order
        List<SearchCriteria> searchCriteria = new ArrayList<>();
        if(params != null){
            //duyệt mảng
            for(String s : params){
                //name->operation->value
                Pattern pattern = Pattern.compile("(\\w+?)(:|>|<)(.*)");
                Matcher matcher = pattern.matcher(s);
                if(matcher.find()){
                    searchCriteria.add(new SearchCriteria(matcher.group(1), matcher.group(2),matcher.group(3) ));
                }
            }

        }
        List<Order> orders = getOrders(offset,pageSize,searchCriteria);
        //2. lay ra so luong ban ghi
        Long totalElements = getTotalElements(searchCriteria);

        Page<Order> page = new PageImpl<>(orders, PageRequest.of(offset,pageSize),totalElements);

        return PageResponse.builder()
                .data(orders)
                .pageSize(pageSize)
                .pageNo(offset)
                .totalElement(totalElements)
                .totalPages(page.getTotalPages())
                .pageNo(offset)
                .build();
    }

    private List<Order> getOrders(int offset,int pageSize,List<SearchCriteria> criteria){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder(); // dùng để lấy ra 1 instance của CriteriaBuilder
        CriteriaQuery<Order> query = criteriaBuilder.createQuery(Order.class); // tạo câu truy vấn động cho thực thể Order
        Root<Order> root = query.from(Order.class); // xác định bảng trong truy vấn (FROM Order)

        //xử lý điều kiện tìm kiếm
        Predicate predicate = criteriaBuilder.conjunction();
        //sau đó gọi đến consumer để xử lý toán tử
        OrderSearchCriteria searchCriteria = new OrderSearchCriteria(criteriaBuilder,predicate,root);

        //kiểm tra xem params có giá trị không

        criteria.forEach(searchCriteria);//áp dụng logic thông qua đối tượng searchCriteria
        predicate = searchCriteria.getPredicate();
        query.where(predicate); // gán điều kiện vào câu truy vấn
        return entityManager.createQuery(query)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .getResultList();
    }
    private Long getTotalElements(List<SearchCriteria> params){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Order> root = query.from(Order.class);

        Predicate predicate = criteriaBuilder.conjunction();
        OrderSearchCriteria searchCriteria = new OrderSearchCriteria(criteriaBuilder,predicate,root);
        params.forEach(searchCriteria);
        predicate = searchCriteria.getPredicate();
        query.select(criteriaBuilder.count(root));
        query.where(predicate);
        return entityManager.createQuery(query).getSingleResult();
    }
}
