package com.example.demo.data_jooq.response;

public class ResponseError extends ResponseData{

    public ResponseError(int status, String message) {
        super(status, message);
    }
}
