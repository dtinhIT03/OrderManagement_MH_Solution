package com.example.demo.data_jooq.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PageResponse<T> {
    private int pageNo;
    private int pageSize;
    private int totalPages;
    private long totalElement;
    private T data;
}
