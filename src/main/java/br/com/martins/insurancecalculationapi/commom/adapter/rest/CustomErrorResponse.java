package br.com.martins.insurancecalculationapi.commom.adapter.rest;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CustomErrorResponse {


    private String type;
    private String message;
    private LocalDateTime timestamp;
}
