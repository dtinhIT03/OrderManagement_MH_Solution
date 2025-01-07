package com.example.demo.data_jooq.request;

import com.example.demo.repository.criteria.SearchCriteria;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CriteriaRequest {
    List<SearchCriteria> criteria;
}
