package com.example.demo.data_jooq.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ValidTokenResponse {
    private boolean valid;
}
