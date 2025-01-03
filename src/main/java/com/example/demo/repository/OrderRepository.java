package com.example.demo.repository;

import com.example.demo.entity.Order;
import com.example.demo.enums.StatusOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query(value = """
            SELECT * FROM `order` o WHERE o.status = :status LIMIT :pageSize OFFSET :offset
            """,nativeQuery = true)
    List<Order> findAllByStatusOrder(@Param("status") String statusOrder, @Param("pageSize") int pageSize, @Param("offset") int offset);

    @Query(value = """
            SELECT * FROM `order` o LIMIT :pageSize OFFSET :offset
        """,nativeQuery = true)
    List<Order> findAll(@Param("pageSize") int pageSize, @Param("offset") int offset);
    long countByStatusOrder(StatusOrder status);

    @Query(value = """
            SELECT * FROM `order` o WHERE 1=1 AND (:status IS NULL OR o.status = :status)
            AND (:userId IS NULL OR o.user_id = :userId)
            AND (:orderDate IS NULL OR DATE_FORMAT(o.date_order, '%Y-%m-%d') = :orderDate) LIMIT :pageSize OFFSET :offset
            """,nativeQuery = true)
    List<Order> findAllAdvanced(@Param("status") String status,@Param("userId") Long userId,
                                @Param("orderDate") String orderDate,@Param("pageSize") int pageSize,@Param("offset") int offset);

    @Query(value = """
            SELECT COUNT(*) FROM `order` o WHERE 1=1 AND (:status IS NULL OR o.status = :status)
            AND (:userId IS NULL OR o.user_id = :userId)
            AND (:orderDate IS NULL OR DATE_FORMAT(o.date_order, '%Y-%m-%d') = :orderDate)
            """,nativeQuery = true)
    int countAdvanced(@Param("status") String status,@Param("userId") Long userId,
                      @Param("orderDate") String orderDate);
}
