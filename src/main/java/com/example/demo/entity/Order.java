package com.example.demo.entity;


import com.example.demo.enums.StatusOrder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "`order`")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "date_order",nullable = false)
    LocalDateTime orderDate;

    @Column(name = "date_delivery")
    LocalDateTime deliveryDate;

    @Column(name = "date_recieve")
    LocalDateTime recieveDate;

    @Column(name = "status",nullable = false)
    @Enumerated(EnumType.STRING) // hibernate lưu giá trị enum dưới dạng chuỗi
    //@JdbcTypeCode(SqlTypes.NAMED_ENUM) // chỉ định kiểu mà Hibernate sẽ sử dụng khi chuyển đổi Java sang kiểu trong csdl
    StatusOrder statusOrder;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user_id;

    @JsonIgnore
    @OneToMany(mappedBy = "orderId",cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderProduct> items;

}
