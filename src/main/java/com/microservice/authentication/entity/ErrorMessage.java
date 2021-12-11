package com.microservice.authentication.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorMessage {

    private HttpStatus status;

    private String message;

    private List<String> fieldErrors = new ArrayList<>();

    public ErrorMessage(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.fieldErrors = null;
    }
}
