package com.example.demo.repository.criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//mục đích để query
public class SearchCriteria {
    private String name; // tương ứng với các field Name như firstName, lastName, id, email,...
    private String operation; // các toán tử = , <, >
    private Object value; // Object có thể là Long,Integer, String, ...
}
